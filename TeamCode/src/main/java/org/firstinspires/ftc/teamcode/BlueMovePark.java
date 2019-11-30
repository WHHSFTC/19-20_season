package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(group = "Auto", name = "BlueMovePark")
public class BlueMovePark extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        // init
        bot.init();
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);
        // start
        waitForStart();
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT);
        // move forward
        //bot.driveTrain.goVector(0, 12, .5);
        bot.driveTrain.goAngle(24,DriveTrain.RED_SIDE,0.5);
        // park
        bot.driveTrain.goAngle(12, DriveTrain.BUILDING_ZONE, 0.5);
        // stop
        bot.stop();
    }
}
