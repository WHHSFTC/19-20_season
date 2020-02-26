package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.implementations.*

@Disabled
@Autonomous(name = "Camera View Check", group = "Auto")
class CameraViewCheck : Auto() {
    override fun genesis() {
        super.genesis()
        bot.visionTF = VisionTF(this, "Webcam 1")
    }

    override fun run() {
        sleep(5000)

        for (i in 0..1) {
            bot.driveTrain.goAngle(8.0, DriveTrain.LOADING_ZONE, .25)
            sleep(5000)
            if (bot.visionTF.stone) {
                telemetry.addLine("FOUND STONE")
                telemetry.update()
                sleep(5000)
                break
            }
        }
    }

    override fun halt() {
        super.halt()
        bot.visionTF.shutdown()
    }
}
