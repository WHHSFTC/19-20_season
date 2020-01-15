package org.firstinspires.ftc.teamcode.implementations

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.qualcomm.hardware.motors.NeveRest20Gearmotor
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType

@Config
class DriveConstants {

    val MOTOR_CONFIG = MotorConfigurationType.getMotorType(NeveRest20Gearmotor::class.java)

    val RUN_USING_ENCODER = true
    val MOTOR_VELO_PID: PIDCoefficients? = null

    val WHEEL_RADIUS = 2.0
    val GEAR_RATIO = 1.0 // output (wheel) speed / input (motor) speed
    val TRACK_WIDTH = 1.0

    val kV : Double
        get() = 1.0 / rpmToVelocity(maxRpm)
    val kA : Double
        get() = 0.0
    val kStatic : Double
        get() = 0.0

    val BASE_CONSTRAINTS : DriveConstraints
        get() = DriveConstraints(
            maxVel = 30.0,
            maxAccel = 30.0,
            maxJerk = 0.0,
            maxAngVel = Math.toRadians(180.0),
            maxAngAccel = Math.toRadians(180.0),
            maxAngJerk = 0.0
        )

    fun encoderTicksToInches(ticks: Double): Double {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / MOTOR_CONFIG.ticksPerRev
    }

    fun rpmToVelocity(rpm: Double): Double {
        return rpm * GEAR_RATIO * 2 * Math.PI * WHEEL_RADIUS / 60.0
    }

    val maxRpm: Double
        get() = MOTOR_CONFIG.maxRPM *
                if (RUN_USING_ENCODER) MOTOR_CONFIG.achieveableMaxRPMFraction else 1.0

    val ticksPerSec: Double
        get() =
            MOTOR_CONFIG.maxRPM * MOTOR_CONFIG.ticksPerRev / 60.0


    val motorVelocityF: Double
        get() =
            32767 / ticksPerSec
}