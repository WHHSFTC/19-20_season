package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(name = "SlowFoundation", group = "Auto")
public class SlowFoundation extends Auto {
    @Override
    public void run() {
        bot.driveTrain.align(bot.our_side);
        // heading over 2 tiles to get lined up with the center of the foundation
        // in reference to the middle of the bot
        bot.driveTrain.goAngle(12, DriveTrain.BUILDING_ZONE, 0.25);

        bot.driveTrain.align(bot.our_side);

        bot.driveTrain.goAngle(54- Sursum.ROBOT_LENGTH, bot.opponents_side, 0.25); // Hard coded distance
        sleep(1000);
        // setting foundation hooks to hook onto the foundation
        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);
        sleep(1000);

        bot.driveTrain.goAngle(60 - Sursum.ROBOT_LENGTH, bot.our_side, .25);
        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
        sleep(1000);

        // heading to park under bridge
        bot.driveTrain.goAngle(50, DriveTrain.LOADING_ZONE, .25);

        stop();
    }
}
