package org.firstinspires.ftc.teamcode.implementations

abstract class Tele: OpModeRunner() {
    override fun runOpMode() {
        genesis()
        waitForStart()
        while (opModeIsActive()) run()
        halt()
    }

    abstract fun run()

    fun genesis() {
        bot = Summum(this)
    }

    fun halt() {
        bot.stop()
    }
}