package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

/**
 * Created by khadija on 11/16/2019.
 */
@Autonomous(name = "1-stone-random", group = "Auto")
public class stone1rand extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        // init
        bot.init();
        // start
        waitForStart();
        // go forward with the flywheels facing the opposite side of the skybridge until it knocks the first stone away
        bot.driveTrain.goAngle(-24,90,0.5);
        //turn on the belt
        bot.belt.setPower(1);
        //turn on the flywheels
        bot.flywheels.setPower(-1);
        //move forward to intake the stone
        bot.driveTrain.goAngle(12,180,0.5);
        //stop the flywheels and belt
        bot.belt.setPower(0);
        bot.flywheels.setPower(0);
        //move backwards
        bot.driveTrain.goAngle(-12,-180,0.5);
        //move back left
        bot.driveTrain.goAngle(24,-90,0.5);
        //go past the skybridge
        bot.driveTrain.goAngle(48,180,0.5);
        bot.shuttleGate.setState(ShuttleGate.State.OPEN);
        bot.belt.setPower(1);
        bot.driveTrain.goAngle(12,-90,0.5);
        bot.belt.setPower(0);

    }
}
