package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(group = "Auto", name = "RedFoundation")
public class RedFoundation extends LinearOpMode {

    // constant for tile distance
    // length == width
    private static final double TILE = 24; // inches


    private Sursum bot;

    /**
     * get the distance in inches based on how many tiles are put into param
     * @param number_of_tiles is the number of tiles that the distance is needed for
     * @return total distance of the number of tiles
     */
    public static double tile_distance(int number_of_tiles) {return TILE*number_of_tiles;}

    @Override
    public void runOpMode() throws InterruptedException{
        // Creating Bot
        bot = new Sursum(this);

        // Starting up the Bot
        bot.init();

        // Setting our global heading by changing offset
        bot.driveTrain.setHeading(DriveTrain.RED_SIDE);

        // waiting for start
        waitForStart();

        // heading over 2 tiles to get lined up with the center of the foundation
        // in reference to the middle of the bot
        bot.driveTrain.goAngle(tile_distance(1) + bot.ROBOT_WIDTH, 180, 1);

        // bot.driveTrain.goAngle(48-bot.ROBOT_LENGTH, 270, 1); Hard Coded Distance

        // heading towards the foundation
        // using distance sensor to judge when to stop
        // waiting a set time between checks
        bot.driveTrain.startAngle(270, .25);
        while(bot.ods_back.getDistance(DistanceUnit.INCH) > 2.5) { Thread.sleep(50); }

        // stopping all bot movement
        bot.driveTrain.halt();

        // setting foundation hooks to hook onto the foundation
        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);

        // bot.driveTrain.goAngle(tile_distance(2), 90, .5); //Hard Coded Distance

        // heading towards the wall
        // using distance sensor to judge when to stop
        // waiting a set time between checks
        bot.driveTrain.startAngle(90, .25);
        while(bot.ods_front.getDistance(DistanceUnit.INCH) > 5.1) { Thread.sleep(50); }

        // stopping all bot movement
        bot.driveTrain.halt();

        // releasing foundation
        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);

        // heading to park under bridge
        bot.driveTrain.goAngle(tile_distance(2), 0, 1);

        // completely stops all bot movement
        bot.stop();
    }
}
