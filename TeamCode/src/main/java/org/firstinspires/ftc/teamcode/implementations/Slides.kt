package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
import kotlin.math.max

class Slides(opMode: OpModeIF, slideMotor1: String, slideMotor2: String, horizontalSlidesS: String) {
    private val verticalSlides = VerticalSlides(opMode = opMode, str1 = slideMotor1, str2 = slideMotor2)
    
    private val horizontalSlides = HorizontalSlides(opMode = opMode, str = horizontalSlidesS)
    
    var height: Int = verticalSlides.state.index
        set(value) {
            verticalSlides.state = VerticalSlides.Level(max(0, value), isPlacing)
            field = value
    }

    var isPlacing: Boolean = verticalSlides.state.isPlacing
        set(value) {
            verticalSlides.state = VerticalSlides.Level(height, value)
            field = value
        }

    var vPower: Double = verticalSlides.motorPower
        set(value) {
            verticalSlides.motorPower = value
            field = value
        }
    
    var state: HorizontalSlides.State = horizontalSlides.state
        set(value) {
            horizontalSlides.state = value
            field = value
        }

    fun runVerticalSlides(): Slides {
        verticalSlides.run()
        return this
    }

    fun dumpEncoders() {
        verticalSlides.dumpEncoders()
    }
}