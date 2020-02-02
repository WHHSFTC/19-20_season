package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.implementations.*

@Autonomous(group = "Auto", name = "One Sky Stone only: Park by Bridge")
class SkyStone1Only : Auto() {

    override fun genesis() {
        super.genesis()
        bot.visionTF = VisionTF(this, "Webcam 1")
    }

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

        skyStonePosition = try {
            bot.findSkystone()
        } catch (ex: InterruptedException) {
            telemetry.addLine(ex.message)
            telemetry.update()
            requestOpModeStop()
            return
        }

        // intaking the skystone
        intakeSkystone()

        // heads back to go under skybridge
        bot.driveTrain.goAngle(10.0, bot.our_side, .5)

        // goes 36 inches into building zone
        bot.driveTrain.goAngle(skyStonePosition.distance + 40, DriveTrain.BUILDING_ZONE, .5)
        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // drops stone
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(250)

        // arm up to not get hit by sky bridge
        bot.sideArm.arm.state = SideArm.Arm.State.UP
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        bot.driveTrain.goAngle(10.0, DriveTrain.LOADING_ZONE, .5)
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

    @Throws(InterruptedException::class)
    override fun halt() {
        super.halt()
        bot.visionTF.shutdown()
    }
}