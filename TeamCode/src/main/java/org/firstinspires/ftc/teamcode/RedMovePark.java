package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.Sursum;

/**
 * Created by khadija on 11/10/2019.
 */
@Autonomous(name = "RedMovePark", group = "Auto")
public class RedMovePark extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        // init
        bot.init();
        // start
        waitForStart();
        //Move forward
        bot.driveTrain.goAngle(-24,90,.5);
        // park
        bot.driveTrain.goVector(12, 0, .5);
        // stop
        bot.stop();
    }
}
