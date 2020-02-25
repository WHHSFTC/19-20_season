package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;

@Autonomous(group = "Auto", name = "MovePark")
public class MovePark extends Auto {
    @Override
    public void run() {
        // move forward
        bot.driveTrain.goAngle(24, bot.opponents_side,0.5);
        // park
        bot.driveTrain.goAngle(12, DriveTrain.BUILDING_ZONE, 0.5);
    }
}
