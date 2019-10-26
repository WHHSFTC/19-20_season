package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.lynx.LynxDigitalChannelController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Intake {
    public CRServo flyL, flyR;
    public Servo skyArm;
    public DcMotor belt;
    public DistanceSensor limit;

    Intake(HardwareMap hwmap) {
        flyL = hwmap.crservo.get("left");
        flyR = hwmap.crservo.get("right");
       // skyArm = hwmap.servo.get("skyarm");
        flyR.setDirection(DcMotorSimple.Direction.REVERSE);
        //belt = hwmap.dcMotor.get("belt");
       // limit = hwmap.get(DistanceSensor.class,"limit");
    }
    public void setServoPower(double power) {
        flyL.setPower(power);
        flyR.setPower(power);
    }
    public void setBeltPower(double power) {
        belt.setPower(power);
    }
    public boolean getLimit() {
        return limit.getDistance(DistanceUnit.INCH) == 0;
    }
    // sets power and waits until limit matches desired
    public void runTilLimit(double power, boolean desired) {
        belt.setPower(power);
        while(getLimit() != desired) {}
        belt.setPower(0);
    }
}