package org.firstinspires.ftc.teamcode.implementations

import com.qualcomm.robotcore.hardware.CRServo
import org.firstinspires.ftc.teamcode.interfaces.Mechanism
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

class TapeMeasurer(var opMode: OpModeIF, var conf: String): Mechanism<TapeMeasurer.State> {
    val servo: CRServo = opMode.hardwareMap.crservo[conf]
    override var state: State = State.HOLD
        set(value) {
            if (field == value) return
            if (value == State.PARK) servo.power = 0.5
            if (value == State.HOLD) servo.power = -0.5
            Thread.sleep(500)
            servo.power = 0.0
            field = value
        }
    enum class State {
        PARK, HOLD
    }

    override fun stop() {
        servo.power = 0.0
    }
}