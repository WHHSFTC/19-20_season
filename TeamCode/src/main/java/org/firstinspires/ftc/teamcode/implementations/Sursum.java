package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;
import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;

// the bot
public class Sursum {
    public static final double ROBOT_WIDTH = 17.75;
    public static final double ROBOT_LENGTH = 17.75;
    public static final double SIDEARM_Y = ROBOT_LENGTH/2 - 5;
    public static final double CAMERA_X = ROBOT_WIDTH/2 - 2;
    // declarations
    public StrafingDriveTrain driveTrain;
    public Mechanism shuttleGate;
    public ContinuousMechanism outputSlides;
    public Mechanism arm;
    public Mechanism claw;
    public CRServo flywheels;
    public DcMotor belt;
    public SideArm leftArm;
    public SideArm rightArm;
    //public DistanceSensor ods;
    //public DigitalChannel limit;
    public ColorSensor color_sensor_bottom;
//    public Vision vision;
    public VisionTF visionTF;
    public LinearOpMode opMode;
    // initialization
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
        leftArm = new SideArm(opMode, "leftArm", "leftClaw");
        rightArm = new SideArm(opMode, "rightArm", "rightClaw");
        rightArm.arm.setDirection(Servo.Direction.REVERSE);
        rightArm.claw.setDirection(Servo.Direction.REVERSE);
        // }}}

        // sensors {{{
        //ods = opMode.hardwareMap.get(DistanceSensor.class, "ods");
        //limit = opMode.hardwareMap.digitalChannel.get("limit");
        color_sensor_bottom = opMode.hardwareMap.colorSensor.get("color");
        // }}}

//        vision = new Vision(opMode, "Webcam 1");
    }
    public void init() {
        driveTrain.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.setModes(DcMotor.RunMode.RUN_USING_ENCODER);
        driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT);
        shuttleGate.setState(ShuttleGate.State.CLOSED);
        arm.setState(Arm.State.BELT);
        claw.setState(Claw.State.OPEN);
        visionTF = new VisionTF(opMode, "Webcam 1");
    }

    public void redInit() {
        init();
        rightArm.setArmPosition(SideArm.Arm.State.DOWN);
        rightArm.setClawPosition(SideArm.Claw.State.OPEN);
    }

    public void blueInit() {
        init();
        leftArm.setArmPosition(SideArm.Arm.State.DOWN);
        leftArm.setClawPosition(SideArm.Claw.State.OPEN);
    }

    public void findSkystone() throws InterruptedException {
        for(SkyStonePosition position : new SkyStonePosition[] {SkyStonePosition.THREE_SIX, SkyStonePosition.TWO_FIVE}) {
            String object = visionTF.getStone().toLowerCase();
            Thread.sleep(1000);
            opMode.telemetry.addData("Tensorflow Object", object);
            opMode.telemetry.addData("Confidence: ", "N/A");
            opMode.telemetry.update();
            if (object.equals("skystone")) {
                break;
            }
            driveTrain.goAngle(8, DriveTrain.LOADING_ZONE, .25);
        }
        // backing up
        driveTrain.goAngle(ROBOT_LENGTH/2, DriveTrain.RED_SIDE, .5);

        // turn so sidearm faces stones
        driveTrain.rotate(90);

        // lines up sidearm
        driveTrain.goAngle(4, DriveTrain.BUILDING_ZONE, .5);

        // moves forward to be line with stone
        driveTrain.goAngle(13, DriveTrain.BLUE_SIDE, .5);

        // FAILSAFE
        rightArm.setClawPosition(SideArm.Claw.State.OPEN);

        // arm comes down
        rightArm.setArmPosition(SideArm.Arm.State.DOWN);
        Thread.sleep(1000);

        // claw closes on stone
        rightArm.setClawPosition(SideArm.Claw.State.CLOSED);
        Thread.sleep(1000);

        // holding stone
        rightArm.setArmPosition(SideArm.Arm.State.HOLD);

        opMode.telemetry.addLine("Holding Stone");
        opMode.telemetry.update();
    }
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
