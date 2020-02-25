package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.implementations.*
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
@Disabled
@TeleOp(name = "Slides Increment Test", group = "Tele")
class Increment_Test : LinearOpMode(), OpModeIF{
    /**
     * override of getHardwareMap from OpModeIF
     */
    override fun getHardwareMap(): HardwareMap { return hardwareMap }

    /**
     * override of getTelemetry from OpModeIF
     */
    override fun getTelemetry(): Telemetry { return telemetry }

    /**
     * class CurrentLevel
     * used to keep track of the current level as well as perform basic arm functionalities
     */
    private class CurrentLevel(private var bot: Summum, private var level: Int = 0) {

        enum class StoneLevel(var above: List<Int>, var place: List<Int> = listOf()) {
            CLEAR(above = listOf(310, 320)),
            TWO(above = listOf(390, 400), place = listOf(300, 310)),
            THREE(above = listOf(830, 840), place = listOf(570, 580)),
            FOUR(above = listOf(1060, 1070), place = listOf(820, 830)),
            FIVE(above = listOf(1320, 1330), place = listOf(1020, 1030)),
            SIX(above = listOf(1630, 1640), place = listOf(1320, 1330));

            val clearRange: List<Int> get() = above
            val placeRange: List<Int> get() = place
        }

        companion object {
            private val levelValues: List<StoneLevel> = StoneLevel.values().toList()
        }

        var l: Int = 0

        init {
            l = level
        }

        val value: Int get() = level

        // private val of increment between levels
        private val levelIncrement: Int = 250

        // constant value of the height of foundation hooks
        // TODO check if this location is correct
        private val foundationHookHeight: Int = 200

        // increments level based on the number of levels
        fun increment(levels: Int = 1) {
            level += levelIncrement * levels
        }

        // decrements level based on the number of levels
        fun decrement(levels: Int = 1) {
            level -= levelIncrement * levels
        }

        /**
         * functionality
         * automation of reaching stone level
         */
        fun gotoLevel(power: Double = 1.0) {
            increment()
            (bot.outputSlides as OutputSlides).runToPosition(level, power)
        }

        /**
         * DEPRECATED
         */
        fun returnToZero(power: Double = 1.0) {
            if (level < foundationHookHeight) {
                increment(levels = 2)
                (bot.outputSlides as OutputSlides).runToPosition(level, power)
            }
            bot.arm.state = Arm.State.BELT
            (bot.outputSlides as OutputSlides).runToPosition(0, power)
        }
    }

    /**
     * override of runOpMode from LinearOpMode
     */
    override fun runOpMode() {
        // creating the Sursum object
        val bot = Summum(this)

        // creating CurrentLevel counter object
        val level = CurrentLevel(bot)

        // initialization of tele-op arms
        bot.leftArm.arm.state = SideArm.Arm.State.UP
        bot.rightArm.arm.state = SideArm.Arm.State.UP

        // initialization of tele-op claws
        (bot.leftArm.claw as LeftSideArm.Claw).servo.position = 1.0
        (bot.rightArm.claw as RightSideArm.Claw).servo.position = 0.0

        telemetry.addLine("Initialization DONE")
        telemetry.update()

        waitForStart()

        var upPressed = false
        var downPressed = false

        while(opModeIsActive()) {
            if (gamepad2.y && !upPressed) level.increment()

            if (gamepad2.a && !downPressed) level.decrement()

            upPressed = gamepad2.y
            downPressed = gamepad2.a

            if (gamepad2.x) {
                telemetry.addLine("Running gotoLevel")
                telemetry.update()
                level.gotoLevel()
            }

            if (gamepad2.left_bumper) bot.claw.state = Claw.State.CLOSED

            if (gamepad2.right_bumper) bot.claw.state = Claw.State.OPEN

            if (gamepad2.dpad_left || gamepad2.dpad_right) bot.arm.state = Arm.State.OUT

            if (gamepad2.dpad_down || gamepad2.dpad_up) bot.arm.state = Arm.State.RIGHT

            if (gamepad2.b) bot.arm.state = Arm.State.BELT

            telemetry.addData("Level value", level.value)
            telemetry.addData("Motor position", (bot.outputSlides as OutputSlides).dumpEncoders())
            telemetry.update()
        }
    }
}
