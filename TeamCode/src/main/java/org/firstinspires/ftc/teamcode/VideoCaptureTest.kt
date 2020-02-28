package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.implementations.Auto
import org.firstinspires.ftc.teamcode.implementations.VisionFromWall2
import org.opencv.core.Mat
import org.opencv.videoio.VideoCapture

@Autonomous(name = "Testing VideoCapture", group = "Auto")
class VideoCaptureTest : Auto() {
    override fun genesis() {
        super.genesis()
        bot.pipeline2 = VisionFromWall2(this.telemetry)
    }

    override fun run() {
        var img = Mat()
        var i = 0
        for (i_ in 0..250) {
            val cap = VideoCapture(2)
            val read: Boolean = cap.read(img)
            if (!img.empty()) {
                break
            }
            i++
        }
        telemetry.addLine("i is [$i]")
        telemetry.update()

        if (img.empty()) {
            telemetry.addLine("[ERROR] NOT READ")
            telemetry.update()
        } else {
            val nImg = bot.pipeline2.processFrame(img)
            val positionFound = bot.pipeline2.POSITION
            telemetry.addLine(positionFound.toString())
            telemetry.addLine(bot.translateRelativePosition(positionFound).toString())
            telemetry.update()
        }
        sleep(5000)
    }
}