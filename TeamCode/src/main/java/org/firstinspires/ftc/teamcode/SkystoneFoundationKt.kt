package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.implementations.*

/**
 * Created by khadija on 1/29/2020.
 */
@Autonomous(name = "One Sky Stone and move Foundation : Park near bridge", group = "Auto")
class SkystoneFoundationKt : Auto() {

    @Throws(InterruptedException::class)
    override fun genesis() {
        super.genesis()
        bot.visionTF = VisionTF(this, "Webcam 1")
    }

    @Throws(InterruptedException::class)
    override fun run() {
        // servo calls
        bot.sideArm.arm.state = SideArm.Arm.State.DOWN
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        // preparing arm to grab
        bot.driveTrain.goAngle(41 - Sursum.ROBOT_LENGTH, bot.opponents_side, .5)

        bot.opMode.telemetry.addLine("Starting TensorFlow Search")
        bot.opMode.telemetry.update()

        // finding position of sky stone
        val skyStonePosition: SkyStonePosition

        try {
            skyStonePosition = bot.findSkystone()
        } catch (ex: InterruptedException) {
            telemetry.addLine(ex.message)
            telemetry.update()
            requestOpModeStop()
            return
        }

        // intaking the skystone
        intakeSkystone()

        // heads back to go under skybridge
        bot.driveTrain.goAngle(14.0, bot.our_side, .5)

        // goes 36 inches into building zone
        bot.driveTrain.goAngle(skyStonePosition.distance + 70, DriveTrain.BUILDING_ZONE, .5)

        bot.sideArm.arm.state = SideArm.Arm.State.HOLD

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        bot.driveTrain.goAngle(10.0, bot.opponents_side, .5)

        // drops stone
        bot.sideArm.arm.state = SideArm.Arm.State.DOWN

        sleep(250)

        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(100)

        bot.sideArm.arm.state = SideArm.Arm.State.UP

        sleep(250)

        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        bot.driveTrain.goAngle(10.0, bot.our_side, .5)

        bot.driveTrain.align(bot.our_side)

        bot.driveTrain.goAngle(12.0, bot.opponents_side, .5)

        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION)

        when (bot.alliance) {
            Alliance.BLUE -> bot.driveTrain.goArc(15.0, 90.0, 90.0, 0.5)
            Alliance.RED -> bot.driveTrain.goArc(15.0, 90.0, -90.0, 0.5)
            null -> {}
        }

        bot.shuttleGate.setState(ShuttleGate.State.CLOSED)

        bot.driveTrain.goAngle(24.0, DriveTrain.BUILDING_ZONE, .25)

        bot.driveTrain.goAngle(24.0, bot.our_side, .5)

        bot.driveTrain.goAngle(22.0, bot.opponents_side, .5)

        bot.driveTrain.goAngle(48.0, DriveTrain.LOADING_ZONE, .5)

    }

    private fun intakeSkystone() {
        // turn so sidearm faces stones
        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // lines up sidearm
        bot.driveTrain.goAngle(2.0, DriveTrain.LOADING_ZONE, .5)

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // moves forward to be line with stone
        bot.driveTrain.goAngle(13.0, bot.opponents_side, .75)

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // claw closes on stone
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        sleep(250)
    }

}
