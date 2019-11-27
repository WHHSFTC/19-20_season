package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.Sursum;


@Autonomous(group = "Auto", name = "SideArmSkyStoneAutoRed")
public class SideArmSkyStoneAutoRed extends LinearOpMode {

    private static final double TILE = 24; // inches
    /**
     * get the distance in inches based on how many tiles are put into param
     * @param number_of_tiles is the number of tiles that the distance is needed for
     * @return total distance of the number of tiles
     */
    public static double tile_distance(double number_of_tiles) {return TILE*number_of_tiles;}
    public static int[] get_color_sensor_value(ColorSensor color) {
        return new int[] {color.red(), color.green(), color.blue()};
    }

    /**
     * compares two int arrays and gives the overall difference between the sum of all values in both
     * @param first_compare the first array of ints of RGB values to compare with the second
     * @param second_compare the second array of ints of RGB values to compare with the first
     * @return the absolute value of the difference between the values of the first array and second array
     */
    public static int compare_colors(int[] first_compare, int[] second_compare) {
        int compare = 0;
        for (int i = 0; i < first_compare.length; i++) {
            compare = Math.abs(first_compare[i]-second_compare[i]);
        }
        return compare;
    }
    private ElapsedTime run_time;
    private Sursum bot;
    @Override
    public void runOpMode() throws InterruptedException{
        // creating the bot
        bot = new Sursum(this);

        // initializing all bot functions
        bot.redInit();

        // sets global heading of the bot
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);

        run_time = new ElapsedTime();
        // waiting for start button to be pressed
        waitForStart();
        run_time.reset();

        // heading towards block setup
        bot.driveTrain.goAngle(tile_distance(1.5), DriveTrain.LOADING_ZONE, .5);

        bot.driveTrain.goAngle(tile_distance(.5), DriveTrain.BLUE_SIDE, .5);

        bot.driveTrain.rotate(90);

        bot.rightArm.setArmPosition(SideArm.Arm.State.DOWN);

        Thread.sleep(1000);

        bot.rightArm.setClawPosition(SideArm.Claw.State.OPEN);

        Thread.sleep(1000);

        bot.driveTrain.goAngle(tile_distance(.5), DriveTrain.BLUE_SIDE, .5);

        Thread.sleep(1000);

        bot.rightArm.setClawPosition(SideArm.Claw.State.CLOSED);

        Thread.sleep(1500);

        bot.rightArm.arm.setPosition(.4);

        bot.driveTrain.goAngle(tile_distance(1), DriveTrain.RED_SIDE, .5);

        bot.driveTrain.goAngle(tile_distance(3), DriveTrain.BUILDING_ZONE, .5);

        bot.driveTrain.goAngle(tile_distance(1), DriveTrain.BLUE_SIDE, .5);

        bot.rightArm.setArmPosition(SideArm.Arm.State.DOWN);

        bot.rightArm.setClawPosition(SideArm.Claw.State.OPEN);

        Thread.sleep(1500);

        bot.driveTrain.goAngle(tile_distance(1), DriveTrain.RED_SIDE, .5);

        bot.driveTrain.startAngle(DriveTrain.LOADING_ZONE, .5);

        run_time.reset();
        while (run_time.seconds() <= 15.0){
            if (compare_colors(get_color_sensor_value(bot.color_sensor), new int[] {255, 51, 51}) <= 10) {
                Thread.sleep(100);
                break;
            }
            Thread.sleep(50);
        }

        bot.driveTrain.halt();

        bot.stop();
    }
}
