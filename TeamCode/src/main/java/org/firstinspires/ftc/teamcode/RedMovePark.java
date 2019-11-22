package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(group = "Auto", name = "RedMovePark")
public class RedMovePark extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        // init
        bot.init();
        // start
        waitForStart();
        // move forward
        //bot.driveTrain.goVector(0, 12, .5);
        bot.driveTrain.goAngle(24,270,.5);
        // park
        bot.driveTrain.goAngle(12, 180, 0.5);
        // stop
        bot.stop();
    }
}
