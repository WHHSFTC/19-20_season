package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    public CRServo flyL, flyR;
    public Servo skyArm;
    Intake(HardwareMap hwmap) {
        flyL = hwmap.crservo.get("left");
        flyR = hwmap.crservo.get("right");
        skyArm = hwmap.servo.get("skyarm");
        flyR.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void setServoPower(double power) {
        flyL.setPower(power);
        flyR.setPower(power);
    }
}