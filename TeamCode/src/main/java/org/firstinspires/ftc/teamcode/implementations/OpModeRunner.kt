package org.firstinspires.ftc.teamcode.implementations

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

abstract class OpModeRunner: LinearOpMode(), OpModeIF {
    protected lateinit var bot: Summum

    @Throws(InterruptedException::class)
    abstract fun genesis()

    @Throws(InterruptedException::class)
    abstract fun halt()

    @Throws(InterruptedException::class)
    abstract fun begin()

    override fun getHardwareMap(): HardwareMap? {
        return this.hardwareMap
    }

    override fun getTelemetry(): Telemetry {
        return this.telemetry
    }

}