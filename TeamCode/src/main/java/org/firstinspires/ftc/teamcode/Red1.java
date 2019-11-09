package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(group = "Auto", name = "Red1")
public class Red1 extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(hardwareMap, telemetry, this);
        bot.init();
        waitForStart();
        bot.driveTrain.goVector(0, 12, 1);
        bot.stop();
    }
}
