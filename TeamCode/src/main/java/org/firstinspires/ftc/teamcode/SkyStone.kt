package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.implementations.*

@Autonomous(name = "SkyStone: Park near bridge", group = "Auto")
class SkyStone : Auto() {

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
        bot.driveTrain.goAngle(14.0, bot.our_side, .5)

        // goes 36 inches into building zone
        bot.driveTrain.goAngle(skyStonePosition.distance + 36, DriveTrain.BUILDING_ZONE, .5)
        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // drops stone
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(250)

        // arm up to not get hit by sky bridge
        bot.sideArm.arm.state = SideArm.Arm.State.UP
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        // second cycle
        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // heading back to get second sky stone
        bot.driveTrain.goAngle(skyStonePosition.distance + (24 + 36), DriveTrain.LOADING_ZONE, .75)

        bot.sideArm.arm.state = SideArm.Arm.State.DOWN
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(250)

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // driving to get the stone
        bot.driveTrain.goAngle(10.0, bot.opponents_side, .25)

        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        sleep(250)

        // heads back with stone
        bot.driveTrain.goAngle(14.0, bot.our_side, .5)

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        bot.driveTrain.goAngle(skyStonePosition.distance + 24 + 36, DriveTrain.BUILDING_ZONE, .5)

        // drop stone
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(250)

        // bring up arm
        bot.sideArm.arm.state = SideArm.Arm.State.UP
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        // adjustment
        bot.driveTrain.goAngle(2.0, bot.opponents_side, .75)

        // park
        bot.driveTrain.goAngle(16.0, DriveTrain.LOADING_ZONE, .75)
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