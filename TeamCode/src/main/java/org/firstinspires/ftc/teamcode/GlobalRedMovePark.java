package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
@Disabled
@Autonomous(group = "Auto", name = "GlobalRedMovePark")
public class GlobalRedMovePark extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        // init
        bot.init();
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);
        // start
        waitForStart();
        // move forward
        bot.driveTrain.goAngle(24, DriveTrain.BLUE_SIDE,.5);
        // park
        bot.driveTrain.goAngle(12, DriveTrain.BUILDING_ZONE, .5);

        // stop
        bot.stop();
    }
}