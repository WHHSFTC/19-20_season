package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.implementations.*

@Autonomous(name = "FTCLib Vision", group = "Auto")
class FTCLibVisionTest : Auto() {
    override fun genesis() {
        super.genesis()
        bot.visionFtc = VisionFTC(this.telemetry)
    }

    override fun run() {
        // getting sky-stone position
        val untranslatedPosition: VisionFTC.SkystonePosition = bot.visionFtc.skystonePosition!!

        // translating from left, center, right to 1-4, 2-5, 3-6 based on alliance
        val translatedSkyStonePosition: SkyStonePosition = bot.translateRelativePosition(untranslatedPosition)

        // case to move to sky-stone
        when (translatedSkyStonePosition) {
            SkyStonePosition.ONE_FOUR -> {
                bot.driveTrain.goAngle(10.0, DriveTrain.LOADING_ZONE, .5)
                telemetry.addLine("Sky-Stone Position 1-4")
            }
            SkyStonePosition.TWO_FIVE -> { telemetry.addLine("Sky-Stone Position 2-5") }
            SkyStonePosition.THREE_SIX -> {
                bot.driveTrain.goAngle(10.0, DriveTrain.BUILDING_ZONE, .5)
                telemetry.addLine("Sky-Stone Position 3-6")
            }
        }
        telemetry.update()

        bot.sideArm.arm.state = SideArm.Arm.State.DOWN

        // go to pick up the stone
        bot.driveTrain.goAngle(45.0 - Sursum.ROBOT_LENGTH, bot.opponents_side, .5)

        // alignment
        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // grabbing stone
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        sleep(250)

        // heading back to not hit sky-bridge
        bot.driveTrain.goAngle(15.0, bot.our_side, .5)

        // heading back distance to drop off stone
        bot.driveTrain.goAngle(translatedSkyStonePosition.distance + 40, DriveTrain.BUILDING_ZONE, .5)

        // dropping stone
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(250)

        // moving arm up to not break on bridge
        bot.sideArm.arm.state = SideArm.Arm.State.UP
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        sleep(100)

        // heading back to new stone
        bot.driveTrain.goAngle(translatedSkyStonePosition.distance + (24 + 40), DriveTrain.LOADING_ZONE, .75)

        // dropping arm; opening claw
        bot.sideArm.arm.state = SideArm.Arm.State.DOWN
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(250)

        // alignment
        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        // heading over to pick up stone
        bot.driveTrain.goAngle(15.0, bot.opponents_side, .25)

        // grabbing stone
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        sleep(250)

        // heading back to go under bridge
        bot.driveTrain.goAngle(15.0, bot.our_side, .25)

        // alignment
        bot.driveTrain.align(DriveTrain.LOADING_ZONE)

        sleep(100)

        // heading under bridge to drop second stone
        bot.driveTrain.goAngle(translatedSkyStonePosition.distance + (24 + 40), DriveTrain.BUILDING_ZONE, .5)

        // drop stone
        bot.sideArm.claw.state = SideArm.Claw.State.OPEN

        sleep(250)

        // moving arm up to not break
        bot.sideArm.arm.state = SideArm.Arm.State.UP
        bot.sideArm.claw.state = SideArm.Claw.State.CLOSED

        // moving over to not hit other bot
        bot.driveTrain.goAngle(4.0, bot.opponents_side, .75)

        // moving under bridge to break the plane
        bot.driveTrain.goAngle(16.0, DriveTrain.LOADING_ZONE, .75)
    }
}
