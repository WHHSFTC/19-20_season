package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

class Output(val bot: Summum, opMode: OpModeIF) {
    var height: Double = 0.0
        set(value) {

            field = value
        }

}