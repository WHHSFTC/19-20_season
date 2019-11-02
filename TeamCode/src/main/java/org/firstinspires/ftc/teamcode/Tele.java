package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.implementations.Sursum;

@TeleOp(group = "Tele", name = "Tele")
public class Tele extends LinearOpMode {
    private static final double DEADZONE = 0.05;
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(hardwareMap);
        bot.init();
        waitForStart();
        while (opModeIsActive()) {
            driveDriveTrain();
            driveInput();
            driveOutput();
            bot.driveTrain.dumpMotors();
            driveOutput();
        }
        bot.stop();
    }

    // drives the drivetrain
    private void driveDriveTrain() {
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        // ternaries for dead-zone logic
        x = Math.abs(x) > DEADZONE ? x : 0;
        y = Math.abs(y) > DEADZONE ? y : 0;
        turn = Math.abs(turn) > DEADZONE ? turn : 0;
        // todo logarithmic scale and drive powers
    }

    // drives the output
    private void driveOutput() {

    }

    // drives the input
    private void driveInput() {

    }
}
