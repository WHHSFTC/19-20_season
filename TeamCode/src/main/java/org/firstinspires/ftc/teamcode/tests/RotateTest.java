package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.implementations.Summum;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

@Disabled
@Autonomous(name = "rotate test",group = "test")
public class RotateTest extends LinearOpMode implements OpModeIF {
    private Summum bot;

    @Override
    public void runOpMode() {
        bot = new Summum(this);
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
