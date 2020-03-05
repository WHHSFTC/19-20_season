package org.firstinspires.ftc.teamcode.tests

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.implementations.DriveTrain
import org.firstinspires.ftc.teamcode.implementations.Summum
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

@Autonomous(name = "ArcTest", group = "Test")
class ArcTest : LinearOpMode(), OpModeIF {
    lateinit var bot: Summum

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        bot = Summum(this)
        bot.init()
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE)
        waitForStart()
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE)
        bot.driveTrain.goArc(ArcConstants.distance, ArcConstants.frontAngle, ArcConstants.turnAngle, ArcConstants.power, ArcConstants.waitTime)
        sleep(10000)
        bot.stop()
    }

    @Config
    object ArcConstants {
        @JvmStatic var distance = 14.0
        @JvmStatic var frontAngle = 90.0
        @JvmStatic var turnAngle = 90.0
        @JvmStatic var power = .5
        @JvmStatic var waitTime = 5.0
    }

    override fun getHardwareMap(): HardwareMap {
        return hardwareMap
    }

    override fun getTelemetry(): Telemetry {
        return telemetry
    }
}