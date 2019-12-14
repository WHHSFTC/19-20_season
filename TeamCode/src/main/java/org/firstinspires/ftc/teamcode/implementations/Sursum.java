package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;
import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;

/**
 * Bot Class
 */
public class Sursum {

    // fields that changed based on what side we start on
    public double our_side;
    public double opponents_side;

    // Constants that contain magic numbers
    public static final double ROBOT_WIDTH = 17.75;
    public static final double ROBOT_LENGTH = 17.75;
    public static final double SIDEARM_Y = ROBOT_LENGTH/2 - 5;
    public static final double CAMERA_X = ROBOT_WIDTH/2 - 2.5;
    public static final double SIDE_ARM_OFFSET = 8; //inches

    // declarations of all systems
    public StrafingDriveTrain driveTrain;
    public Mechanism shuttleGate;
    public ContinuousMechanism outputSlides;
    public Mechanism arm;
    public Mechanism claw;
    public CRServo flywheels;
    public DcMotor belt;
    public LeftSideArm leftArm;
    public RightSideArm rightArm;
    public SideArm sideArm;
    //public DistanceSensor ods;
    //public DigitalChannel limit;
    public ColorSensor color_sensor_bottom;
//    public Vision vision;
    public VisionTF visionTF;
    public OpModeIF opMode;
    public AnalogInput allianceSwitch;
    public Alliance alliance;

    /**
     * Creation of all systems of the bot
     * @param opMode import current opMode to get initialize
     */
    public Sursum(OpModeIF opMode) {
        this.opMode = opMode;
        driveTrain = new DriveTrain(opMode, "motorRF", "motorLF", "motorLB", "motorRB");
        // ((DriveTrain) driveTrain).stubify();
        shuttleGate = new ShuttleGate(opMode, "leftGate", "rightGate");


        // output {{{
        outputSlides = new OutputSlides(opMode, "spool1", "spool2", "spool3");
        arm = new Arm(opMode, "elbow", "wrist");
        claw = new Claw(opMode, "inner", "outer");
        // }}}

        // intake {{{
        flywheels = new Flywheels(opMode, "leftFly", "rightFly");
        belt = opMode.getHardwareMap().dcMotor.get("belt");
        leftArm = new LeftSideArm(opMode, "leftArm", "leftClaw");
        rightArm = new RightSideArm(opMode, "rightArm", "rightClaw");
//        rightArm.arm.setDirection(Servo.Direction.REVERSE);
//        rightArm.claw.setDirection(Servo.Direction.REVERSE);
        // }}}

        // sensors {{{
        //ods = opMode.hardwareMap.get(DistanceSensor.class, "ods");
        //limit = opMode.hardwareMap.digitalChannel.get("limit");
        color_sensor_bottom = opMode.getHardwareMap().colorSensor.get("color");
        allianceSwitch = opMode.getHardwareMap().analogInput.get("allianceSwitch");
        // }}}

//        vision = new Vision(opMode, "Webcam 1");
    }

    public void init() {
        //init(allianceSwitch.getVoltage() / allianceSwitch.getMaxVoltage() > 0.5 ? Alliance.RED : Alliance.BLUE);
        init(Alliance.RED);
    }

    public void init(Alliance alliance) {
        this.alliance = alliance;
        switch(this.alliance) {
            case RED:
                our_side = DriveTrain.RED_SIDE;
                opponents_side = DriveTrain.BLUE_SIDE;
                sideArm = rightArm;
                break;
            case BLUE:
                our_side = DriveTrain.BLUE_SIDE;
                opponents_side = DriveTrain.RED_SIDE;
                sideArm = leftArm;
        }
        driveTrain.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.setModes(DcMotor.RunMode.RUN_USING_ENCODER);
        shuttleGate.setState(ShuttleGate.State.CLOSED);
        arm.setState(Arm.State.BELT);
        claw.setState(Claw.State.OPEN);
        leftArm.arm.setState(SideArm.Arm.State.UP);
        rightArm.arm.setState(SideArm.Arm.State.UP);
        leftArm.claw.setState(SideArm.Claw.State.CLOSED);
        rightArm.claw.setState(SideArm.Claw.State.CLOSED);
        visionTF = new VisionTF(opMode, "Webcam 1");
        driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT);
        opMode.getTelemetry().addLine("Initialization DONE");
        opMode.getTelemetry().update();
    }

    /**
     * finds the skystone unsing TensorFlow to detect
     * @throws InterruptedException
     */
    public SkyStonePosition findSkystone() throws InterruptedException {
        for(SkyStonePosition position : new SkyStonePosition[] {SkyStonePosition.THREE_SIX, SkyStonePosition.TWO_FIVE}) {
            Thread.sleep(1000);
//            opMode.telemetry.addData("Tensorflow Object", object);
//            opMode.telemetry.addData("Confidence: ", "N/A");
//            opMode.telemetry.update();
            if (visionTF.getStone()) {
                return position;
            }
            driveTrain.goAngle(8.5, DriveTrain.LOADING_ZONE, .25);
        }
        return SkyStonePosition.ONE_FOUR;
    }

    /**
     * picks up skystone found by findSkystone()
     * @throws InterruptedException
     */
    public void intakeSkyStoneRed() throws InterruptedException {
    }

    /**
     * picks up skystone found by findSkystone()
     * @throws InterruptedException
     */
    public void intakeSkyStoneBlue() throws InterruptedException {
        // backing up
        driveTrain.goAngle(10, our_side, .5);

        // turn so sidearm faces stones
        driveTrain.align(DriveTrain.LOADING_ZONE);

        // lines up sidearm
        driveTrain.goAngle(SIDEARM_Y + CAMERA_X - 2, DriveTrain.BUILDING_ZONE, .25);

        leftArm.arm.setState(SideArm.Arm.State.DOWN);
        leftArm.claw.setState(SideArm.Claw.State.OPEN);

        // moves forward to be line with stone
        driveTrain.goAngle(18  + (ROBOT_LENGTH - ROBOT_WIDTH)/2, opponents_side,.5);

        // claw closes on stone
        leftArm.claw.setState(SideArm.Claw.State.CLOSED);
        Thread.sleep(500);

        // holding stone
        leftArm.arm.setState(SideArm.Arm.State.HOLD);
        Thread.sleep(500);

        opMode.getTelemetry().addLine("Holding Stone");
        opMode.getTelemetry().update();
    }

    /**
     * moves foundation slow
     * @throws InterruptedException
     */
    public void skystoneFoundationRed() throws InterruptedException {
    }

    public void skystoneFoundationBlue() throws InterruptedException {
        // drive towards stones
        driveTrain.goAngle( 43-ROBOT_LENGTH, opponents_side, .25);

        opMode.getTelemetry().addLine("Starting TensorFlow Search");
        opMode.getTelemetry().update();

        SkyStonePosition sky_stone_position = findSkystone();

        intakeSkyStoneBlue();

        // TODO FORK IF PARTNER PARKED IN LINE

        // heads back to go under skybridge
        driveTrain.goAngle(18, our_side, .25);

        // heads to wall to line up
        driveTrain.goAngle(sky_stone_position.getDistance() - SIDE_ARM_OFFSET + 72, DriveTrain.BUILDING_ZONE, 1);
//        bot.driveTrain.goAngle(tile_distance(0.75), DriveTrain.BUILDING_ZONE, POWER/2);

        driveTrain.align(DriveTrain.LOADING_ZONE);

        driveTrain.goAngle(32, opponents_side, 1.0/4);

        // drops stone onto foundation
        leftArm.claw.setState(SideArm.Claw.State.OPEN);
        Thread.sleep(500);

        // raises arm
        leftArm.arm.setState(SideArm.Arm.State.UP);
        // closes claw so andrew doesn't have to make a new one
        leftArm.claw.setState(SideArm.Claw.State.CLOSED);
        Thread.sleep(500);

        // moving out to turn
        driveTrain.goAngle(12, our_side, 1.0/2);

        // turning back to face foundation
        driveTrain.align(our_side);

        // heads back to foundation
        driveTrain.goAngle(18, opponents_side, 1.0/2);

        // activate foundation hooks
        shuttleGate.setState(ShuttleGate.State.FOUNDATION);
        Thread.sleep(500);

        // pulls foundation
        driveTrain.goAngle(52, our_side, 1.0);

        // deactivate foundation hooks
        shuttleGate.setState(ShuttleGate.State.CLOSED);
        Thread.sleep(500);

        // parking
        driveTrain.goAngle(54, DriveTrain.LOADING_ZONE, 0.5);

    }

    /**
     * Intake mechanisms
     * @throws InterruptedException
     */
    public void intake() throws InterruptedException {
        flywheels.setPower(-2.0/3);
        belt.setPower(-1);
      //while (!limit.getState()) {
      //    Thread.sleep(100);
      //}
        Thread.sleep(500);
        flywheels.setPower(0);
        Thread.sleep(500);
        belt.setPower(0);
    }

    /**
     * stops bot completely
     */
    public void stop() {
        driveTrain.stop();
        shuttleGate.stop();
        outputSlides.stop();
        arm.stop();
        claw.stop();
        flywheels.setPower(0);
        belt.setPower(0);
    }
    public void start() {
        driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
