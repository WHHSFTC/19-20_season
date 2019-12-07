package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;


@Autonomous(group = "Auto", name = "BlueFoundation")
public class BlueFoundation extends LinearOpMode {
    private Sursum bot;

    @Override
    public void runOpMode() throws InterruptedException {
        // Creating Bot
        bot = new Sursum(this);

        // Starting up the Bot
        bot.init(Sursum.Alliance.BLUE);

        // Setting our global heading by changing offset
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);

        // waiting for start
        waitForStart();
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);

        bot.slowFoundation();

        // completely stops all bot movement
        bot.stop();
    }

}
