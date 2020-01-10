package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

import java.util.List;

@Disabled
@TeleOp(name = "ServoTest", group = "Test")
public class ServoTest extends LinearOpMode implements OpModeIF {
    @Override
    public void runOpMode() throws InterruptedException {
        List<Servo> servos;
        int i;
        Servo servo;
        boolean stick = false;
        boolean prevInc = false;
        boolean prevDec = false;
        boolean prevStick = false;
        double offset = 0;
        double precision = 0.05;
        double a = 0;
        double b = 0.5;
        double y = 1;
        servos = hardwareMap.getAll(Servo.class);
        i = 0;
        waitForStart();
        while (opModeIsActive()) {
            servo = servos.get(i);
            telemetry.addData("Stick", stick);
            telemetry.addData("Offset", offset);
            telemetry.addData("Precision", precision);
            telemetry.addData("Controller", servo.getController().getDeviceName());
            telemetry.addData("Port", servo.getPortNumber());
            telemetry.addData("Names", hardwareMap.getNamesOf(servo));
            telemetry.addData("Position", servo.getPosition());
            telemetry.addData("a", a);
            telemetry.addData("b", b);
            telemetry.addData("y", y);
            telemetry.update();
            if(gamepad1.right_bumper && !prevInc && i < servos.size() - 1) i++;
            prevInc = gamepad1.right_bumper;
            if(gamepad1.left_bumper && !prevDec && i > 0) i--;
            prevDec = gamepad1.left_bumper;
            if(gamepad1.left_trigger > 0.5) {
                if(gamepad1.a) a = servo.getPosition();
                if(gamepad1.b) b = servo.getPosition();
                if(gamepad1.y) y = servo.getPosition();
            } else if (gamepad1.right_trigger > 0.5) {
                if(gamepad1.a) servo.setPosition(a);
                if(gamepad1.b) servo.setPosition(b);
                if(gamepad1.y) servo.setPosition(y);
            } else {
                if(gamepad1.x) offset = servo.getPosition();
                if(gamepad1.b && !prevStick) stick = !stick;
                prevStick = gamepad1.b;
            }
            if(gamepad1.dpad_left) precision = 0.05;
            if(gamepad1.dpad_right) precision = 0.01;
            if(stick) {
                servo.setPosition(gamepad1.left_stick_y * precision + offset);
            }
            else {
                if(gamepad1.dpad_up && servo.getPosition() <= 1 - precision) {
                    servo.setPosition(servo.getPosition() + precision);
                    Thread.sleep(200);
                }
                if(gamepad1.dpad_down && servo.getPosition() >= precision) {
                    servo.setPosition(servo.getPosition() - precision);
                    Thread.sleep(200);
                }
            }
        }
    }
    @Override
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    @Override
    public Telemetry getTelemetry() {
        return telemetry;
    }
}
