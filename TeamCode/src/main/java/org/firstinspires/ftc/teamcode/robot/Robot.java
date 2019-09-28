package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    public DriveTrain drive;
    public Intake intake;
    public Output output;
    public Sensors sensors;
    public Robot(HardwareMap hwmap) {
        drive = new DriveTrain(hwmap);
        intake = new Intake(hwmap);
        output = new Output(hwmap);
        //sensors = new Sensors(hwmap);
        
    }
}
