package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

import java.util.*;

@Autonomous(group = "Auto", name = "RedFoundation")
public class RedFoundation extends LinearOpMode {

    // constant for tile distance
    // length == width
    private static final double TILE = 24; // inches


    private Sursum bot;

    private List<Integer> encoder_values;

    private int original_value;
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
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);

        // waiting for start
        waitForStart();

        // heading over 2 tiles to get lined up with the center of the foundation
        // in reference to the middle of the bot
        bot.driveTrain.goAngle(35, DriveTrain.BUILDING_ZONE, 0.75);

        bot.driveTrain.goAngle(54-bot.ROBOT_LENGTH, DriveTrain.BLUE_SIDE, 0.25); // Hard coded distance

        // stopping all encoders and resetting to get the original value
      //bot.driveTrain.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

      //// getting encoder values
      //encoder_values = ((DriveTrain) bot.driveTrain).getEncoders();

      //// sorting encoder values
      //Collections.sort(encoder_values);

      //// using the 3 encoder as the benchmark for the original value
      //original_value = encoder_values.get(2);

      //// heading towards the foundation
      //// using distance sensor to judge when to stop
      //// waiting a set time between checks
      //bot.driveTrain.startAngle(270, .25);

      //while(bot.ods.getDistance(DistanceUnit.INCH) > 2.5) { Thread.sleep(50); }

      //// stopping all bot movement
      //bot.driveTrain.halt();
        Thread.sleep(1000);
        // setting foundation hooks to hook onto the foundation
        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);
        Thread.sleep(1000);

        bot.driveTrain.goAngle(60 - bot.ROBOT_LENGTH, DriveTrain.RED_SIDE, .25);
        // bot.driveTrain.goAngle(tile_distance(2), 90, .5); //Hard Coded Distance

      //// heading towards the wall
      //// using distance sensor to judge when to stop
      //// waiting a set time between checks
      //bot.driveTrain.startAngle(90, .25);

      //// using do-while loop to get encoders while driving to check if we hit the original position
      //do {
      //    Thread.sleep(50);
      //    encoder_values = ((DriveTrain) bot.driveTrain).getEncoders();
      //    Collections.sort(encoder_values);
      //} while(encoder_values.get(2) > original_value);

      //// stopping all bot movement
      //bot.driveTrain.halt();

        // releasing foundation
        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
        Thread.sleep(1000);

        // heading to park under bridge
        bot.driveTrain.goAngle(tile_distance(2) + 2, DriveTrain.LOADING_ZONE, 1);

        // completely stops all bot movement
        bot.stop();
    }
}
