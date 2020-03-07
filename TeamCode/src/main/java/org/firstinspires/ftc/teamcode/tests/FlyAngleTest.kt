package org.firstinspires.ftc.teamcode.tests

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.implementations.DriveTrain
import org.firstinspires.ftc.teamcode.implementations.Summum
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

//@Disabled
@Autonomous(name = "Flywheel Angle Test", group = "Test")
class FlyAngleTest : LinearOpMode(), OpModeIF {
    lateinit var bot: Summum

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        bot = Summum(this)
        bot.init()
        waitForStart()
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE)
        bot.flywheels.power = AngleConstants.flyPower
        bot.driveTrain.align(AngleConstants.angle)
        bot.driveTrain.goAngle(AngleConstants.distance, AngleConstants.angle, AngleConstants.power)
        sleep(AngleConstants.waitTime)
        bot.flywheels.power = 0.0
        bot.stop()
    }

    @Config
    object AngleConstants {
        @JvmStatic var distance = 10.0
        @JvmStatic var angle = 0.0
        @JvmStatic var power = .5
        @JvmStatic var waitTime = 5L
        @JvmStatic var flyPower = -1.0
    }

    override fun getHardwareMap(): HardwareMap {
        return hardwareMap
    }

    override fun getTelemetry(): Telemetry {
        return telemetry
    }
}