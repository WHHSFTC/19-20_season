package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(group = "Auto", name = "DistanceTest")
public class MovementTest extends LinearOpMode {

    private Sursum bot;

    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        // init
        bot.init(Sursum.Alliance.RED);
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);
        // start
        waitForStart();
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);
        // park
        bot.driveTrain.goAngle(48, DriveTrain.BUILDING_ZONE, 0.5);
        // stop
        bot.stop();
    }
}
