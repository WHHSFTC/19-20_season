package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

import static java.lang.Thread.sleep;

// drives slide motors using sensors and encoders
public class OutputSlides implements ContinuousMechanism {
    private DcMotor motor1;
    private DcMotor motor2;
    private OpModeIF opMode;

    public OutputSlides(OpModeIF opMode, String str1, String str2) {
        motor1 = opMode.getHardwareMap().dcMotor.get(str1);
        motor2 = opMode.getHardwareMap().dcMotor.get(str2);
        this.opMode = opMode;
        initMotors();
    }

    private void initMotors() {
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);

        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void setState(Double value) throws IllegalArgumentException {
        motor1.setPower(value);
        motor2.setPower(value);
    }

    @Override
    public Double getState() {
        return (motor1.getPower() + motor2.getPower()) / 2.0;
    }

    @Override
    public double getMax() {
        return 1;
    }

    @Override
    public double getMin() {
        return -1;
    }

    public void dumpEncoders() {
        opMode.getTelemetry().addData("spool1", motor1.getCurrentPosition());
        opMode.getTelemetry().addData("spool2", motor2.getCurrentPosition());
    }
    public void stop() {
        setState(0.0);
    }

    public void runToZero() {
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor1.setTargetPosition(0);
        motor1.setPower(0.5);

        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setTargetPosition(0);
        motor2.setPower(0.5);
    }

    public void runToPosition(int position, double power) throws InterruptedException {
        motor1.setTargetPosition(-position);
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setTargetPosition(-position);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setState(power);
        Thread.sleep(500);
        setState(0.0);
    }
}
