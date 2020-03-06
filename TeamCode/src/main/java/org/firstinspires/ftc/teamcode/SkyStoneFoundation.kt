package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.implementations.*
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@Autonomous(name = "One SkyStone Foundation", group = "Auto")
class SkyStoneFoundation: Auto() {
    override fun genesis() {
        super.genesis()
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        bot.camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName::class.java, "Webcam 1"), cameraMonitorViewId)

        bot.camera.openCameraDevice()

        bot.pipeline = VisionFromWall(telemetry)
        bot.camera.setPipeline(bot.pipeline)

        bot.camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT)
    }

    override fun run() {
        val stonePosition: VisionFromWall.Position = bot.pipeline.position

        val convertedPosition: SkyStonePosition = bot.translateRelativePosition(stonePosition)

        when(convertedPosition) {
            SkyStonePosition.ONE_FOUR -> bot.driveTrain.goAngle(
                    if (bot.alliance == Alliance.BLUE) Summum.ROBOT_WIDTH - 10.5 else 0.0, DriveTrain.BUILDING_ZONE, .5)
            SkyStonePosition.TWO_FIVE -> bot.driveTrain.goAngle(
                    if (bot.alliance == Alliance.BLUE) Summum.ROBOT_WIDTH - 2.5 else 0.0, DriveTrain.BUILDING_ZONE, .5)
            SkyStonePosition.THREE_SIX -> bot.driveTrain.goAngle(
                    if (bot.alliance == Alliance.BLUE) 0.0 else 0.0, DriveTrain.LOADING_ZONE, .5)
        }
        when (convertedPosition) {
            SkyStonePosition.ONE_FOUR, SkyStonePosition.TWO_FIVE -> {
                // clear from wall for turn, then go to quarry
                bot.driveTrain.goAngle(15.0, bot.opponents_side, .5)
                bot.driveTrain.align(DriveTrain.LOADING_ZONE)
                bot.driveTrain.goAngle(35.0-Summum.ROBOT_WIDTH/2.0, bot.opponents_side, .5)
                // intake
                bot.flywheels.power = -1.0
                bot.driveTrain.goAngle(6.0, DriveTrain.LOADING_ZONE, .25)
                sleep(1000)
                // withdraw, end up with front of bot in center of skystone location
//                    bot.driveTrain.goAngle(4.0, DriveTrain.BUILDING_ZONE, .75)
                bot.flywheels.power = 0.0
                // move out of quarry, 8 inches of leeway
                bot.driveTrain.goAngle(Summum.ROBOT_WIDTH/2.0 + 10.0, bot.our_side, .5)

            }
            SkyStonePosition.THREE_SIX -> {
                bot.driveTrain.goAngle(15.0, bot.opponents_side, .5)
                bot.driveTrain.align(DriveTrain.BUILDING_ZONE)
                bot.driveTrain.goAngle(6.0, DriveTrain.LOADING_ZONE, .75)
                bot.driveTrain.goAngle(35.0-Summum.ROBOT_WIDTH/2, bot.opponents_side, .5)
                // intake
                bot.flywheels.power = -1.0
                bot.driveTrain.goAngle(14.0, DriveTrain.BUILDING_ZONE, .25)
                sleep(1000L)
                bot.flywheels.power = 0.0
                // move out of quarry, 8 inches of leeway
                bot.driveTrain.goAngle(Summum.ROBOT_WIDTH/2.0 + 10.0, bot.our_side, .5)
                bot.driveTrain.align(DriveTrain.LOADING_ZONE)
            }
        }
        // go to foundation
        bot.driveTrain.goAngle(convertedPosition.distance + 24.0 - Summum.ROBOT_WIDTH/2 + 48.0, DriveTrain.BUILDING_ZONE, .75)
        // clear from foundation for turn, then go to foundation
        bot.driveTrain.goAngle(5.0, bot.our_side, .75)
        bot.driveTrain.align(bot.our_side)
        bot.driveTrain.goAngle(14.0, bot.opponents_side, .25)
        bot.foundation.state = FoundationHooks.State.DOWN
        runBlocking {
            val arcJob = GlobalScope.launch {
                delay(500L)
                when (bot.alliance) {
                    Alliance.BLUE -> bot.driveTrain.goArc(15.0, 90.0, 90.0, 1.0, 6.0)
                    Alliance.RED -> bot.driveTrain.goArc(15.0, 90.0, -90.0, 1.0, 6.0)
                }
                bot.foundation.state = FoundationHooks.State.UP
                bot.driveTrain.goAngle(14.0, DriveTrain.BUILDING_ZONE, .75)
                bot.driveTrain.goAngle(6.0, bot.opponents_side, .75)
            }
            sleep(500L)
            bot.output.claw.state = Claw.State.CLOSED
            sleep(500L)
            bot.output.slides.state = HorizontalSlides.State.OUT
            sleep(1000L)
            bot.output.claw.state = Claw.State.OPEN
            sleep(500L)
            arcJob.join()
        }
        bot.output.claw.state = Claw.State.INNER
        bot.driveTrain.goAngle(45.0, DriveTrain.LOADING_ZONE, .75)

        bot.camera.stopStreaming()
    }
}