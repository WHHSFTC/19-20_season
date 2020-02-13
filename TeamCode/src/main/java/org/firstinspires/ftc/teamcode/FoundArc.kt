package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.implementations.*

@Autonomous(name = "FoundArc: Park by the wall", group = "Auto")
class FoundArc : Auto() {

    @Throws(InterruptedException::class)
    override fun run() {
        bot.driveTrain.align(bot.our_side)

        // heading over 2 tiles to get lined up with the center of the foundation
        // in reference to the middle of the bot
        bot.driveTrain.goAngle(12.0, DriveTrain.BUILDING_ZONE, 0.25)

        bot.driveTrain.align(bot.our_side)

        bot.driveTrain.goAngle(54 - Sursum.ROBOT_LENGTH, bot.opponents_side, 0.25) // Hard coded distance

        bot.shuttleGate.state = ShuttleGate.State.FOUNDATION

        when (bot.alliance) {
            Alliance.BLUE -> bot.driveTrain.goArc(15.0, 90.0, 90.0, 0.75, 6.0)
            Alliance.RED -> bot.driveTrain.goArc(15.0, 90.0, -90.0, 0.75, 6.0)
            null -> {}
        }

        bot.shuttleGate.state = ShuttleGate.State.CLOSED

        bot.driveTrain.goAngle(24.0, DriveTrain.BUILDING_ZONE, .25)

        bot.driveTrain.goAngle(24.0, bot.our_side, .5)

        bot.driveTrain.goAngle(48.0, DriveTrain.LOADING_ZONE, .5)

    }
}
