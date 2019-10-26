package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.Output.ClawPositionInner;
import org.firstinspires.ftc.teamcode.robot.Output.ClawPositionOuter;
import org.firstinspires.ftc.teamcode.robot.Output.ServoPosition;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp(group = "Tele", name = "TeleTest")
public class TeleTest extends LinearOpMode {double deadzone = .05;
    float xPow, yPow, zPow;
    Robot bot;
    @Override
    public void runOpMode() {
        bot = new Robot(hardwareMap);
        boolean in = false, out = false, pIn = false, pOut = false;
        waitForStart();
        while (opModeIsActive()) {
            yPow = -gamepad1.left_stick_y;
            xPow = gamepad1.left_stick_x;
            zPow = gamepad1.right_stick_x;

            xPow = Math.abs(xPow) < deadzone ? 0 : xPow;
            yPow = Math.abs(yPow) < deadzone ? 0 : yPow;

            double pwr;
            double m;

            double theta = Math.atan2(yPow, xPow);
            double power = Math.pow(Math.max(Math.abs(xPow), Math.abs(yPow)),2);
            double zPower = Math.pow(Math.abs(zPow),2);
            double x = Math.cos(theta);
            double y = Math.sin(theta);

            double z = Math.signum(zPow);

///         if (gamepad2.a) {bot.output.setWristPosition(ServoPosition.INNER);}
///         if (gamepad2.y) {bot.output.setWristPosition(ServoPosition.LEFT);}
///         if (gamepad2.b) {bot.output.setWristPosition(ServoPosition.RIGHT);}
///         if (gamepad2.x) {bot.output.setWristPosition(ServoPosition.OUTER);}

///         if (gamepad2.dpad_down) {bot.output.setArmPosition(ServoPosition.INNER);}
///         if (gamepad2.dpad_up) {bot.output.setArmPosition(ServoPosition.LEFT);}
///         if (gamepad2.dpad_right) {bot.output.setArmPosition(ServoPosition.RIGHT);}
///         if (gamepad2.dpad_left) {bot.output.setArmPosition(ServoPosition.OUTER);}

///         if (gamepad2.right_bumper && !pOut) {
///             out = !out;
///             bot.output.setOuterPosition(out ? ClawPositionOuter.CLOSE : ClawPositionOuter.OPEN);
///         }
///         if (gamepad2.left_bumper && !pIn) {
///             in = !in;
///             bot.output.setInnerPosition(in ? ClawPositionInner.CLOSE : ClawPositionInner.OPEN);
///         }
///         pIn = gamepad2.left_bumper;
///         pOut = gamepad2.right_bumper;

            if (gamepad1.a) {bot.intake.setServoPower(.5);}
            if (gamepad1.b) {bot.intake.setServoPower(0);}
            if (gamepad1.y) {bot.intake.setServoPower(-.5);}

            pwr = -gamepad2.right_stick_y;
            m = Math.max(Math.abs(pwr), 1.0);

            pwr /= m;

  //        bot.output.setSpoolPower(pwr);

            bot.drive.setDrivePowers(
                    power * (y-x) + zPower*z,
                    power * (y+x) + zPower*z,
                    power * (-y+x) + zPower*z,
                    power * (-y-x) + zPower*z
            );

        }
    }
}
