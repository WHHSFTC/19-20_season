package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

/**
 * Created by khadija on 10/19/2019.
 */
@Disabled
@Autonomous(name = "AutoTest1", group = "Tests")
public class AutoTestv1 extends LinearOpMode implements OpModeIF {
    private Bot robot = new Bot(this);
    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            robot.resetEncoder();
            robot.autoDriveStraight(1);
            robot.gyroTurn(45,.1);
            robot.encoderDrive(0.1,-5,-5,5);
            robot.encoderTurn(0.1,-45,5);
            robot.encoderDrive(0.1,-1,-1,5);
            robot.runTilLimit(1,true);
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
