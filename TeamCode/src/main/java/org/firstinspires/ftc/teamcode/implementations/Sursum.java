package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;
import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;
import org.firstinspires.ftc.teamcode.tests.VuforiaStuff;

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
    public CRServo flywheels;
    public DcMotor belt;
    public LeftSideArm leftArm;
    public RightSideArm rightArm;
    public SideArm sideArm;
    //public DistanceSensor ods;
    //public DigitalChannel limit;
//    public ColorSensor color_sensor_bottom;
    //public Vision vision;
    //public VuforiaStuff vuforiaStuff;
   // public VuforiaLocalizer vuforia;
    //VuforiaStuff.skystonePos pos;
    //public static final String VUFORIA_KEY = "AZjnTyD/////AAABmWbY5Kf/tUDGlNmyg0to/Ocsr2x5NKR0bN0q9InlH4shr90xC/iovUPDBu+PWzwD2+F8moAWhCpUivQDuKp/j2IHVtyjoKOQvPkTaXAb1IgPtAM6pMDltXDTkQ8Olwds22Z97Wdx+RAPK8WrC809Hj+JDZJJ3/Lx3bqAwcR1TRJ4OejxkWVSAKvFX8rOp5gE82jPNEv1bQ5S+iTgFtToZNQTj2ldtYJjoSkyUHqfODyV3JUazYSu82UEak0My2Ks/zIXYrDEY0y5MgNzRr9pzg3AiA8pbUT3SVk3SSUYmjlml+H9HovgDuiGrnJnmNMSjQGfcGpliGW6fs61ePYuAHvN4+Rwa1esR/prFgYKrTTn";
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
//        color_sensor_bottom = opMode.getHardwareMap().colorSensor.get("color");
        allianceSwitch = opMode.getHardwareMap().digitalChannel.get("allianceSwitch");
        allianceSwitch.setMode(DigitalChannel.Mode.INPUT);
    //    vuforiaStuff = new VuforiaStuff(vuforia);
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
      //  int cameraMonitorViewId = opMode.getHardwareMap().appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.getHardwareMap().appContext.getPackageName());
        //VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        //parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        //vuforia = ClassFactory.getInstance().createVuforia(parameters);
        switch(this.alliance) {
            case RED:
                our_side = DriveTrain.RED_SIDE;
                opponents_side = DriveTrain.BLUE_SIDE;
                sideArm = rightArm;
          //      pos = vuforiaStuff.vuforiascan(false,true);
                break;
            case BLUE:
                our_side = DriveTrain.BLUE_SIDE;
                opponents_side = DriveTrain.RED_SIDE;
                sideArm = leftArm;
            //    pos = vuforiaStuff.vuforiascan(false,false);

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
//        visionTF = new VisionTF(opMode, "Webcam 1");
        /*switch (pos){
            case RIGHT:
                opMode.getTelemetry().addLine("Skystone Pos: RIGHT");
                opMode.getTelemetry().update();
                break;
            case CENTER:
                opMode.getTelemetry().addLine("Skystone Pos: CENTER");
                opMode.getTelemetry().update();
                break;
            case LEFT:
                opMode.getTelemetry().addLine("Skystone Pos: LEFT");
                opMode.getTelemetry().update();
                break;
        }
*/

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
