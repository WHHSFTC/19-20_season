package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;

import java.util.*;

public class DriveTrain implements StrafingDriveTrain {
    private static final double MECANUM_WHEEL_CIRCUMFERENCE = 12.368475;
    private double offset;
    private DcMotor motorRF;
    private DcMotor motorLF;
    private DcMotor motorLB;
    private DcMotor motorRB;
    private OpModeIF opMode;
    private BNO055IMU imu;

    private static double P_TURN_COEFF; //.018;
    private static double I_TURN_COEFF; //0.01;
    private static double D_TURN_COEFF; //0.02;
    private static final double HEADING_THRESHOLD = 1;
    private static final double ANTI_WINDUP = 2;
    private static final double TICKSPERROTATION = 537.6;

    public static final double RED_SIDE = 180;
    public static final double BUILDING_ZONE = 270;
    public static final double BLUE_SIDE =  0;
    public static final double LOADING_ZONE = 90;

    public DriveTrain(OpModeIF opMode, String rf, String lf, String lb, String rb) {
        this.opMode = opMode;
        motorRF = opMode.getHardwareMap().dcMotor.get(rf);
        motorLF = opMode.getHardwareMap().dcMotor.get(lf);
        motorLB = opMode.getHardwareMap().dcMotor.get(lb);
        motorRB = opMode.getHardwareMap().dcMotor.get(rb);
        setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);
        imu = opMode.getHardwareMap().get(BNO055IMU.class, "imu");
        initImu();
        P_TURN_COEFF = RobotConstants.TURNING_PID.p * RobotConstants.SCALAR;
        I_TURN_COEFF = RobotConstants.TURNING_PID.i * RobotConstants.SCALAR;
        D_TURN_COEFF = RobotConstants.TURNING_PID.d * RobotConstants.SCALAR;
    }

    public void stubify() {
        motorRF = new DcMotorStub(opMode);
        motorLF = new DcMotorStub(opMode);
        motorLB = new DcMotorStub(opMode);
        motorRB = new DcMotorStub(opMode);
        setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void goArc(double distance,double frontAngle, double turnAngle,double power) throws InterruptedException {
        setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModes(DcMotor.RunMode.RUN_USING_ENCODER);

        /*
        Drives the robot in an arc, by driving two separate arcs using two pairs of wheels. One pair drives the outer arc,
        and the other drives the inner arc. Powering the outer wheels faster than the inner wheels the creates the desired arc.
        */

        double turnAngel = Math.toRadians(turnAngle);
        double radius = (distance/2)/Math.sin(Math.abs(turnAngel)/2);

        //compute how many rotations to move desired distance
        double rotations = distance / (MECANUM_WHEEL_CIRCUMFERENCE);

        //the angle on the robot that is considered the front, in radians
        double frontAngel = Math.toRadians(frontAngle);

        //extract the sine and cosine of the frontAngle
        double xFront = Math.cos(frontAngel);
        double yFront = Math.sin(frontAngel);

        //find the ratio between the radius of the outer and inner circles, each 9 inches from the center of the robot
        double ratio = (radius - 9) / (radius + 9);

        int expRF = (int) Math.signum((yFront - xFront) * turnAngle) >>> 31;
        int expLF = (int) Math.signum((-yFront - xFront) * turnAngle) >>> 31;
        int expLB = (int) Math.signum((-yFront + xFront) * turnAngle) >>> 31;
        int expRB = (int) Math.signum((yFront + xFront) * turnAngle) >>> 31;

        double powerRF = power * Math.pow(ratio, expRF) * Math.signum(yFront - xFront);
        double powerLF = power * Math.pow(ratio, expLF) * Math.signum(-yFront - xFront);
        double powerLB = power * Math.pow(ratio, expLB) * Math.signum(-yFront + xFront);
        double powerRB = power * Math.pow(ratio, expRB) * Math.signum(yFront + xFront);

        //sets up the PID turn`
        Orientation ref = imu.getAngularOrientation();
        double heading = ref.firstAngle;
        double angleWanted = turnAngle + heading;
        double integral = 0;
        double previous_error = 0;
        double rcw = 1;

        //combines the tick condition with a gyroscopic sensor condition to ensure accuracy
        while (rcw !=0 && opMode.opModeIsActive()) {

            ref = imu.getAngularOrientation();

            double firstAngle = ref.firstAngle;
            if(ref.firstAngle<0&&turnAngle>0){
                firstAngle = 360 + ref.firstAngle;
            }
            if(ref.firstAngle>0&&turnAngle<0){
                firstAngle = -360+ ref.firstAngle;
            }

            double error = Math.abs(angleWanted) - Math.abs(firstAngle);

            double derivative = error - previous_error;
            //small margin of error for increased speed
            if (Math.abs(error) < HEADING_THRESHOLD) {
                error = 0;
            }
            //prevents integral from growing too large
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
            previous_error = error;
            integral=0;
            derivative=0;
            rcw = P_TURN_COEFF * error + I_TURN_COEFF*integral + D_TURN_COEFF * derivative;
            //sets the power, which, due to the exponents, is either the ratio or 1. User can change power factor for lower rcws.
            motorRF.setPower(-powerRF * rcw);
            motorLF.setPower(-powerLF * rcw);
            motorLB.setPower(-powerLB * rcw);
            motorRB.setPower(-powerRB * rcw);

            opMode.getTelemetry().addData("Path2", "Running at %7d :%7d",
                    motorLB.getCurrentPosition(),
                    motorLF.getCurrentPosition(),
                    motorRB.getCurrentPosition(),
                    motorRF.getCurrentPosition());
            opMode.getTelemetry().addData("target", "Running at %7d :%7d",
                    motorLB.getTargetPosition(),
                    motorLF.getTargetPosition(),
                    motorRB.getTargetPosition(),
                    motorRF.getTargetPosition());
            opMode.getTelemetry().addData("first angle", firstAngle);
            opMode.getTelemetry().addData("second angle", ref.secondAngle);
            opMode.getTelemetry().addData("third angle", ref.thirdAngle);
            opMode.getTelemetry().addData("target", turnAngle);
            opMode.getTelemetry().addData("error", angleWanted - ref.firstAngle);
            opMode.getTelemetry().addData("angleWanted", angleWanted);
            opMode.getTelemetry().addData("motor power", motorLF.getPower());
            opMode.getTelemetry().addData("rcw", rcw);
            opMode.getTelemetry().addData("P", P_TURN_COEFF * error);
            opMode.getTelemetry().addData("I", I_TURN_COEFF * integral);
            opMode.getTelemetry().addData("D", D_TURN_COEFF * derivative);
            opMode.getTelemetry().update();

            Thread.sleep(20);
        }
        halt();
        setModes(DcMotor.RunMode.RUN_USING_ENCODER);
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
        double heading = ret.thirdAngle;
        return heading;
    }

    public Orientation getRawHeadings() {
        return imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
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
        opMode.getTelemetry().addData("motorRF", motorRF);
        opMode.getTelemetry().addData("motorLF", motorLF);
        opMode.getTelemetry().addData("motorLB", motorLB);
        opMode.getTelemetry().addData("motorRB", motorRB);
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

    @Override
    public void setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior behavior) {
        motorRF.setZeroPowerBehavior(behavior);
        motorLF.setZeroPowerBehavior(behavior);
        motorLB.setZeroPowerBehavior(behavior);
        motorRB.setZeroPowerBehavior(behavior);
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

    /**
     * turns robot
     * @param angle positive input = counter-clockwise || negative input = clockwise
     */
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

            opMode.getTelemetry().addData("first angle", getHeading());
            opMode.getTelemetry().addData("speed ", rcw);
            opMode.getTelemetry().addData("x", angleWanted - getHeading());
            opMode.getTelemetry().addData("angleWanted", angleWanted);
            opMode.getTelemetry().addData("motor power", motorLF.getPower());
            opMode.getTelemetry().addData("rcw", rcw);
            opMode.getTelemetry().addData("P", P_TURN_COEFF * error);
            opMode.getTelemetry().addData("I", I_TURN_COEFF * integral);
            opMode.getTelemetry().addData("D", D_TURN_COEFF * derivative);
            opMode.getTelemetry().update();
        }
        accelerate(0);
    }

    @Override
    public void align(double angle) {
        align(angle, Side.FRONT);
    }

    @Override
    public void align(double angle, Side side) {
        //Turn  to global angle using PID
        // clockwise = negative input, counter-clockwise = positive input
        double angleWanted = angle - side.getDegrees();
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

            opMode.getTelemetry().addData("first angle", getHeading());
            opMode.getTelemetry().addData("speed ", rcw);
            opMode.getTelemetry().addData("error", angleWanted - getHeading());
            opMode.getTelemetry().addData("angleWanted", angleWanted);
            opMode.getTelemetry().addData("motor power", motorLF.getPower());
            opMode.getTelemetry().addData("rcw", rcw);
            opMode.getTelemetry().addData("P", P_TURN_COEFF * error);
            opMode.getTelemetry().addData("I", I_TURN_COEFF * integral);
            opMode.getTelemetry().addData("D", D_TURN_COEFF * derivative);
            opMode.getTelemetry().update();
        }
        accelerate(0);
    }

    public void accelerate(double speed) {
        double clip_speed = Range.clip(speed, -1, 1);
        motorLF.setPower(-clip_speed);
        motorRF.setPower(-clip_speed);
        motorRB.setPower(-clip_speed);
        motorLB.setPower(-clip_speed);
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
//            opMode.getTelemetry().addData("Motor Power",
//                    "%3d %3d %3d %3d",
//                    power * (y - x),
//                    -power * (-y - x),
//                    -power * (-y + x),
//                    power * (y + x)
//            );
            opMode.getTelemetry().addData("Raw Heading", getRawHeading());
            opMode.getTelemetry().addData("Heading", getHeading());
            opMode.getTelemetry().addData("Angle", angel);
            opMode.getTelemetry().addData("Path2", "Running at %7d :%7d",
                    motorLB.getCurrentPosition(),
                    motorLF.getCurrentPosition(),
                    motorRB.getCurrentPosition(),
                    motorRF.getCurrentPosition());
            opMode.getTelemetry().addData("target", "Running at %7d :%7d",
                    motorLB.getTargetPosition(),
                    motorLF.getTargetPosition(),
                    motorRB.getTargetPosition(),
                    motorRF.getTargetPosition());
            opMode.getTelemetry().addData("gyroHeading", imu.getAngularOrientation());
            dumpMotors();
            opMode.getTelemetry().update();
        }
        setPowers(0,0,0,0);
        setModes(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void startAngle(double angle, double power) {
        double angel = Math.toRadians(angle+180-getHeading());
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
    public List<Integer> getEncoders() {
        List<Integer> encoders = new ArrayList<>();
        encoders.add(motorRF.getCurrentPosition());
        encoders.add(motorLF.getCurrentPosition());
        encoders.add(motorLB.getCurrentPosition());
        encoders.add(motorRB.getCurrentPosition());
        return encoders;
    }
}
