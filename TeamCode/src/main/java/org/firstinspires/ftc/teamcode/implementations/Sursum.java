package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;
import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
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
    public static final double CAMERA_X = ROBOT_WIDTH/2 - 2;
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
    //public DistanceSensor ods;
    //public DigitalChannel limit;
    public ColorSensor color_sensor_bottom;
//    public Vision vision;
    public VisionTF visionTF;
    public LinearOpMode opMode;

    /**
     * Creation of all systems of the bot
     * @param opMode import current opMode to get initialize
     */
    public Sursum(LinearOpMode opMode) {
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
        belt = opMode.hardwareMap.dcMotor.get("belt");
        leftArm = new LeftSideArm(opMode, "leftArm", "leftClaw");
        rightArm = new RightSideArm(opMode, "rightArm", "rightClaw");
//        rightArm.arm.setDirection(Servo.Direction.REVERSE);
//        rightArm.claw.setDirection(Servo.Direction.REVERSE);
        // }}}

        // sensors {{{
        //ods = opMode.hardwareMap.get(DistanceSensor.class, "ods");
        //limit = opMode.hardwareMap.digitalChannel.get("limit");
        color_sensor_bottom = opMode.hardwareMap.colorSensor.get("color");
        // }}}

//        vision = new Vision(opMode, "Webcam 1");
    }

    /**
     * generic initialization
     */
    public void init() {
        driveTrain.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.setModes(DcMotor.RunMode.RUN_USING_ENCODER);
        driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT);
        shuttleGate.setState(ShuttleGate.State.CLOSED);
        arm.setState(Arm.State.BELT);
        claw.setState(Claw.State.OPEN);
        leftArm.setArmPosition(LeftSideArm.Arm.State.UP);
        rightArm.setArmPosition(RightSideArm.Arm.State.UP);
        leftArm.setClawPosition(LeftSideArm.Claw.State.CLOSED);
        rightArm.setClawPosition(RightSideArm.Claw.State.CLOSED);
        visionTF = new VisionTF(opMode, "Webcam 1");
        opMode.telemetry.addLine("Initialization DONE");
        opMode.telemetry.update();
    }

    /**
     * Initialization for Red Side
     */
    public void redInit() {
        init();
        our_side = DriveTrain.RED_SIDE;
        opponents_side = DriveTrain.BLUE_SIDE;
    }

    /**
     * Initialization for Blue Side
     */
    public void blueInit() {
        init();
        our_side = DriveTrain.BLUE_SIDE;
        opponents_side = DriveTrain.RED_SIDE;
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
            driveTrain.goAngle(8, DriveTrain.LOADING_ZONE, .25);
        }
        return SkyStonePosition.ONE_FOUR;
    }

    /**
     * picks up skystone found by findSkystone()
     * @throws InterruptedException
     */
    public void pick_up_stone(double angle) throws InterruptedException {
        // backing up
        driveTrain.goAngle(10, our_side, .5);

        // turn so sidearm faces stones
        driveTrain.rotate(angle);

        // lines up sidearm
        driveTrain.goAngle(SIDEARM_Y + CAMERA_X - 3, DriveTrain.BUILDING_ZONE, .25);

        rightArm.setArmPosition(RightSideArm.Arm.State.DOWN);
        rightArm.setClawPosition(RightSideArm.Claw.State.OPEN);

        // moves forward to be line with stone
        driveTrain.goAngle(14  + (ROBOT_LENGTH - ROBOT_WIDTH)/2, opponents_side,.5);

        // claw closes on stone
        rightArm.setClawPosition(RightSideArm.Claw.State.CLOSED);
        Thread.sleep(500);

        // holding stone
        rightArm.setArmPosition(RightSideArm.Arm.State.HOLD);
        Thread.sleep(500);

        opMode.telemetry.addLine("Holding Stone");
        opMode.telemetry.update();
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
}
