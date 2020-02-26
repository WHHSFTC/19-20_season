package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.implementations.DriveTrain
import org.firstinspires.ftc.teamcode.implementations.IncrementalOutput
import org.firstinspires.ftc.teamcode.implementations.OutputSlides
import org.firstinspires.ftc.teamcode.implementations.Summum
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
import kotlin.math.*

class Tele_Summum : LinearOpMode(), OpModeIF {
    companion object {
        const val DEADZONE = .05
    }
    private val bot: Summum = Summum(this)

    override fun getHardwareMap(): HardwareMap {
        return this.hardwareMap
    }

    override fun getTelemetry(): Telemetry {
        return this.telemetry
    }

    override fun runOpMode() {

    }

    fun runDriveTrain() {
        var xpow = gamepad1.left_stick_x.toDouble()
        var ypow = -gamepad1.left_stick_y.toDouble()
        var zpow = gamepad1.right_stick_x.toDouble()

        val theta = atan2(ypow, xpow) //angle of joystick

        val power = (abs(xpow) max abs(ypow)).pow(2.0) //logarithmic drive

        // ternaries for dead-zone logic
        xpow = if (abs(xpow) > DEADZONE) xpow else 0.0
        ypow = if (abs(ypow) > DEADZONE) ypow else 0.0
        zpow = if (abs(zpow) > DEADZONE) zpow else 0.0

        val zpower = abs(zpow).pow(2.0)
        val x = cos(theta)
        val y = sin(theta)
        val z = sign(zpow)

        (bot.driveTrain as DriveTrain).setPowers(
                power * -(y - x) + zpower * z,
                power * -(-y - x) + zpower * z,
                power * -(-y + x) + zpower * z,
                power * -(y + x) + zpower * z)

        // offset of pi/4 makes wheels strafe correctly at cardinal and intermediate directions
        telemetry.addData("xpow", xpow)
        telemetry.addData("zpow", zpow)
        telemetry.addData("ypow", ypow)
        telemetry.addData("theta", theta)
    }

    fun runInput() {
        if (gamepad1.a) {
            bot.flywheels.power = -2.0/3.0
            bot.belt.power = -1.0
        }
        if (gamepad1.y) {
            bot.flywheels.power = -2.0/3.0
            bot.belt.power = -1.0
        }
        if (gamepad1.b) {
            bot.flywheels.power = 0.0
            bot.belt.power = 0.0
        }
    }

    fun runOutput() {
        if (gamepad2.dpad_up) {
            (bot.outputSlides as IncrementalOutput).currentLevel += 1
        }
        if (gamepad2.dpad_down) {
            (bot.outputSlides as IncrementalOutput).currentLevel -= 1
        }
        if (gamepad2.y) {
            (bot.outputSlides as IncrementalOutput).goto(
                    location = IncrementalOutput.Level.Location.ABOVE,
                    encoderLocation = IncrementalOutput.Level.EncoderLocation.MIN
            )
        }
        if (gamepad2.x) {
            (bot.outputSlides as IncrementalOutput).goto(
                    location = IncrementalOutput.Level.Location.PLACE,
                    encoderLocation = IncrementalOutput.Level.EncoderLocation.MIN
            )
        }

        bot.outputSlides.state = if (abs(gamepad2.right_stick_x) >= DEADZONE)
            gamepad2.right_stick_x.toDouble()
        else
            0.0
        (bot.outputSlides as OutputSlides).dumpEncoders()
    }

    infix fun Double.max(other: Double): Double {
        return this.coerceAtLeast(other)
    }
}