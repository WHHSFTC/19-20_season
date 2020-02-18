package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

import static java.lang.Thread.sleep;

// drives slide motors using sensors and encoders
public class OutputSlides implements ContinuousMechanism {
    private DcMotor motor;
    private OpModeIF opMode;
    public OutputSlides(OpModeIF opMode, String str) {
        motor = opMode.getHardwareMap().dcMotor.get(str);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.opMode = opMode;
    }
    @Override
    public void setState(Double value) throws IllegalArgumentException {
        motor.setPower(value);
    }

    @Override
    public Double getState() {
        return motor.getPower();
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
        opMode.getTelemetry().addData("spool", motor.getCurrentPosition());
    }
    public void stop() {
        setState(0.0);
    }

    public void runToZero() {
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(0);
        motor.setPower(0.5);
    }

    public void runToPosition(int position, double power) throws InterruptedException {
        motor.setTargetPosition(-position);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setState(power);
        Thread.sleep(500);
        setState(0.0);
    }
}
