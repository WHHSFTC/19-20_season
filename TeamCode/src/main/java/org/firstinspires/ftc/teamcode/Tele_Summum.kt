package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.implementations.*
import kotlin.math.*

@TeleOp(name = "Summum", group = "Tele")
class Tele_Summum : Tele() {
    private var prevInc: Boolean = false
    private var prevDec: Boolean = false
    private var prevCap: Boolean = false
    private var heightCounter: Int = 0

    companion object {
        const val DEADZONE = 0.05
    }

    override fun runLoop() {
        runDriveTrain()
        runOutput()
        runInput()
        runFoundationHooks()
        telemetry.update()
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
            bot.flywheels.power = -1.0
        }
        if (gamepad1.y) {
            bot.flywheels.power = 1.0
        }
        if (gamepad1.b) {
            bot.flywheels.power = 0.0
        }
    }

    fun runOutput() {
        if (gamepad2.right_bumper && !prevInc) {
            heightCounter++
        }

        if (gamepad2.left_bumper && !prevDec) {
            heightCounter--
        }

        prevInc = gamepad2.right_bumper
        prevDec = gamepad2.left_bumper

        heightCounter = heightCounter clip 0..VerticalSlides.Level.maxStackHeight
        bot.output.slides.height = heightCounter

        when {
            gamepad2.y -> {
                bot.output.claw.state = Claw.State.CLOSED
                bot.output.slides.isPlacing = false
                bot.output.slides.runVerticalSlides()
            }
            gamepad2.b -> {
                bot.output.slides.isPlacing = true
                bot.output.slides.runVerticalSlides()
            }
            gamepad2.a -> {
                bot.output.claw.state = Claw.State.OPEN

                if (bot.output.slides.height != 0) {
//                    bot.output.slides.isPlacing = false
//                    bot.output.slides.runVerticalSlides()
                    if (bot.output.slides.state == HorizontalSlides.State.OUT) {
                        bot.output.slides.vPower = .5
                        sleep(1000)

                        bot.output.slides.state = HorizontalSlides.State.IN
                        sleep(750)

                        bot.output.slides.vPower = .0
                    }
//
                    bot.output.slides.height = 0
                    bot.output.slides.runVerticalSlides()
                }

                bot.output.claw.state = Claw.State.INNER
            }
            gamepad2.x -> bot.output.claw.state = Claw.State.INNER
        }

        when {
            gamepad2.dpad_up -> bot.output.slides.state = HorizontalSlides.State.OUT
            gamepad2.dpad_down -> bot.output.slides.state = HorizontalSlides.State.IN
        }

        when {
            gamepad2.dpad_left -> bot.output.claw.state = Claw.State.OPEN
            gamepad2.dpad_right -> bot.output.claw.state = Claw.State.CLOSED
        }

        if (abs(gamepad2.right_stick_y) >= DEADZONE)
            bot.output.slides.vPower = -gamepad2.right_stick_y.toDouble()

//        if (gamepad2.left_stick_button && !prevCap) {
//            bot.capStone.state = if (bot.capStone.state == CapStone.State.HOLD) CapStone.State.PLACE else CapStone.State.HOLD
//        }
//
//        prevCap = gamepad2.left_stick_button

        telemetry.addData("[HEIGHT]", bot.output.slides.height)
//        bot.output.slides.dumpEncoders()
    }

    fun runFoundationHooks() {
        when {
            gamepad1.left_bumper -> bot.foundation.state = FoundationHooks.State.UP
            gamepad1.right_bumper -> bot.foundation.state = FoundationHooks.State.DOWN
        }
    }

    infix fun Double.max(other: Double): Double {
        return this.coerceAtLeast(other)
    }

    infix fun Int.clip(other: IntRange): Int {
        return min(max(other.first, heightCounter), other.last)
    }
}