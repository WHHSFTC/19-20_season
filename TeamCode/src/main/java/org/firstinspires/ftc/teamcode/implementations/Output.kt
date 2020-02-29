package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

open class Output(
        val opMode: OpModeIF,
        slideMotor1: String,
        slideMotor2: String,
        horizontalServo: String,
        innerClaw: String,
        outerClaw: String
) {
    val slides = Slides(
            opMode = opMode,
            slideMotor1 = slideMotor1,
            slideMotor2 = slideMotor2,
            horizontalSlidesS = horizontalServo
    )

    val claw = Claw(opMode, innerClaw, outerClaw)
}
