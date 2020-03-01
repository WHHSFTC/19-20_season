package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.teamcode.interfaces.Mechanism
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

class CapStone(opMode: OpModeIF, str: String) : Mechanism<CapStone.State> {
    private val capServo = opMode.hardwareMap.servo[str]

    enum class State(val position: Double) {
        HOLD(position = 0.0),
        PLACE(position = 1.0);
    }

    override var state: State = State.HOLD
        set(value) {
            capServo.position = value.position
            field = value
        }

    override fun stop() {
        state = State.HOLD
    }
}