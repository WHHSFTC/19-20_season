package org.firstinspires.ftc.teamcode.tests

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.implementations.Auto
import org.firstinspires.ftc.teamcode.implementations.OutputSlides

@Disabled
@Autonomous(name = "Slides Test", group = "Auto")
class SlidesTest : Auto() {

    @Config
    object Constants {
        @JvmField var position: Int = -684
        @JvmField var power: Double = 1.0
    }

    @Throws(InterruptedException::class)
    override fun run() {
        telemetry.addLine("Running to position -684")
        telemetry.update()

        sleep(250)

        (bot.outputSlides as OutputSlides).runToPosition(Constants.position, Constants.power)
        (bot.outputSlides as OutputSlides).dumpEncoders()

        sleep(5000)
    }
}
