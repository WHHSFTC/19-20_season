package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;
import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvPipeline;

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
    public Mechanism<ShuttleGate.State> shuttleGate;
    public ContinuousMechanism outputSlides;
    public Mechanism<Arm.State> arm;
    public Mechanism<Claw.State> claw;
    public DcMotorSimple flywheels;
    public DcMotor belt;
    public LeftSideArm leftArm;
    public RightSideArm rightArm;
    public SideArm sideArm;
    public VisionTF visionTF;
    public OpModeIF opMode;
    public Alliance alliance;
    public VisionFTC visionFtc;

    public VisionWall visionTest;

    private DigitalChannel allianceSwitch;

    public OpenCvCamera camera;

    public VisionFromWall pipeline;
    /**
     * Creation of all systems of the bot
     * @param opMode import current opMode to get initialize
     */
    public Sursum(OpModeIF opMode) {
        this.opMode = opMode;
        driveTrain = new DriveTrain(opMode, "motorRF", "motorLF", "motorLB", "motorRB");
        shuttleGate = new ShuttleGate(opMode, "leftGate", "rightGate");

        // output {{{
        outputSlides = new OutputSlides(opMode, "spool");
        arm = new Arm(opMode, "elbow", "wrist");
        claw = new Claw(opMode, "inner", "outer");
        // }}}

        // intake {{{
        flywheels = new FlyWheels(opMode, "leftIn", "rightIn");
        belt = opMode.getHardwareMap().dcMotor.get("belt");
        leftArm = new LeftSideArm(opMode, "leftArm", "leftClaw");
        rightArm = new RightSideArm(opMode, "rightArm", "rightClaw");
        allianceSwitch = opMode.getHardwareMap().digitalChannel.get("allianceSwitch");
        allianceSwitch.setMode(DigitalChannel.Mode.INPUT);
        // }}}
    }

    public void init() {
        init(allianceSwitch.getState() ? Alliance.BLUE : Alliance.RED);
        opMode.getTelemetry().addData("Alliance", alliance.toString());
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
            if (visionTF.getStone()) {
                driveTrain.goAngle(3.25, DriveTrain.LOADING_ZONE, .25);
                return position;
            }
            driveTrain.goAngle(8.0, DriveTrain.LOADING_ZONE, .25);
        }
        driveTrain.goAngle(3.25, DriveTrain.LOADING_ZONE, .25);
        return SkyStonePosition.ONE_FOUR;
    }

    public SkyStonePosition translateRelativePosition(VisionFromWall.Position val) {
        if (val == VisionFromWall.Position.NULL) {
            opMode.getTelemetry().addLine("[ERROR] Found Position.NULL, returning SkyStonePosition.ONE_FOUR");
            opMode.getTelemetry().update();
            return SkyStonePosition.ONE_FOUR;
        }
        if (val == VisionFromWall.Position.MIDDLE) {
            return SkyStonePosition.TWO_FIVE;
        }
        if (alliance == Alliance.RED) {
            return val == VisionFromWall.Position.LEFT ? SkyStonePosition.ONE_FOUR : SkyStonePosition.THREE_SIX;
        } else {
            return val == VisionFromWall.Position.LEFT ? SkyStonePosition.THREE_SIX : SkyStonePosition.ONE_FOUR;
        }
    }

    public SkyStonePosition translateRelativePosition(VisionFTC.SkystonePosition val) {
        if (val == VisionFTC.SkystonePosition.CENTER_STONE) {
            return SkyStonePosition.TWO_FIVE;
        }
        if (alliance == Alliance.RED) {
            return val == VisionFTC.SkystonePosition.LEFT_STONE ? SkyStonePosition.ONE_FOUR : SkyStonePosition.THREE_SIX;
        } else {
            return val == VisionFTC.SkystonePosition.LEFT_STONE ? SkyStonePosition.THREE_SIX : SkyStonePosition.ONE_FOUR;
        }
    }

    /**
     * Intake mechanisms
     * @throws InterruptedException
     */
    public void intake() throws InterruptedException {
        flywheels.setPower(-2.0/3);
        belt.setPower(-1);
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
