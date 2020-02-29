package org.firstinspires.ftc.teamcode.implementations

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.interfaces.Mechanism
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

class FoundationHooks(opMode: OpModeIF, leftStr: String, rightStr: String): Mechanism<FoundationHooks.State> {
    private val left: Servo = opMode.hardwareMap.servo[leftStr]
    private val right: Servo = opMode.hardwareMap.servo[rightStr]

    enum class State(val left: Double, val right: Double) {
        UP(0.0, 0.0),
        DOWN(1.0, 1.0)
    }

    override var state: State = State.UP
        set(value) {
            left.position = value.left
            right.position = value.right
            field = value
        }

    override fun stop() {
        state = State.UP
    }
}