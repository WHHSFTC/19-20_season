package org.firstinspires.ftc.teamcode.implementations

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism
import org.firstinspires.ftc.teamcode.interfaces.Mechanism
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max

class VerticalSlides(val opMode: OpModeIF, str1: String, str2: String) : Mechanism<VerticalSlides.Level> {
    @Config
    companion object VerticalConf {
        @JvmStatic
        val TICKS_PER_LEVEL: Double = 0.0
        @JvmStatic
        val DEFAULT_POWER: Double = 1.0
    }

    var motor1: DcMotor = opMode.hardwareMap.dcMotor.get(str1)
    var motor2: DcMotor = opMode.hardwareMap.dcMotor.get(str2)

    fun dumpEncoders() {
        opMode.telemetry.addData("[MOTOR 1]", motor1.currentPosition)
        opMode.telemetry.addData("[MOTOR 2]", motor2.currentPosition)
    }

    init {
        motor1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor1.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motor1.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motor1.direction = DcMotorSimple.Direction.REVERSE

        motor2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor2.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motor2.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    var method: Level.EncoderLocation = Level.EncoderLocation.MEAN

    /**number of blocks high**/
    override var state = Level(0)

    fun run(): VerticalSlides {
        val dummyValue = state.calculateValue(method)
        motor1.targetPosition = dummyValue
        motor1.mode = DcMotor.RunMode.RUN_TO_POSITION

        motor2.targetPosition = dummyValue
        motor2.mode = DcMotor.RunMode.RUN_TO_POSITION

        motor1.power = power
        motor2.power = power

        while(motor1.isBusy || motor2.isBusy) Thread.sleep(50)

        motor1.power = 0.0
        motor2.power = 0.0

        return this
    }

    var motorPower: Double = 0.0
        set(value) {
            if (motor1.mode != DcMotor.RunMode.RUN_WITHOUT_ENCODER) motor1.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            if (motor2.mode != DcMotor.RunMode.RUN_WITHOUT_ENCODER) motor2.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            field = value
            motor1.power = value
            motor2.power = value
        }

    /**power used by vertical slides**/
    var power: Double = DEFAULT_POWER
        set(value) {
            if (abs(value) > 1)
                throw IllegalArgumentException("Power is $value which is greater than 1")
            field = value
        }

    override fun stop() {
        motor1.power = 0.0
        motor2.power = 0.0
    }

    class Level(var index: Int, var isPlacing: Boolean = false, var level: StoneLevels = StoneLevels.NULL) {

        enum class StoneLevels(var above: List<Int>, var place: List<Int> = listOf(0, 0)) {
            // TODO get these locations
            NULL(listOf(0, 0)),
            ONE(above = listOf(310, 320)),
            TWO(above = listOf(390, 400), place = listOf(300, 310)),
            THREE(above = listOf(830, 840), place = listOf(570, 580)),
            FOUR(above = listOf(1060, 1070), place = listOf(820, 830)),
            FIVE(above = listOf(1320, 1330), place = listOf(1020, 1030)),
            SIX(above = listOf(1630, 1640), place = listOf(1320, 1330));
            // TODO check how high we are able to go

            val clearRange: IntRange get() = above.range()
            val placeRange: IntRange get() = place.range()

            val clear: List<Int>
                get() {
                    val ret: MutableList<Int> = ArrayList()
                    ret.addAll(clearRange)
                    return ret
                }
            val placement: List<Int>
                get() {
                    val ret: MutableList<Int> = ArrayList()
                    ret.addAll(placeRange)
                    return ret
                }
        }

        companion object {
            private val levels: List<StoneLevels> = StoneLevels
                    .values()
                    .toList()
                    .subList(0, StoneLevels.values().toList().size)
        }

        enum class Location {
            ABOVE,
            PLACE;
        }

        enum class EncoderLocation {
            MIN,
            MAX,
            MEAN,
            MEDIAN,
            RANDOM;
        }

        init {
            level = levels[index]
        }

        fun reIndex() {
            index = max(0, index)
            level = levels[index]
        }

        fun increment(numberOfIncrement: Int): Level {
            index += numberOfIncrement
            reIndex()
            return this
        }

        operator fun inc(): Level {
            return increment(1)
        }

        private operator fun plus(other: Int) {
            increment(other)
        }

        operator fun plusAssign(other: Int) {
            return this + other
        }

        fun decrement(numberOfDecrement: Int) {
            increment(-numberOfDecrement)
        }

        private operator fun minus(other: Int) {
            decrement(other)
        }

        operator fun minusAssign(other: Int) {
            return this - other
        }

        fun calculateValue(method: EncoderLocation): Int {
            return if (isPlacing) {
                when (method) {
                    EncoderLocation.MEDIAN -> level.placement.median()
                    EncoderLocation.MIN -> level.placement.min()!!
                    EncoderLocation.MAX -> level.placement.max()!!
                    EncoderLocation.MEAN -> level.placement.mean()
                    EncoderLocation.RANDOM -> level.placement[Random().nextInt(level.placement.size)]
                }
            }
            else {
                when (method) {
                    EncoderLocation.MEDIAN -> level.clear.median()
                    EncoderLocation.MIN -> level.clear.min()!!
                    EncoderLocation.MAX -> level.clear.max()!!
                    EncoderLocation.MEAN -> level.clear.mean()
                    EncoderLocation.RANDOM -> level.clear[Random().nextInt(level.clear.size)]
                }
            }
        }
    }
}
fun List<Int>.range(): IntRange {
    return this.min()!! .. this.max()!!
}

fun List<Int>.median(): Int {
    return if (this.size % 2 == 1)
        this[this.size / 2]
    else
        (this[this.size / 2] + this[this.size / 2 + 1]) / 2
}

fun List<Int>.mean(): Int {
    return this.sum() / this.size
}