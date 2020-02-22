package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by khadija on 2/8/2020.
 */
@TeleOp(name = "Practice Omni", group = "Test")
public class PracticeTele extends OpMode {
    private DcMotor motorR, motorL, motorF, motorB;
    @Override
    public void init() {
        motorR = hardwareMap.dcMotor.get("r");
        motorL = hardwareMap.dcMotor.get("l");
        motorF = hardwareMap.dcMotor.get("f");
        motorB = hardwareMap.dcMotor.get("b");
        motorF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorF.setDirection(DcMotorSimple.Direction.REVERSE);
        motorL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorB.setDirection(DcMotorSimple.Direction.REVERSE);
        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void loop() {
        double x = gamepad1.left_stick_y;
        double y = gamepad1.left_stick_x;
        double z = gamepad1.right_stick_x;
        setPowers(
                y+z,
                -x+z,
                -y+z,
                x+z
        );
        telemetry.addData("x", x);
        telemetry.addData("y", y);
        telemetry.addData("z", z);
    }

    @Override
    public void stop() {
        super.stop();
        setPowers(0, 0, 0, 0);
    }

    private void setPowers(double f, double l, double b, double r) {
        double max = Math.max(1, Math.max(Math.max(Math.abs(f), Math.abs(l)), Math.max(Math.abs(b), Math.abs(r))));

        motorF.setPower(f/max);
        motorL.setPower(l/max);
        motorB.setPower(b/max);
        motorR.setPower(r/max);
    }
}
