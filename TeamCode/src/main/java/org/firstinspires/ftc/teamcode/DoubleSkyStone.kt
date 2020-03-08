package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.implementations.*
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@Autonomous(name = "Double SkyStone Deliver", group = "Auto")
class DoubleSkyStone : Auto() {
    override fun genesis() {
        super.genesis()
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        bot.camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName::class.java, "Webcam 1"), cameraMonitorViewId)

        bot.camera.openCameraDevice()

        bot.pipeline = VisionFromWall(telemetry, bot.alliance)
        bot.camera.setPipeline(bot.pipeline)

        bot.camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT)
    }

    override fun run() {

    }
}