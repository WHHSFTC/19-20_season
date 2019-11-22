package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

/**
 * Created by khadija on 11/16/2019.
 */
public class Distancesensortest extends LinearOpMode {
    private Sursum bot;
    public double distance;

    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        bot.init();
        waitForStart();
        while (opModeIsActive()) {
            distance = bot.ods.getDistance(DistanceUnit.INCH);
            telemetry.addData("The distance in Inches is: ", distance);

        }
    }
}
