package org.firstinspires.ftc.teamcode.implementations

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

class VerticalSlides(val bot: Summum, opMode: OpModeIF, str1: String, str2: String) : ContinuousMechanism {
    @Config
    companion object VertConf {
        @JvmStatic
        val TICKS_PER_LEVEL: Double = 0.0
    }
    var state: Double = 0.0
        get() = field
        set(value) {
            field = value
        }
    var power: Double = 0.5
        get() = field
        set(value) {
            field = value
        }
    var motor1: DcMotor = opMode.hardwareMap.dcMotor.get(str1)
    var motor2: DcMotor = opMode.hardwareMap.dcMotor.get(str2)
    override fun getMax(): Double {
        return 11.0;
    }

    override fun getState(): Double {
        return state
    }

    override fun stop() {
        motor1.power = 0.0
        motor2.power = 0.0
    }

    override fun getMin(): Double {
        return 0.0;
    }

    override fun setState(state: Double?) {
        state!!
        motor1.targetPosition = (state * VertConf.TICKS_PER_LEVEL).toInt()
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION)
        motor2.targetPosition = (state * VertConf.TICKS_PER_LEVEL).toInt()
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION)
        motor1.power = power
        motor2.power = power
        while(motor1.isBusy || motor2.isBusy) Thread.sleep(50)
        motor1.power = 0.0
        motor2.power = 0.0
        this.state = state
    }

}