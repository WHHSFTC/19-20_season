package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotProcessor.RobotProcessor;
import org.firstinspires.ftc.teamcode.robot.Robot;

public class BetterTest extends LinearOpMode {
    Robot robot;
    RobotProcessor proc;
    public void runOpMode() {
        robot = new Robot(hardwareMap);
        proc = new RobotProcessor(this);



    }
}
