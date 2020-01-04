package org.firstinspires.ftc.teamcode.implementations;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

@Config
public class RobotConstants {
    public static double SCALAR = 1.0;
    public static PIDCoefficients TURNING_PID = new PIDCoefficients(0.018, 0.01, 0.02);
}
