package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(group = "Auto", name = "SimplePark")
public class SimplePark extends Auto {
    @Override
    public void start() {
        super.start();
        // park
        bot.driveTrain.goAngle(12, DriveTrain.BUILDING_ZONE, 0.5);

        stop();
    }
}
