package org.firstinspires.ftc.teamcode.RobotProcessor;

import org.firstinspires.ftc.teamcode.Processor;
import org.firstinspires.ftc.teamcode.robot.Robot;

/**
 * Created by khadija on 9/28/2019.
 * Fixed by luke on 10/25/19.
 */
public class RobotProcessor {
    private Robot robot;
    public DriveTrainProcessor drive;
    public IntakeProcessor intake;
    public OutputProcessor output;
    public SensorProcessor sensor;
    public RobotProcessor(Robot robot) {
        this.robot = robot;
    }
}
