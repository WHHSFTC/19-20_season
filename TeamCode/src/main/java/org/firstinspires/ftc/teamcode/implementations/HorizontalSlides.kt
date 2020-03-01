package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

class HorizontalSlides(val opMode: OpModeIF, str: String):
        StatefulServo<HorizontalSlides.State>(opMode.hardwareMap.servo[str]) {

    enum class State(override val position: Double): StatefulServo.State {
        IN(position = 0.0), OUT(position = 0.75);
    }

    override val init: State = State.IN
}
