package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Output {
    public Servo arm, wrist, inner, outer;
    public DcMotor spool1, spool2, spool3, spool4;
    Output(HardwareMap hwmap) {
      //arm = hwmap.servo.get("arm");
      //wrist = hwmap.servo.get("wrist");
      //inner = hwmap.servo.get("inner");
      //outer = hwmap.servo.get("outer");

      //spool1 = hwmap.dcMotor.get("spool1");
      //spool2 = hwmap.dcMotor.get("spool2");
      //spool3 = hwmap.dcMotor.get("spool3");
      //spool4 = hwmap.dcMotor.get("spool4");
    }
    public void setSpoolPower(double power) {
        spool1.setPower(power);
        spool2.setPower(power);
        spool3.setPower(power);
        spool4.setPower(power);
    }
    public void setArmPosition(ServoPosition position) {
        arm.setPosition(position.pos);
    }
    public void setWristPosition(ServoPosition position) {
        wrist.setPosition(position.pos);
    }
    public void setInnerPosition(ClawPositionInner pos) {
        inner.setPosition(pos.pos);
    }
    public void setOuterPosition(ClawPositionOuter pos) {
        outer.setPosition(pos.pos);
    }
    public enum ServoPosition {
        OUTER(1), LEFT(0.66), INNER(0.33), RIGHT(0);
        double pos;
        ServoPosition(double pos) {
            this.pos = pos;
        }
    }
    public enum ClawPositionInner {
        OPEN(0), CLOSE(.5);
        double pos;
        ClawPositionInner(double pos) {this.pos = pos;}
    }
    public enum ClawPositionOuter {
        OPEN(.5), CLOSE(0);
        double pos;
        ClawPositionOuter(double pos) {this.pos = pos;}
    }
}

