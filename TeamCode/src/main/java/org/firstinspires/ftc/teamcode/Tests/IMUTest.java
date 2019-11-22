package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

public class IMUTest extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() throws InterruptedException {
        bot = new Sursum(this);
        bot.init();
        bot.driveTrain.setHeading(DriveTrain.RED_SIDE);
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
