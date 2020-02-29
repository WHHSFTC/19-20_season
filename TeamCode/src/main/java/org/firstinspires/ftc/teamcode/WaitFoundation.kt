package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.implementations.*

@Autonomous(name = "WaitFoundation", group = "Auto")
class WaitFoundation : Auto() {
    override fun run() {
        bot.driveTrain.align(bot.our_side)
        // heading over 2 tiles to get lined up with the center of the foundation
// in reference to the middle of the bot
        bot.driveTrain.goAngle(12.0, DriveTrain.BUILDING_ZONE, 0.25)
        bot.driveTrain.align(bot.our_side)
        bot.driveTrain.goAngle(54 - Summum.ROBOT_LENGTH, bot.opponents_side, 0.25) // Hard coded distance
        sleep(1000)
        // setting foundation hooks to hook onto the foundation
        bot.foundation.state = FoundationHooks.State.DOWN
        sleep(1000)
        bot.driveTrain.goAngle(60 - Summum.ROBOT_LENGTH, bot.our_side, .25)
        bot.foundation.state = FoundationHooks.State.UP
        sleep(1000)
        // heading to park under bridge
        bot.driveTrain.goAngle(50.0, DriveTrain.LOADING_ZONE, 1.0)
    }
}