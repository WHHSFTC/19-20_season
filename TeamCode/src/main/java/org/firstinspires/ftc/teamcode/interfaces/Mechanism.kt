package org.firstinspires.ftc.teamcode.interfaces

// S is the type of the state
interface Mechanism<S> {
    var state: S

    fun stop()
}