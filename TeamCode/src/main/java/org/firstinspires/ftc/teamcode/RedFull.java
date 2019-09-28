package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.Robot;

@Autonomous(name = "RedFull", group = "Red")
public class RedFull extends Processor {
    Robot bot;
    public void runOpMode() {
        bot = new Robot(hardwareMap);
        // auto-specific init here
        waitForStart();
        // autonomous code here
    }
}
