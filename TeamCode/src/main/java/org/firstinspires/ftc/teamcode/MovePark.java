package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(group = "Auto", name = "MovePark")
public class MovePark extends Auto {
    private Sursum bot;
    @Override
    public void start() {
        // move forward
        bot.driveTrain.goAngle(24, bot.opponents_side,0.5);
        // park
        bot.driveTrain.goAngle(12, DriveTrain.BUILDING_ZONE, 0.5);
        // stop
        bot.stop();
    }
}
