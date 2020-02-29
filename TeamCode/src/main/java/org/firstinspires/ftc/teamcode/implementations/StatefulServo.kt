package org.firstinspires.ftc.teamcode.implementations

import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoController
import org.firstinspires.ftc.teamcode.interfaces.Mechanism

// wrapper for Servo that uses an enum of states
abstract class StatefulServo<S : StatefulServo.State?>(
// servo is a servo motor
        protected var servo: Servo) : Mechanism<S>, Servo {
    // implemented by state enums of subclasses
    interface State {
        // returns the position
        val position: Double
    }
    abstract val init: S

    override var state: S = init
        set(value) {
            servo.position = value!!.position
            field = value
        }

    override fun stop() {}
    override fun getController(): ServoController {
        return servo.controller
    }

    override fun getDirection(): Servo.Direction {
        return servo.direction
    }

    override fun setDirection(direction: Servo.Direction) {
        servo.direction = direction
    }

    override fun getPosition(): Double {
        return servo.position
    }

    override fun setPosition(position: Double) {
        servo.position = position
    }

    override fun getPortNumber(): Int {
        return servo.portNumber
    }

    override fun scaleRange(min: Double, max: Double) {
        servo.scaleRange(min, max)
    }

    override fun getManufacturer(): Manufacturer {
        return servo.manufacturer
    }

    override fun getDeviceName(): String {
        return servo.deviceName
    }

    override fun getConnectionInfo(): String {
        return servo.connectionInfo
    }

    override fun getVersion(): Int {
        return servo.version
    }

    override fun resetDeviceConfigurationForOpMode() {
        servo.resetDeviceConfigurationForOpMode()
    }

    override fun close() {
        servo.close()
    }

}