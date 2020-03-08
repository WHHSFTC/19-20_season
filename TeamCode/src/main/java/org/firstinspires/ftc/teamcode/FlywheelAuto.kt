package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.implementations.*
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation


@Autonomous(name = "Flywheel intake", group = "Auto")
class FlywheelAuto : Auto() {
    override fun genesis() {
        super.genesis()
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        bot.camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName::class.java, "Webcam 1"), cameraMonitorViewId)

        bot.camera.openCameraDevice()

        bot.pipeline = VisionFromWall(telemetry, bot.alliance)
        bot.camera.setPipeline(bot.pipeline)

        bot.camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT)
    }

    @Throws(InterruptedException::class)
    override fun run() {
        // goes out to push first stone away
        bot.driveTrain.goAngle(50 - Summum.ROBOT_WIDTH / 2, bot.opponents_side, 0.75)

        // intakes second stone
        bot.flywheels.power = -1.0

        bot.driveTrain.goAngle(8.0, DriveTrain.LOADING_ZONE, 0.5)

        sleep(1000)

        bot.flywheels.power = 0.0

        // withdraws
        bot.driveTrain.goAngle(4.0, DriveTrain.BUILDING_ZONE, 0.5)
        bot.driveTrain.goAngle(4 + Summum.ROBOT_WIDTH / 2, bot.our_side, 0.75)

        // goes to foundation and hooks on
        bot.driveTrain.align(bot.our_side)

        bot.driveTrain.goAngle(70.0, DriveTrain.BUILDING_ZONE, 0.75)
        bot.driveTrain.goAngle(6.0, bot.opponents_side, 0.50)

        bot.foundation.state = FoundationHooks.State.DOWN

        sleep(1000)

        when (bot.alliance) {
            Alliance.BLUE -> bot.driveTrain.goArc(15.0, 90.0, 90.0, 1.0, 6.0)
            Alliance.RED -> bot.driveTrain.goArc(15.0, 90.0, -90.0, 1.0, 6.0)
        }

        // ram foundation into wall
        bot.driveTrain.goAngle(14.0, DriveTrain.BUILDING_ZONE, .75)

        bot.output.slides.state = HorizontalSlides.State.OUT

        sleep(250)

        bot.output.claw.state = Claw.State.OPEN

        sleep(250)

        bot.output.slides.state = HorizontalSlides.State.IN

        sleep(250)

        bot.output.claw.state = Claw.State.INNER

        bot.driveTrain.goAngle(45.0, DriveTrain.LOADING_ZONE, .75)
    }
}