package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

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
    public Vision vision;
    public VisionTF visionTF;
    public OpModeIF opMode;
    public DigitalChannel allianceSwitch;
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
        allianceSwitch = opMode.getHardwareMap().digitalChannel.get("allianceSwitch");
        allianceSwitch.setMode(DigitalChannel.Mode.INPUT);
        // }}}

//        vision = new Vision(opMode, "Webcam 1");
    }

    public void init() {
        //init(allianceSwitch.getVoltage() / allianceSwitch.getMaxVoltage() > 0.5 ? Alliance.RED : Alliance.BLUE);
        init(allianceSwitch.getState() ? Alliance.BLUE : Alliance.RED);
        opMode.getTelemetry().addData("Alliance", alliance.toString());
        //init(Alliance.RED);
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
        // reset the motors
        driveTrain.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.setModes(DcMotor.RunMode.RUN_USING_ENCODER);
        // set servo positions
        shuttleGate.setState(ShuttleGate.State.CLOSED);
        arm.setState(Arm.State.BELT);
        claw.setState(Claw.State.OPEN);
        leftArm.arm.setState(SideArm.Arm.State.UP);
        rightArm.arm.setState(SideArm.Arm.State.UP);
        leftArm.claw.setState(SideArm.Claw.State.CLOSED);
        rightArm.claw.setState(SideArm.Claw.State.CLOSED);
        // start vision
        visionTF = new VisionTF(opMode, "Webcam 1");
        // set zero power behaviors to float so Kaden can turn the bot
        driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT);
        // tell Kaden the bot can now be pushed
        opMode.getTelemetry().addLine("Initialization DONE");
    }

    /**
     * finds the skystone unsing TensorFlow to detect
     * @throws InterruptedException
     */
    public SkyStonePosition findSkystone() throws InterruptedException {
        for(SkyStonePosition position : new SkyStonePosition[] {SkyStonePosition.THREE_SIX, SkyStonePosition.TWO_FIVE}) {
            Thread.sleep(500);
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
