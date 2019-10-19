package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by khadija on 10/19/2019.
 */
public class Bot {
    //Drive motors
    public DcMotor frontLeftDrive = null;
    public DcMotor frontRightDrive = null;
    public DcMotor backLeftDrive = null;
    public DcMotor backRightDrive = null;

    //Lift motors
    public DcMotor lift1 = null;
    public DcMotor lift2 = null;
    public DcMotor lift3 = null;

    //Intake servo/motors
    public CRServo intakeLeft = null;
    public CRServo intakeRight = null;

    //Random stuff
    public HardwareMap hwMap = null;
    public LinearOpMode opMode = null;

    //IMU stuff
    public BNO055IMU imu = null;
    public Orientation angles = null;
    public Acceleration gravity = null;

    //Claw servos
    public Servo servo1 = null;
    public Servo servo2 = null;
    public Servo servo3 = null;
    public Servo servo4 = null;

    //Holder servo
    public Servo holdServo = null;

    //Conveyor motor
    public DcMotor conveyor = null;

    //Vuforia Tracking Stuff
    public int cameraMonitorViewId;
    VuforiaTrackables SkystoneTrackables;
    VuforiaTrackable SkystoneTemplate;
    public SimpleVuforia vuforia;

    public Bot(LinearOpMode opMode) {
        this.opMode = opMode;
    }


    public void init(HardwareMap ahwMap) {


        hwMap = ahwMap;
        //for drive
        frontLeftDrive = hwMap.get(DcMotor.class, "frontLeft");
        frontRightDrive = hwMap.get(DcMotor.class, "frontRight");
        backLeftDrive = hwMap.get(DcMotor.class, "backLeft");
        backRightDrive = hwMap.get(DcMotor.class, "backRight");
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        // For Lift
        lift1 = hwMap.get(DcMotor.class, "lift1");
        lift2 = hwMap.get(DcMotor.class, "lift2");
        lift3 = hwMap.get(DcMotor.class, "lift3");

        // for intake
        intakeLeft = hwMap.get(CRServo.class, "leftIntake");
        intakeRight = hwMap.get(CRServo.class, "rightIntake");
        intakeLeft.setDirection(DcMotor.Direction.FORWARD);
        intakeRight.setDirection(DcMotor.Direction.REVERSE);

        //IMU stuff
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // For Claw
        servo1 = hwMap.get(Servo.class,"Servo1");
        servo2 = hwMap.get(Servo.class,"Servo2");
        servo3 = hwMap.get(Servo.class,"Servo3");
        servo4 = hwMap.get(Servo.class,"Servo4");

        //For back holder
        holdServo = hwMap.get(Servo.class, "holdServo");

        //For conveyor
        conveyor = hwMap.get(DcMotor.class, "conveyor");

        //stop coasting code
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        String vuforiaKey = "...";
        vuforia = new SimpleVuforia(vuforiaKey);


    }
    //drive
    public void setPower(double leftPower, double rightPower) {
        backLeftDrive.setPower(leftPower);
        backRightDrive.setPower(rightPower);
        frontLeftDrive.setPower(leftPower);
        frontRightDrive.setPower(rightPower);
    }
    //intake
    public void setIntake(double leftPower, double rightPower){
        intakeLeft.setPower(leftPower);
        intakeRight.setPower(rightPower);
    }


    //Obtain the imu's gyro sensor stuff
    public double getGyroHeading() {
        // Update gyro
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity = imu.getGravity();
        double heading = AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle));
        return heading;
    }
    private ElapsedTime runtime = new ElapsedTime();
    public double getRunTime (){
        return runtime.seconds();
    }
    public void gyroTurn(double target, double speed){
        while(!(getGyroHeading() < target + 1 && getGyroHeading() > target -1));
        setPower(speed,-speed);

    }
    static final double COUNTS_PER_MOTOR_REV = 537.6;
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 3.93;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    static final double INCHES_PER_DEGREE = 0.1744;

    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;

        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newFrontLeftTarget = frontLeftDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newFrontRightTarget = frontRightDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newBackLeftTarget = frontLeftDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH * INCHES_PER_DEGREE);
            newBackRightTarget = frontRightDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH * INCHES_PER_DEGREE);
            frontLeftDrive.setTargetPosition(newFrontLeftTarget);
            frontRightDrive.setTargetPosition(newFrontRightTarget);
            backLeftDrive.setTargetPosition(newBackLeftTarget);
            backRightDrive.setTargetPosition(newBackRightTarget);

            // Turn On RUN_TO_POSITION
            frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            frontLeftDrive.setPower(Math.abs(speed));
            frontRightDrive.setPower(Math.abs(speed));
            backLeftDrive.setPower(Math.abs(speed));
            backRightDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (frontLeftDrive.isBusy() && frontRightDrive.isBusy() && backLeftDrive.isBusy() && backRightDrive.isBusy())) {

                // Display it for the driver.
                opMode.telemetry.addData("Path1", "Running to %7d :%7d", newFrontLeftTarget, newFrontRightTarget, newBackLeftTarget, newBackRightTarget);
                opMode.telemetry.addData("Path2", "Running at %7d :%7d",
                        frontLeftDrive.getCurrentPosition(),
                        frontRightDrive.getCurrentPosition(),
                        backLeftDrive.getCurrentPosition(),
                        backRightDrive.getCurrentPosition());
                opMode.telemetry.update();
            }
        }
        // Stop all motion;
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);

        // Turn off RUN_TO_POSITION
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void encoderTurn(double speed, double degrees, double timeoutS) {
        int newfrontLeftTarget;
        int newfrontRightTarget;
        int newbackLeftTarget;
        int newbackRightTarget;
        int newRightTurnTarget;
        int newLeftTurnTarget;





        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newfrontLeftTarget = frontLeftDrive.getCurrentPosition() + (int) (-degrees * COUNTS_PER_INCH * INCHES_PER_DEGREE);
            newfrontRightTarget = frontRightDrive.getCurrentPosition() + (int) (degrees * COUNTS_PER_INCH * INCHES_PER_DEGREE);
            newbackLeftTarget = frontLeftDrive.getCurrentPosition() + (int) (-degrees * COUNTS_PER_INCH * INCHES_PER_DEGREE);
            newbackRightTarget = frontRightDrive.getCurrentPosition() + (int) (degrees * COUNTS_PER_INCH * INCHES_PER_DEGREE);
            frontLeftDrive.setTargetPosition(newfrontLeftTarget);
            frontRightDrive.setTargetPosition(newfrontRightTarget);
            backLeftDrive.setTargetPosition(newbackLeftTarget);
            backRightDrive.setTargetPosition(newbackRightTarget);
            // Turn On RUN_TO_POSITION
            frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            frontLeftDrive.setPower(Math.abs(speed));
            frontRightDrive.setPower(Math.abs(speed));
            backLeftDrive.setPower(Math.abs(speed));
            backRightDrive.setPower(Math.abs(speed));
        }
        while (opMode.opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (frontLeftDrive.isBusy() && frontRightDrive.isBusy() && backLeftDrive.isBusy() && backRightDrive.isBusy())) {
            opMode.telemetry.addData("frontLeftEncoder", frontLeftDrive.getCurrentPosition());
            opMode.telemetry.addData("frontRightEncoder", frontRightDrive.getCurrentPosition());
            opMode.telemetry.addData("backLeftEncoder", backLeftDrive.getCurrentPosition());
            opMode.telemetry.addData("backRightEncoder", backRightDrive.getCurrentPosition());
            opMode.telemetry.update();
        }
        // Stop all motion;
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);

        // Turn off RUN_TO_POSITION
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void resetEncoder() {
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }
    public void autoDriveStraight(double distance) {
        double ticks = COUNTS_PER_INCH * distance;
        ticks += frontLeftDrive.getCurrentPosition();
        while (frontLeftDrive.getCurrentPosition() < ticks) {
            setPower(1, 1);
        }
        setPower(0, 0);
    }

    public double getFrontLeft() {
        return frontLeftDrive.getCurrentPosition();
    }

    public double getFrontRight() {
        return frontRightDrive.getCurrentPosition();
    }

    public double getBackLeft() {
        return backLeftDrive.getCurrentPosition();
    }

    public double getBackRight() {
        return backRightDrive.getCurrentPosition();


    }

    public class SimpleVuforia {
        /**
         * Creates a Vuforia localizer and starts localization.
         * @param vuforiaLicenseKey The license key to access Vuforia code.
         */
        public SimpleVuforia(String vuforiaLicenseKey) {
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
            parameters.vuforiaLicenseKey = vuforiaLicenseKey;
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

            relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
            relicTemplate = relicTrackables.get(0);
            relicTrackables.activate();
        }

        /**
         * Returns the last detected mark type.
         */
        public RelicRecoveryVuMark detectMark() {
            return RelicRecoveryVuMark.from(relicTemplate);
        }


        // The external Vuforia ID localizer.
        private VuforiaLocalizer vuforia;
        private VuforiaTrackables relicTrackables;
        private VuforiaTrackable relicTemplate;
    }
}
