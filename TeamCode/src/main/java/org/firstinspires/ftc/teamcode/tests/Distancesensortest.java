package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

/**
 * Created by khadija on 11/16/2019.
 */
@Disabled
public class Distancesensortest extends LinearOpMode implements OpModeIF {
    private Sursum bot;
    public double distance;

    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        bot.init();
        waitForStart();
        while (opModeIsActive()) {
//            distance = bot.ods.getDistance(DistanceUnit.INCH);
            telemetry.addData("The distance in Inches is: ", distance);

        }
    }
    @Override
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    @Override
    public Telemetry getTelemetry() {
        return telemetry;
    }
}
