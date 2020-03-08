package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.implementations.Auto
import org.firstinspires.ftc.teamcode.implementations.DriveTrain

@Autonomous(group = "Auto", name = "SimplePark")
class SimplePark : Auto() {
    override fun run() { // park
        bot.driveTrain.goAngle(12.0, DriveTrain.LOADING_ZONE, 0.5)
    }
}