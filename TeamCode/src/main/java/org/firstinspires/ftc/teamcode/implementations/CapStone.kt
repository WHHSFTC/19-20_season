package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

class CapStone(opMode: OpModeIF, str: String) : StatefulServo<CapStone.State>(opMode.hardwareMap.servo[str]) {
    enum class State(override val position: Double): StatefulServo.State {
        HOLD(position = 0.0),
        PLACE(position = 1.0);
    }

    override val init: State
        get() = State.HOLD
}