package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.implementations.*
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

import kotlinx.coroutines.*

@Autonomous(name = "Double SkyStone Deliver", group = "Auto")
class DoubleSkyStone : Auto() {
    override fun genesis() {
        super.genesis()

        /**creating camera and setting up pipeline**/
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        bot.camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName::class.java, "Webcam 1"), cameraMonitorViewId)

        bot.camera.openCameraDevice()

        bot.pipeline = VisionFromWall(telemetry, bot.alliance)
        bot.camera.setPipeline(bot.pipeline)

        bot.camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT)
    }

    override fun run() {
        /**getting sky stone position from vision pipeline**/
        val stonePosition: VisionFromWall.Position = bot.pipeline.position

        /**translating position from pipeline**/
        val convertedPosition: SkyStonePosition = bot.translateRelativePosition(stonePosition)

        /**fancy switch statement**/
        when (convertedPosition) {
            SkyStonePosition.ONE_FOUR ->
                bot.driveTrain.goAngle(
                        Summum.ROBOT_WIDTH - 10.5 - (if (bot.alliance == Alliance.RED) 8.0 else 0.0),
                        DriveTrain.BUILDING_ZONE,
                        .5
                )
            SkyStonePosition.TWO_FIVE ->
                bot.driveTrain.goAngle(
                        Summum.ROBOT_WIDTH - 1.5 - (if (bot.alliance == Alliance.RED) 8.0 else 0.0),
                        DriveTrain.BUILDING_ZONE,
                        .5
                )
            SkyStonePosition.THREE_SIX ->
                bot.driveTrain.goAngle(
                        0.0 - (if (bot.alliance == Alliance.RED) 8.0 else 0.0),
                        DriveTrain.BUILDING_ZONE,
                        .5
                )
        }

        when (convertedPosition) {
            SkyStonePosition.ONE_FOUR, SkyStonePosition.TWO_FIVE -> {
                // clear from wall for turn, then go to quarry
                bot.driveTrain.goAngle(15.0, bot.opponents_side, .5)

                bot.driveTrain.align(DriveTrain.LOADING_ZONE)

                bot.driveTrain.goAngle(35.0 - Summum.ROBOT_WIDTH / 2.0, bot.opponents_side, .5)

                // intake
                bot.flywheels.power = -1.0

                bot.driveTrain.goAngle(6.0, DriveTrain.LOADING_ZONE, .25)

                sleep(1000)

                bot.flywheels.power = 0.0

                // move out of quarry, 8 inches of leeway
                bot.driveTrain.goAngle(Summum.ROBOT_WIDTH / 2.0 + 10.0, bot.our_side, .5)

                bot.driveTrain.align(DriveTrain.LOADING_ZONE)
            }

            SkyStonePosition.THREE_SIX -> {
                bot.driveTrain.goAngle(15.0, bot.opponents_side, .5)

                bot.driveTrain.align(DriveTrain.BUILDING_ZONE)

                bot.driveTrain.goAngle(6.0, DriveTrain.LOADING_ZONE, .75)

                bot.driveTrain.goAngle(35.0 - Summum.ROBOT_WIDTH / 2, bot.opponents_side, .5)

                // intake
                bot.flywheels.power = -1.0

                bot.driveTrain.goAngle(14.0, DriveTrain.BUILDING_ZONE, .25)

                sleep(1000L)

                bot.flywheels.power = 0.0

                // move out of quarry, 8 inches of leeway
                bot.driveTrain.goAngle(Summum.ROBOT_WIDTH / 2.0 + 10.0, bot.our_side, .5)

                bot.driveTrain.align(DriveTrain.BUILDING_ZONE)
            }
        }

        bot.output.claw.state = Claw.State.CLOSED

        runBlocking {
            val armJob = GlobalScope.launch {
                delay(750L)

                bot.output.slides.state = HorizontalSlides.State.OUT
            }

            bot.driveTrain.goAngle(convertedPosition.distance + 40.0, DriveTrain.BUILDING_ZONE, .75)

            bot.output.claw.state = Claw.State.OPEN

            sleep(250L)

            bot.output.slides.state = HorizontalSlides.State.IN

            bot.driveTrain.align(DriveTrain.LOADING_ZONE)

            bot.output.claw.state = Claw.State.INNER

            bot.driveTrain.goAngle(convertedPosition.distance + (15.0 + 40.0), DriveTrain.LOADING_ZONE, .75)

            bot.driveTrain.goAngle(Summum.ROBOT_WIDTH / 2.0 + 10.0, bot.opponents_side, .5)

            bot.flywheels.power = -1.0

            bot.driveTrain.goAngle(6.0, DriveTrain.LOADING_ZONE, .25)

            bot.flywheels.power = 0.0

            bot.driveTrain.goAngle(Summum.ROBOT_WIDTH / 2.0 + 10.0, bot.our_side, .5)

            armJob.start()

            bot.driveTrain.goAngle(convertedPosition.distance + (15.0 + 40.0), DriveTrain.BUILDING_ZONE, .75)

            bot.output.claw.state = Claw.State.OPEN

            sleep(250L)

            bot.output.slides.state = HorizontalSlides.State.IN

            bot.driveTrain.align(DriveTrain.LOADING_ZONE)

            bot.output.claw.state = Claw.State.INNER

            bot.driveTrain.goAngle(6.0, bot.opponents_side, .5)

            bot.driveTrain.goAngle(16.0, DriveTrain.LOADING_ZONE, .75)
        }
    }

    override fun halt() {
        super.halt()
        bot.camera.stopStreaming()
    }
}
