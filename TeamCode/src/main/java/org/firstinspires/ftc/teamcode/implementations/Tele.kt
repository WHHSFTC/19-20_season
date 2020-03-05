package org.firstinspires.ftc.teamcode.implementations

abstract class Tele: OpModeRunner() {
    override fun runOpMode() {
        genesis()
        waitForStart()
        while (opModeIsActive()) runLoop()
        halt()
    }

    abstract fun runLoop()

    override fun genesis() {
        bot = Summum(this)
    }

    override fun halt() {
        bot.stop()
    }
}