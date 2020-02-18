package org.firstinspires.ftc.teamcode.implementations

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareDevice
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
import java.lang.IllegalArgumentException
import kotlin.math.*

class FlyWheels(var opMode: OpModeIF, var left: String, var right: String) : DcMotorSimple {
    var motorLeft: DcMotor = opMode.hardwareMap.dcMotor[left]
    var motorRight: DcMotor = opMode.hardwareMap.dcMotor[right]

    override fun setDirection(direction: DcMotorSimple.Direction?) {
        motorLeft.direction = direction
        motorRight.direction = direction
    }

    override fun setPower(power: Double) {
        if (abs(power) > 1.0) throw IllegalArgumentException("power is out of range (-1, 1)")
        motorLeft.power = power
        motorRight.power = -power
    }

    override fun resetDeviceConfigurationForOpMode() {
        throw RuntimeException("Stub!")
    }

    override fun getDeviceName(): String {
        throw RuntimeException("Stub!")
    }

    override fun getConnectionInfo(): String {
        throw RuntimeException("Stub!")
    }

    override fun getVersion(): Int {
        throw RuntimeException("Stub!")
    }

    override fun getDirection(): DcMotorSimple.Direction {
        throw RuntimeException("Stub!")
    }

    override fun getPower(): Double {
        throw RuntimeException("Stub!")
    }

    override fun close() {
        throw RuntimeException("Stub!")
    }

    override fun getManufacturer(): HardwareDevice.Manufacturer {
        throw RuntimeException("Stub!")
    }

}