package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by khadija on 10/19/2019.
 */
@Autonomous(name = "Real Auto1", group = "Auto1")
public class realTestAuto extends LinearOpMode {
    private Bot robot = new Bot(this);

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            robot.resetEncoder();
            robot.autoDriveStraight(20);




        }
    }
}
