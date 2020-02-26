package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
import java.util.Random

class IncrementalOutput(opMode: OpModeIF, str1: String, str2: String, var currentLevel: Level = Level())
    : OutputSlides(opMode, str1, str2) {
    class Level(var level: StoneLevels = StoneLevels.NULL) {
        private var index = 0

        enum class StoneLevels(var above: List<Int>, var place: List<Int> = listOf(0, 0)) {
            // TODO get these locations
            NULL(listOf(0, 0)),
            CLEAR(above = listOf(310, 320)),
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
            private val clearHooks: List<Int> = StoneLevels.CLEAR.clear
            private val clearHookRange: IntRange = StoneLevels.CLEAR.clearRange
            private val levels: List<StoneLevels> = StoneLevels
                    .values()
                    .toList()
                    .subList(2, StoneLevels.values().toList().size)
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
            level = levels[index]
        }

        fun increment(numberOfIncrement: Int) {
            index += numberOfIncrement
            reIndex()
        }

        private operator fun plus(other: Int) {
            return increment(other)
        }

        operator fun plusAssign(other: Int) {
            return this + other
        }

        fun decrement(numberOfDecrement: Int) {
            increment(numberOfIncrement = -numberOfDecrement)
        }

        private operator fun minus(other: Int) {
            return decrement(other)
        }

        operator fun minusAssign(other: Int) {
            return this - other
        }
    }

    fun goto(location: Level.Location, power: Double = 1.0, encoderLocation: Level.EncoderLocation = Level.EncoderLocation.MEDIAN): Boolean {
        var encoderValue: Int = 0
        when (location) {
            Level.Location.ABOVE -> encoderValue = when (encoderLocation) {
                Level.EncoderLocation.MEDIAN -> currentLevel.level.clear.median()
                Level.EncoderLocation.MAX -> currentLevel.level.clear.max()!!
                Level.EncoderLocation.MIN -> currentLevel.level.clear.min()!!
                Level.EncoderLocation.MEAN -> currentLevel.level.clear.mean()
                Level.EncoderLocation.RANDOM -> currentLevel.level.above[Random().nextInt(currentLevel.level.clear.size)]
            }

            Level.Location.PLACE -> encoderValue = when (encoderLocation) {
                Level.EncoderLocation.MEDIAN -> currentLevel.level.placement.median()
                Level.EncoderLocation.MAX -> currentLevel.level.placement.max()!!
                Level.EncoderLocation.MIN -> currentLevel.level.placement.min()!!
                Level.EncoderLocation.MEAN -> currentLevel.level.placement.mean()
                Level.EncoderLocation.RANDOM -> currentLevel.level.placement[Random().nextInt(currentLevel.level.placement.size)]
            }
        }
        runToPosition(encoderValue, power)
        return true
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
