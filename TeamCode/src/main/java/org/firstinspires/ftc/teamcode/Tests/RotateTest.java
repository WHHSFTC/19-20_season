package org.firstinspires.ftc.teamcode.Tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

@Disabled
@Autonomous(name = "rotate test",group = "test")
public class RotateTest extends LinearOpMode implements OpModeIF {
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
    @Override
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    @Override
    public Telemetry getTelemetry() {
        return telemetry;
    }
}
