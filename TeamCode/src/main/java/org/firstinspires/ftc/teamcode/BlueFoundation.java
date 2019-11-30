package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

import java.util.List;

@Autonomous(group = "Auto", name = "BlueFoundation")
public class BlueFoundation extends LinearOpMode {

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
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT);

        // heading over 2 tiles to get lined up with the center of the foundation
        // in reference to the middle of the bot
        bot.driveTrain.goAngle(12, DriveTrain.BUILDING_ZONE, 0.75);

        bot.driveTrain.goAngle(54-bot.ROBOT_LENGTH, DriveTrain.RED_SIDE, 0.25); // Hard coded distance

        Thread.sleep(1000);
        // setting foundation hooks to hook onto the foundation
        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);
        Thread.sleep(1000);

        bot.driveTrain.goAngle(60 - bot.ROBOT_LENGTH, DriveTrain.BLUE_SIDE, .25);
        // releasing foundation
        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
        Thread.sleep(1000);

        // heading to park under bridge
        bot.driveTrain.goAngle(50, DriveTrain.LOADING_ZONE, 1);

        // completely stops all bot movement
        bot.stop();
    }
}
