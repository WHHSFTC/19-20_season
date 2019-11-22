package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@TeleOp(name = "IMUTest", group = "Test")
public class IMUTest extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() throws InterruptedException {
        bot = new Sursum(this);
        bot.init();
        bot.driveTrain.setHeading(DriveTrain.RED_SIDE);
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT);
        telemetry.addData("raw", ((DriveTrain) bot.driveTrain).getRawHeading());
        telemetry.addData("heading", ((DriveTrain) bot.driveTrain).getHeading());
        telemetry.update();
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("raw", ((DriveTrain) bot.driveTrain).getRawHeading());
            telemetry.addData("heading", ((DriveTrain) bot.driveTrain).getHeading());
            telemetry.update();
        }
    }
}
