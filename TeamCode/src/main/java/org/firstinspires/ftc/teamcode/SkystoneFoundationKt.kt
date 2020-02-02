package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.implementations.*

/**
 * Created by khadija on 1/29/2020.
 */
//@Disabled
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

        // heads back to go under skybridge
        bot.driveTrain.goAngle(14.0, bot.our_side, .5)

        // goes 36 inches into building zone
        bot.driveTrain.goAngle(skyStonePosition.distance + 65, DriveTrain.BUILDING_ZONE, .5)

        // moving the arm to hold position
        bot.sideArm.arm.state = SideArm.Arm.State.HOLD

        // alignment
//        bot.driveTrain.align(DriveTrain.LOADING_ZONE);
//        moving to side of foundation to drop stone on it
        bot.driveTrain.goAngle(12.0, bot.opponents_side, .5)

        // drops stone
        bot.sideArm.arm.state = SideArm.Arm.State.DOWN

        sleep(250)

        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(100)

        bot.sideArm.arm.state = SideArm.Arm.State.UP

        sleep(250)

        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        // heading back to rotate
        bot.driveTrain.goAngle(4.0, bot.our_side, .75)

        // aligning the robot to turn to have foundation hooks facing foundation
        bot.driveTrain.align(bot.our_side)

        // bot.driveTrain.goAngle(8, DriveTrain.BUILDING_ZONE, .75);
        // heading back to the foundation to be flushed to put down foundation hooks
        bot.driveTrain.goAngle(8.0, bot.opponents_side, .25)

        sleep(250)

        // closing foundation hooks
        bot.shuttleGate.state = ShuttleGate.State.FOUNDATION

        sleep(250)

        when (bot.alliance) {
            Alliance.BLUE -> bot.driveTrain.goArc(14.0, 90.0, 90.0, 1.0, 5.0)
            Alliance.RED -> bot.driveTrain.goArc(14.0, 90.0, -90.0, 1.0, 5.0)
            null -> {}
        }
        // releasing foundation hooks
        bot.shuttleGate.state = ShuttleGate.State.CLOSED

        bot.driveTrain.goAngle(8.0, bot.opponents_side, .5)

        bot.driveTrain.goAngle(14.0, DriveTrain.BUILDING_ZONE, .5)

        bot.driveTrain.goAngle(45.0, DriveTrain.LOADING_ZONE, .5)
    }

    private fun intakeSkystone() {
        // turn so sidearm faces stones
        // bot.driveTrain.align(DriveTrain.LOADING_ZONE);
        // lines up sidearm
        bot.driveTrain.goAngle(2.0, DriveTrain.LOADING_ZONE, .5)

        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // moves forward to be line with stone
        bot.driveTrain.goAngle(13.0, bot.opponents_side, .75)

        // bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // claw closes on stone
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        sleep(250)
    }

}
