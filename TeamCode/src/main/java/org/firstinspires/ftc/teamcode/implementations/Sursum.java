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
    public ColorSensor color_sensor;
//    public Vision vision;
    public VisionTF visionTF;
    // initialization
    public Sursum(LinearOpMode opMode) {
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
//        rightArm.arm.setDirection(Servo.Direction.REVERSE);
        rightArm.claw.setDirection(Servo.Direction.REVERSE);
        // }}}

        // sensors {{{
        //ods = opMode.hardwareMap.get(DistanceSensor.class, "ods");
        //limit = opMode.hardwareMap.digitalChannel.get("limit");
        color_sensor = opMode.hardwareMap.colorSensor.get("color");
        // }}}

//        vision = new Vision(opMode, "Webcam 1");
        visionTF = new VisionTF(opMode, "Webcam 1");
    }
    public void init() {
        driveTrain.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.setModes(DcMotor.RunMode.RUN_USING_ENCODER);
        driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT);
        shuttleGate.setState(ShuttleGate.State.CLOSED);
        arm.setState(Arm.State.BELT);
        claw.setState(Claw.State.OPEN);
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

    public SkyStonePosition findSkystone() throws InterruptedException {
        for(SkyStonePosition position : new SkyStonePosition[] {SkyStonePosition.THREE_SIX, SkyStonePosition.TWO_FIVE}) {
            if (visionTF.getStone() == "skystone") {
                return position;
            }
            driveTrain.goAngle(8, DriveTrain.LOADING_ZONE, .25);
        }
        return SkyStonePosition.ONE_FOUR;
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
