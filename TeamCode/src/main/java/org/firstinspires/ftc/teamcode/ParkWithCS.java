package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

import java.util.*;
@Disabled
@Autonomous(name = "ParkWithCS", group = "Test")
public class ParkWithCS extends LinearOpMode {
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
        int first_sum = 0;
        int second_sum = 0;
        for (int i = 0; i < first_compare.length; i++) {
            first_sum += first_compare[i];
            second_sum += second_compare[i];
        }
        return Math.abs(first_sum-second_sum);
    }

    private ElapsedTime run_time;
    private Sursum bot;
    private static final int WINDOW = 10;

    @Override
    public void runOpMode() throws InterruptedException {

        run_time = new ElapsedTime();

        bot = new Sursum(this);

        bot.init();

        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("value", Arrays.toString(get_color_sensor_value(bot.color_sensor_bottom)));
            telemetry.update();
//            ((DriveTrain) bot.driveTrain).setPowers(.25, -.25, -.25, .25);
            bot.driveTrain.startAngle(DriveTrain.BUILDING_ZONE, .25);
            // (0, 52, 190) blue tape
            if (compare_colors(get_color_sensor_value(bot.color_sensor_bottom), new int[]{255, 51, 51}) <= WINDOW) {
                telemetry.addLine("FOUND LINE");
            }
            telemetry.update();
        }
        bot.driveTrain.halt();

        bot.stop();
    }
}
