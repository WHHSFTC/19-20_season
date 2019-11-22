package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;

// drives slide motors using sensors and encoders
public class OutputSlides implements ContinuousMechanism {
    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    private LinearOpMode opMode;
    public OutputSlides(LinearOpMode opMode, String str1, String str2, String str3) {
        motor1 = opMode.hardwareMap.dcMotor.get(str1);
        motor2 = opMode.hardwareMap.dcMotor.get(str2);
        motor3 = opMode.hardwareMap.dcMotor.get(str3);
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setDirection(DcMotorSimple.Direction.REVERSE);
        this.opMode = opMode;
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

    public void dumpEncoders() {
        opMode.telemetry.addData("spool 1", motor1.getPower());
        opMode.telemetry.addData("spool 2", motor2.getPower());
        opMode.telemetry.addData("spool 3", motor3.getPower());
    }
    public void stop() {
        setState(0.0);
    }
}
