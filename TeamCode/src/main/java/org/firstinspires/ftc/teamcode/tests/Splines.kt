package org.firstinspires.ftc.teamcode.tests

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.implementations.Auto

@Autonomous(name = "Splines.kt", group = "Test")
class Splines : Auto() {

    override fun run() {

        if (isStopRequested) return

        bot.driveTrain.followTrajectorySync(
                bot.driveTrain.trajectoryBuilder()
                        .splineTo(
                                pose = Pose2d(
                                        x = 30.0,
                                        y = 0.0,
                                        heading = 0.0
                                )
                        )
                        .build()
        )

        sleep(200)

        bot.driveTrain.followTrajectorySync(
                bot.driveTrain.trajectoryBuilder()
                        .reverse()
                        .splineTo(
                                pose = Pose2d(
                                        x = 0.0,
                                        y = 0.0,
                                        heading = 0.0
                                )
                        )
                        .build()
        )
    }
}