package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(group = "Auto", name = "Red1")
public class Red1 extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        // init
        bot.init();
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);
        telemetry.addData("heading", ((DriveTrain) bot.driveTrain).getHeading());
        telemetry.update();
        // start
        waitForStart();
        telemetry.addData("heading", ((DriveTrain) bot.driveTrain).getHeading());
        telemetry.update();
        // park
        bot.driveTrain.goVector(0, 12, .5);
        // stop
        bot.stop();
    }
}
