package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;

public class DriveTrain implements StrafingDriveTrain {
    private static final double MECANUM_WHEEL_CIRCUMFERENCE = 12.368475;
    private double offset;
    private DcMotor motorRF;
    private DcMotor motorLF;
    private DcMotor motorLB;
    private DcMotor motorRB;
    private LinearOpMode opMode;
    private BNO055IMU imu;

    private static final double P_TURN_COEFF = .018;
    private static final double I_TURN_COEFF = 0.01;
    private static final double D_TURN_COEFF = 0.026;
    private static final double HEADING_THRESHOLD = 5;
    private static final double ANTI_WINDUP = 2;
    private static final double TICKSPERROTATION = 537.6;

    public static final double RED_SIDE = 180;
    public static final double BUILDING_ZONE = 90;
    public static final double BLUE_SIDE =  0;
    public static final double LOADING_ZONE = 270;

    public DriveTrain(LinearOpMode opMode) {
        this.opMode = opMode;
        motorRF = opMode.hardwareMap.dcMotor.get("motorRF");
        motorLF = opMode.hardwareMap.dcMotor.get("motorLF");
        motorLB = opMode.hardwareMap.dcMotor.get("motorLB");
        motorRB = opMode.hardwareMap.dcMotor.get("motorRB");
        motorRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        initImu();
    }

    public void stubify() {
        motorRF = new DcMotorStub(opMode);
        motorLF = new DcMotorStub(opMode);
        motorLB = new DcMotorStub(opMode);
        motorRB = new DcMotorStub(opMode);
        motorRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void goArc(double centerX, double centerY, double angle, double power) {
        // not implemented
    }

    public void initImu() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
    }

    public double getRawHeading() {
        Orientation ret = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        double heading = ret.firstAngle;
        return heading;
    }

    public double getHeading() {
        return getRawHeading() + offset;
    }

    // strafes at cartesian vector
    @Override
    public void goVector(double x, double y, double power) {
        double angle = Math.toDegrees(Math.atan2(y, x));
        goAngle(Math.hypot(x, y), angle, power);
    }

    @Override
    public void dumpMotors() {
        opMode.telemetry.addData("motorRF", motorRF);
        opMode.telemetry.addData("motorLF", motorLF);
        opMode.telemetry.addData("motorLB", motorLB);
        opMode.telemetry.addData("motorRB", motorRB);
    }

    public void setPowers(double rf, double lf, double lb, double rb) {
        motorRF.setPower(rf);
        motorLF.setPower(lf);
        motorLB.setPower(lb);
        motorRB.setPower(rb);
    }

    @Override
    public void setModes(DcMotor.RunMode mode) {
        motorLF.setMode(mode);
        motorRF.setMode(mode);
        motorRB.setMode(mode);
        motorLB.setMode(mode);
    }

    public void stop() {
        halt();
    }
    public void halt() {
        motorRF.setPower(0);
        motorLF.setPower(0);
        motorLB.setPower(0);
        motorRB.setPower(0);
    }

    // absolute
    public void rotate(double angle) {
        //Turn using PID
        // clockwise = negative input, counter-clockwise = positive input
        double angleWanted = getHeading() + angle;
        double rcw = 1;
        double integral = 0;
        double previous_error = 0;
        while (rcw != 0 && opMode.opModeIsActive()) {
            double error = angleWanted - getHeading();
            while (error > 180 && opMode.opModeIsActive()) error -= 360;
            while (error < -180 && opMode.opModeIsActive()) error += 360;
            double derivative = error - previous_error;
            // small margin of error for increased speed
            if (Math.abs(error) < HEADING_THRESHOLD) {
                error = 0;
            }
            // prevents integral from growing too large
            if (Math.abs(error) < ANTI_WINDUP && error != 0) {
                integral += error;
            } else {
                integral = 0;
            }
            if (integral > (50 / I_TURN_COEFF)) {
                integral = 50 / I_TURN_COEFF;
            }
            if (error == 0) {
                derivative = 0;
            }
            rcw = P_TURN_COEFF * error + I_TURN_COEFF * integral + D_TURN_COEFF * derivative;
            previous_error = error;
            accelerate(rcw);

            opMode.telemetry.addData("first angle", getHeading());
            opMode.telemetry.addData("speed ", rcw);
            opMode.telemetry.addData("error", angleWanted - getHeading());
            opMode.telemetry.addData("angleWanted", angleWanted);
            opMode.telemetry.addData("motor power", motorLF.getPower());
            opMode.telemetry.addData("rcw", rcw);
            opMode.telemetry.addData("P", P_TURN_COEFF * error);
            opMode.telemetry.addData("I", I_TURN_COEFF * integral);
            opMode.telemetry.addData("D", D_TURN_COEFF * derivative);
            opMode.telemetry.update();

            try {
                Thread.sleep(20);
            } catch(InterruptedException ex) {
                return;
            }
        }
        accelerate(0);
    }

    public void accelerate(double speed) {
        double clip_speed = Range.clip(speed, -1, 1);
        motorLF.setPower(clip_speed);
        motorRF.setPower(clip_speed);
        motorRB.setPower(clip_speed);
        motorLB.setPower(clip_speed);
    }

    // takes global angle
    public void goAngle(double dist, double angle, double power) {
        // local angle
        double angel = Math.toRadians(angle+180-getHeading());
        setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModes(DcMotor.RunMode.RUN_TO_POSITION);
        double x = Math.cos(angel);
        double y = Math.sin(angel);
        double distance = dist / (MECANUM_WHEEL_CIRCUMFERENCE);
        double ticks = TICKSPERROTATION * distance;
        int ticksRF = (int) Math.round(ticks * (y - x));
        int ticksLF = (int) Math.round(ticks * (-y - x));
        int ticksLB = (int) Math.round(ticks * (-y + x));
        int ticksRB = (int) Math.round(ticks * (y + x));
        motorLF.setTargetPosition(ticksLF);
        motorRF.setTargetPosition(ticksRF);
        motorRB.setTargetPosition(ticksRB);
        motorLB.setTargetPosition(ticksLB);
        motorRF.setPower(power * (y - x));
        motorLF.setPower(-power * (-y - x));
        motorLB.setPower(-power * (-y + x));
        motorRB.setPower(power * (y + x));
        while (opMode.opModeIsActive()  &&
                motorLB.isBusy() &&
                motorRB.isBusy() &&
                motorRF.isBusy() &&
                motorLF.isBusy()) {
            opMode.telemetry.addData("Raw Heading", getRawHeading());
            opMode.telemetry.addData("Heading", getHeading());
            opMode.telemetry.addData("Angle", angel);
            opMode.telemetry.addData("Path2", "Running at %7d :%7d",
                    motorLB.getCurrentPosition(),
                    motorLF.getCurrentPosition(),
                    motorRB.getCurrentPosition(),
                    motorRF.getCurrentPosition());
            opMode.telemetry.addData("target", "Running at %7d :%7d",
                    motorLB.getTargetPosition(),
                    motorLF.getTargetPosition(),
                    motorRB.getTargetPosition(),
                    motorRF.getTargetPosition());
            opMode.telemetry.addData("gyroHeading", imu.getAngularOrientation());
            dumpMotors();
            opMode.telemetry.update();
        }
        setPowers(0,0,0,0);
        setModes(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void startAngle(double angle, double power) {
        angle = angle-180-getHeading();
        double angel = Math.toRadians(angle);
        double x = Math.cos(angel);
        double y = Math.sin(angel);
        motorRF.setPower(power * (y - x));
        motorLF.setPower(-power * (-y - x));
        motorLB.setPower(-power * (-y + x));
        motorRB.setPower(power * (y + x));
    }
    @Override
    public void setHeading(double angle) {
        offset = angle - getRawHeading();
    }
}
