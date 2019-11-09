package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(name = "AutoTest2", group = "Tests")
public class visionTest extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() throws InterruptedException {

        bot = new Sursum(hardwareMap, telemetry, this);
        bot.init();
        waitForStart();
        while (opModeIsActive()) {

        }
    }
}
