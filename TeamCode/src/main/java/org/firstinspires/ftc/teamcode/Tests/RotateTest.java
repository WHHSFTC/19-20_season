package org.firstinspires.ftc.teamcode.Tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(name = "rotate test",group = "test")
public class RotateTest extends LinearOpMode {
    private Sursum bot;

    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        // init
        bot.init();
        // start
        bot.driveTrain.rotate(180);
        bot.driveTrain.halt();
        bot.driveTrain.rotate(90);
        bot.driveTrain.halt();
        bot.driveTrain.rotate(-180);
        bot.driveTrain.halt();
        bot.driveTrain.rotate(-90);
        bot.stop();
    }
}
