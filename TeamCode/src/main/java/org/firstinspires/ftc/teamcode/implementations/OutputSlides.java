package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;

// drives slide motors using sensors and encoders
public class OutputSlides implements ContinuousMechanism {
    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    public OutputSlides(HardwareMap hwmap) {
        motor1 = hwmap.dcMotor.get("spool1");
        motor2 = hwmap.dcMotor.get("spool2");
        motor3 = hwmap.dcMotor.get("spool3");
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    @Override
    public void setState(Double value) throws IllegalArgumentException {
        motor1.setPower(value);
        motor2.setPower(value);
        motor3.setPower(value);
    }

    @Override
    public Double getState() {
        return motor1.getPower();
    }

    @Override
    public double getMax() {
        return 1;
    }

    @Override
    public double getMin() {
        return -1;
    }

    public dumpEncoders(Telemetry telemetry) {
        telemetry.addData("spool 1", motor1.getCurrentPosition());
        telemetry.addData("spool 2", motor2.getCurrentPosition());
        telemetry.addData("spool 3", motor3.getCurrentPosition());
    }
}
