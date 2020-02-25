package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.implementations.*

/**
 * Created by khadija on 1/4/2020.
 */
@Autonomous(name = "Double skystone on Foundation", group = "Auto")
class DoubleFoundation : Auto() {
    @Throws(InterruptedException::class)
    override fun genesis() {
        super.genesis()
        bot.visionTF = VisionTF(this, "Webcam 1")
    }

    override fun run() {
        // drive towards stones
        bot.driveTrain.goAngle(41 - Summum.ROBOT_LENGTH, bot.opponents_side, .5)

        // preparing arm to grab
        bot.sideArm.arm.state = SideArm.Arm.State.DOWN
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        bot.opMode.telemetry.addLine("Starting TensorFlow Search")
        bot.opMode.telemetry.update()

        // finding position of sky stone
        val skyStonePosition: SkyStonePosition = try {
            bot.findSkystone()
        } catch (ex: InterruptedException) {
            telemetry.addLine(ex.message)
            telemetry.update()
            requestOpModeStop()
            return
        }

        // intaking the skystone
        intakeSkystone()

        bot.sideArm.arm.state = SideArm.Arm.State.HOLD

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // heads back to go under skybridge
        bot.driveTrain.goAngle(15.0, bot.our_side, .5)

        // goes 36 inches into building zone
        bot.driveTrain.goAngle(skyStonePosition.distance + 70, DriveTrain.BUILDING_ZONE, .5)

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        bot.driveTrain.goAngle(8.0, bot.opponents_side, .5)

        sleep(250)

        // drops stone
        bot.sideArm.arm.state = SideArm.Arm.State.DOWN

        sleep(250)

        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(350)

        // arm up to not get hit by sky bridge
        bot.sideArm.arm.state = SideArm.Arm.State.UP
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        sleep(250)

        bot.driveTrain.goAngle(11.0, bot.our_side, .5)

        // second cycle
        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // heading back to get second sky stone
        bot.driveTrain.goAngle(skyStonePosition.distance + (24 + 68), DriveTrain.LOADING_ZONE, .5)

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        bot.sideArm.arm.state = SideArm.Arm.State.DOWN

        sleep(250)

        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(250)

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // driving to get the stone
        bot.driveTrain.goAngle(10.0, bot.opponents_side, .5)

        sleep(250)

        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        sleep(250)

        bot.sideArm.arm.state = SideArm.Arm.State.HOLD

        sleep(250)

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // heads back with stone
        bot.driveTrain.goAngle(12.0, bot.our_side, .5)
        bot.driveTrain.goAngle(skyStonePosition.distance + 24 + 78, DriveTrain.BUILDING_ZONE, .5)

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        bot.driveTrain.goAngle(8.0, bot.opponents_side, .5)

        sleep(250)

        // drop stone
        bot.sideArm.arm.state = SideArm.Arm.State.DOWN
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(250)

        // bring up arm
        bot.sideArm.arm.state = SideArm.Arm.State.UP
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        bot.driveTrain.goAngle(11.0, bot.our_side, .5)

        // adjustment
        bot.driveTrain.goAngle(2.0, bot.opponents_side, .5)

        // park
        bot.driveTrain.goAngle(24.0, DriveTrain.LOADING_ZONE, .5)
    }

    private fun intakeSkystone() {
        // backing up
        // bot.driveTrain.goAngle(8, bot.our_side, .5);
        // turn so sidearm faces stones
        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // lines up sidearm
        bot.driveTrain.goAngle(2.0, DriveTrain.LOADING_ZONE, .5)

        // bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        // bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
        // moves forward to be line with stone
        bot.driveTrain.goAngle(14.0, bot.opponents_side, .75)

        // claw closes on stone
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        sleep(350)
    }

    @Throws(InterruptedException::class)
    override fun halt() {
        super.halt()
        bot.visionTF.shutdown()
    }
}