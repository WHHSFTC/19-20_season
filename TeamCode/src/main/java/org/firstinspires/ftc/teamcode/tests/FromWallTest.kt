package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.implementations.Auto
import org.firstinspires.ftc.teamcode.implementations.DriveTrain
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition
import org.firstinspires.ftc.teamcode.implementations.VisionFromWall
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@Disabled
@Autonomous(name = "VisionFromWall test", group = "Auto")
class FromWallTest : Auto() {
    override fun genesis() {
        super.genesis()
        val cameraMonitorViewId = hardwareMap.
                appContext.
                resources.
                getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        bot.camera = OpenCvCameraFactory
                .getInstance()
                .createWebcam(
                        hardwareMap
                                .get(WebcamName::class.java, "Webcam 1"),
                        cameraMonitorViewId
                )

        bot.camera.openCameraDevice()

        bot.pipeline = VisionFromWall(bot, telemetry)
        bot.camera.setPipeline(bot.pipeline)

        bot.camera.startStreaming(320, 240, OpenCvCameraRotation.UPSIDE_DOWN)
    }

    override fun run() {
//        // getting sky-stone position
//        val untranslatedPosition: VisionFromWall.Position = bot.pipeline.POSITION
//
//        // translating from left, center, right to 1-4, 2-5, 3-6 based on alliance
//        val translatedSkyStonePosition: SkyStonePosition = bot.translateRelativePosition(untranslatedPosition)
//
//        // case to move to sky-stone
//        when (translatedSkyStonePosition) {
//            SkyStonePosition.ONE_FOUR -> {
//                bot.driveTrain.goAngle(10.0, DriveTrain.LOADING_ZONE, .5)
//                telemetry.addLine("Sky-Stone Position 1-4")
//            }
//            SkyStonePosition.TWO_FIVE -> { telemetry.addLine("Sky-Stone Position 2-5") }
//            SkyStonePosition.THREE_SIX -> {
//                bot.driveTrain.goAngle(10.0, DriveTrain.BUILDING_ZONE, .5)
//                telemetry.addLine("Sky-Stone Position 3-6")
//            }
//        }
//        telemetry.addLine(translatedSkyStonePosition.toString())
//        telemetry.update()
//
//        bot.camera.closeCameraDevice()
//        sleep(5000)
    }
}
