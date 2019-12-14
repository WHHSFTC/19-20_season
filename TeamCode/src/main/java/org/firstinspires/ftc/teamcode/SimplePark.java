package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;

@Autonomous(group = "Auto", name = "SimplePark")
public class SimplePark extends Auto {
    @Override
    public void run() {
        // park
        bot.driveTrain.goAngle(12, DriveTrain.BUILDING_ZONE, 0.5);
    }
}
