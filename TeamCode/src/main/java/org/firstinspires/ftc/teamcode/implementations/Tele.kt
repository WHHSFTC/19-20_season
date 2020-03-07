package org.firstinspires.ftc.teamcode.implementations

abstract class Tele: OpModeRunner() {
    override fun runOpMode() {
        genesis()
        waitForStart()
        begin()
        while (opModeIsActive()) runLoop()
        halt()
    }

    abstract fun runLoop()

    override fun genesis() {
        bot = Summum(this)
        bot.initTele()
    }
    override fun begin() {
        bot.start()
    }

    override fun halt() {
        bot.stop()
    }
}