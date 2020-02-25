package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Summum;
@Disabled
@Autonomous(name = "FoundArc: Park by the wall", group = "Auto")
public class FoundArcJava extends Auto {
    @Override
    public void run() throws InterruptedException {
        bot.driveTrain.align(bot.our_side);

        // heading over 2 tiles to get lined up with the center of the foundation
        // in reference to the middle of the bot
        bot.driveTrain.goAngle(12, DriveTrain.BUILDING_ZONE, 0.25);

        bot.driveTrain.align(bot.our_side);

        bot.driveTrain.goAngle(54- Summum.ROBOT_LENGTH, bot.opponents_side, 0.25); // Hard coded distance

        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);

        switch(bot.alliance) {
            case BLUE:
                bot.driveTrain.goArc(15, 90, 90, 0.5, 5);
                break;
            case RED:
                bot.driveTrain.goArc(15, 90, -90, 0.5, 5);
        }

        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);

        bot.driveTrain.goAngle(24, DriveTrain.BUILDING_ZONE, .5);

        bot.driveTrain.goAngle(24, bot.our_side, .5);

        bot.driveTrain.goAngle(48, DriveTrain.LOADING_ZONE, .5);

        stop();
    }
}
