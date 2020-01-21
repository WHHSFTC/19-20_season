package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.implementations.*
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

@Autonomous(name = "Slides Increment Test", group = "Tele")
class Increment_Test : LinearOpMode(), OpModeIF{

    /**
     * companion object to create constant value threshold
     */
    companion object { private const val THRESHOLD: Double = .5 }

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
    private class CurrentLevel(private var bot: Sursum, private var level: Int = 0) {
        // private val of increment between levels
        private val levelIncrement: Int = 250

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
         * TODO automate turning arm and claw
         */
        fun gotoLevel(power: Double = 1.0) {
            increment()
            (bot.outputSlides as OutputSlides).runToPosition(level, power)
            decrement()
            (bot.outputSlides as OutputSlides).runToPosition(level, power)
        }
    }

    /**
     * override of runOpMode from LinearOpMode
     */
    override fun runOpMode() {
        // creating Sursum object
        val bot = Sursum(this)

        // creating CurrentLevel counter object
        val level = CurrentLevel(bot)

        // initialization of tele-op arms
        bot.leftArm.arm.state = SideArm.Arm.State.UP
        bot.rightArm.arm.state = SideArm.Arm.State.UP

        // initialization of tele-op claws
        (bot.leftArm.claw as LeftSideArm.Claw).servo.position = 1.0
        (bot.rightArm.claw as RightSideArm.Claw).servo.position = 0.0

        waitForStart()

        while(opModeIsActive()) {
            if (gamepad2.left_trigger >= THRESHOLD) level.increment()

            if (gamepad2.right_trigger >= THRESHOLD) level.decrement()

            if (gamepad2.x) level.gotoLevel()

            if (gamepad2.left_bumper) bot.claw.state = Claw.State.CLOSED

            if (gamepad2.right_bumper) bot.claw.state = Claw.State.OPEN

            if (gamepad2.dpad_left || gamepad2.dpad_right) bot.arm.state = Arm.State.OUT

            if (gamepad2.dpad_down || gamepad2.dpad_up) bot.arm.state = Arm.State.RIGHT

            if (gamepad2.b) bot.arm.state = Arm.State.BELT
        }
    }
}
