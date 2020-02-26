package org.firstinspires.ftc.teamcode.tests

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.implementations.Auto

@Config
object IntakeTestConstants {
    @JvmStatic
    val flyPower: Double = -1.0

    @JvmStatic
    val drivePower: Double = .25

    @JvmStatic
    val angleDifference: Double = 0.0
}

@Autonomous(name = "Test Running into stone", group = "Auto")
class RunIntoStone : Auto() {
    override fun run() {
        bot.flywheels.power = IntakeTestConstants.flyPower

        telemetry.addData("Flywheels", "[RUNNING]")
        telemetry.update()
        bot.driveTrain.align(IntakeTestConstants.angleDifference)
        bot.driveTrain.goAngle(30.0, IntakeTestConstants.angleDifference, IntakeTestConstants.drivePower)

        sleep(2500)
    }

}