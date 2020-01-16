package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.implementations.Auto;

@Disabled
@Autonomous(name = "Splines.java", group = "Test")
public class SplinesTest extends Auto {

    @Override
    public void run() {

        if (isStopRequested()) return;

        bot.driveTrain.followTrajectorySync(
                bot.driveTrain.trajectoryBuilder()
                .splineTo(
                        new Pose2d(
                                30,
                                0,
                                0
                        )
                )
                .build()
        );

        sleep(200);

        bot.driveTrain.followTrajectorySync(
                bot.driveTrain.trajectoryBuilder()
                .reverse()
                .splineTo(
                        new Pose2d(
                                0,
                                0,
                                0
                        )
                )
                .build()
        );
    }
}
