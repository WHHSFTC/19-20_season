package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;

public class DriveTrain implements StrafingDriveTrain {
    private static final double MECANUM_WHEEL_CIRCUMFERENCE = 12.368475;
    private DcMotor motorRF;
    private DcMotor motorLF;
    private DcMotor motorLB;
    private DcMotor motorRB;
    private Telemetry telemetry;
    private OpModeIF opMode;
    private BNO055IMU imu;

    static final double P_TURN_COEFF = .018;
    static final double I_TURN_COEFF = 0.01;
    static final double D_TURN_COEFF = 0.026;
    static final double HEADING_THRESHOLD = 5;
    static final double ANTI_WINDUP = 2;
    static final double TICKSPERROTATION = 537.6;

    public DriveTrain(HardwareMap hwmap, Telemetry telemetry, OpModeIF opMode) {
        motorRF = hwmap.dcMotor.get("motorRF");
        motorLF = hwmap.dcMotor.get("motorLF");
        motorLB = hwmap.dcMotor.get("motorLB");
        motorRB = hwmap.dcMotor.get("motorRB");
        this.telemetry = telemetry;
        this.opMode = opMode;
        imu = hwmap.get(BNO055IMU.class, "imu");
        initImu();
    }

    @Override
    public void goArc(double centerX, double centerY, double angle, double power) {
        // not implemented
    }

    public void initImu(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(parameters);
    }
    public double getHeading(){
        Orientation ret = imu.getAngularOrientation();
        double heading = ret.firstAngle;
        return heading;
    }
    // strafes at cartesian vector
    @Override
    public void goVector(double x, double y, double power) {
        goAngle(Math.sqrt(x*x + y*y), Math.atan2(y, x), power);
    }

    @Override
    public void dumpMotors() {

    }

    public void setPowers(double rf, double lf, double lb, double rb) {
        motorRF.setPower(rf);
        motorLF.setPower(lf);
        motorLB.setPower(lb);
        motorRB.setPower(rb);
    }

    public void resetEnc() {
        motorRF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void enterEnc() {
        motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void enterPosenc() {
        motorLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorLB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void stop() {
        motorRF.setPower(0);
        motorLF.setPower(0);
        motorLB.setPower(0);
        motorRB.setPower(0);
    }


    // absolute
    public void rotate(double angleWanted) {
        //Turn using PID
        // clockwise = negative input, counter-clockwise = positive input
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

            telemetry.addData("first angle", getHeading());
            //telemetry.addData("second angle", ref.secondAngle);
            //telemetry.addData("third angle", ref.thirdAngle);
            telemetry.addData("speed ", rcw);
            telemetry.addData("error", angleWanted - getHeading());
            telemetry.addData("angleWanted", angleWanted);
            telemetry.addData("motor power", motorLF.getPower());
            telemetry.addData("rcw", rcw);
            telemetry.addData("P", P_TURN_COEFF * error);
            telemetry.addData("I", I_TURN_COEFF * integral);
            telemetry.addData("D", D_TURN_COEFF * derivative);
            telemetry.update();

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

    public void goAngle(double dist, double angle, double power) {
        resetEnc();
        enterPosenc();
        angle = angle-180-getHeading();
        double angel = Math.toRadians(angle);
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
        while (( motorLB.isBusy() &&
                motorRB.isBusy() &&
                motorRF.isBusy() &&
                motorLF.isBusy()) &&
                opMode.opModeIsActive()) {
            telemetry.addData("Path2", "Running at %7d :%7d",
                    motorLB.getCurrentPosition(),
                    motorLF.getCurrentPosition(),
                    motorRB.getCurrentPosition(),
                    motorRF.getCurrentPosition());
            telemetry.addData("target", "Running at %7d :%7d",
                    motorLB.getTargetPosition(),
                    motorLF.getTargetPosition(),
                    motorRB.getTargetPosition(),
                    motorRF.getTargetPosition());
            telemetry.addData("gyroHeading", imu.getAngularOrientation());
            telemetry.update();
        }
        setPowers(0,0,0,0);
        enterEnc();
    }
}
