package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class Robot extends LinearOpMode {
    DcMotor motorLF, motorLB, motorRB, motorRF, spool1, spool2, spool3, spool4;
    Servo arm, wrist, inner, outer;
    CRServo flyL, flyR;
    public void runOpMode() {
        motorLF = hardwareMap.get(DcMotor.class, "motorLF");
        motorLB = hardwareMap.get(DcMotor.class, "motorLB");
        motorRB = hardwareMap.get(DcMotor.class, "motorRB");
        motorRF = hardwareMap.get(DcMotor.class, "motorRF");

        arm = hardwareMap.get(Servo.class, "arm");
        wrist = hardwareMap.get(Servo.class, "wrist");
        inner = hardwareMap.get(Servo.class, "inner");
        outer = hardwareMap.get(Servo.class, "outer");

        flyL = hardwareMap.get(CRServo.class, "left");
        flyR = hardwareMap.get(CRServo.class, "right");

        spool1 = hardwareMap.get(DcMotor.class, "1");
        spool2 = hardwareMap.get(DcMotor.class, "2");
        spool3 = hardwareMap.get(DcMotor.class, "3");
        spool4 = hardwareMap.get(DcMotor.class, "4");
    }
    void setDrivePowers(double lb, double lf, double rf, double rb) {
        motorLB.setPower(lb);
        motorLF.setPower(lf);
        motorRF.setPower(rf);
        motorRB.setPower(rb);
    }
    void setSpoolPower(double power) {
        spool1.setPower(power);
        spool2.setPower(power);
        spool3.setPower(power);
        spool4.setPower(power);
    }
    void setArmPosition(Position position) {
        arm.setPosition(position.pos);
    }
    void setWristPosition(Position position) {
        wrist.setPosition(position.pos);
    }
}

enum Position {
    OUTER(1), LEFT(0.66), INNER(0.33), RIGHT(0);
    double pos;
    Position(double pos) {
        this.pos = pos;
    }
}
