package org.firstinspires.ftc.teamcode

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.implementations.*
@Disabled
class SkyStoneSpline : Auto() {

    fun intakeStone() {
        // turn so sidearm faces the stones
        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        val lineUpSideArm: Trajectory = bot.driveTrain.trajectoryBuilder()
                .splineTo(
                        pose = Pose2d(
                                x = 0.0,
                                y = 0.0,
                                heading = 0.0
                        )
                )
                .build()

        val lineUpWithStone: Trajectory = bot.driveTrain.trajectoryBuilder()
                .splineTo(
                        pose = Pose2d(
                                x = 0.0,
                                y = 0.0,
                                heading = 0.0
                        )
                )
                .build()

        // lines up sidearm
        bot.driveTrain.followTrajectorySync(lineUpSideArm)

        // moves forward to be in line with stone
        bot.driveTrain.followTrajectorySync(lineUpWithStone)

        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        sleep(250)
    }

    override fun halt() {
        super.halt()
        bot.visionTF.shutdown()
    }

    /**
     * POSITIVE Y IS LEFT
     * POSITIVE X IS UP
     */
    override fun run() {
        // servo calss
        bot.sideArm.arm.state = SideArm.Arm.State.DOWN
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        // TODO(reason = "FINISH ALL POSE2D FOR ALLAINCE SPECIFIC SIDES")
        val distance = 41 - Sursum.ROBOT_LENGTH
        val redPose: Pose2d = Pose2d(
                x = 0.0,
                y = 0.0,
                heading = 0.0
        )

        val bluePose: Pose2d = Pose2d(
                x = 0.0,
                y = 0.0,
                heading = 0.0
        )


        val path: Trajectory =
                bot.driveTrain.trajectoryBuilder()
                        .splineTo(
                                pose = if (bot.alliance == Alliance.RED) redPose else bluePose
                        )
                        .build()

        // preparing arm to grab
        bot.driveTrain.followTrajectorySync(path)

        bot.opMode.telemetry.addLine("Starting TensoFlow Search")
        bot.opMode.telemetry.update()

        val skyStonePosition: SkyStonePosition?

        try {
            skyStonePosition = bot.findSkystone()
        } catch (ex: InterruptedException) {
            bot.opMode.telemetry.addLine(ex.message)
            bot.opMode.telemetry.update()
            requestOpModeStop()
            return
        }

        // intaking the skystone
        intakeStone()

        val underSkybridgePath: Trajectory = bot.driveTrain.trajectoryBuilder()
                .splineTo(
                        pose = Pose2d(
                                x = 0.0,
                                y = 0.0,
                                heading = 0.0
                        )
                )
                .splineTo(
                        pose = Pose2d(
                                x = 0.0,
                                y = 0.0,
                                heading = 0.0
                        )
                )
                .addMarker(point = Vector2d())
                {
                    bot.driveTrain.align(DriveTrain.LOADING_ZONE)
                    bot.sideArm.claw.state = SideArm.Claw.State.OPEN
                    sleep(250)
                    bot.sideArm.arm.state = SideArm.Arm.State.UP
                    bot.sideArm.claw.state = SideArm.Claw.State.CLOSED
                    bot.driveTrain.align(DriveTrain.LOADING_ZONE)
                }
                .build()

    }
}