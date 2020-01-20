package org.firstinspires.ftc.teamcode.implementations

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.qualcomm.hardware.motors.NeveRest20Gearmotor
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType

@Config
object DriveConstants {

    @JvmStatic
    val MOTOR_CONFIG = MotorConfigurationType.getMotorType(NeveRest20Gearmotor::class.java)

    @JvmStatic
    val RUN_USING_ENCODER = true

    @JvmStatic
    val MOTOR_VELO_PID: PIDCoefficients? = null

    @JvmStatic
    val WHEEL_RADIUS = 2.0

    @JvmStatic
    val GEAR_RATIO = 1.0 // output (wheel) speed / input (motor) speed

    @JvmStatic
    val TRACK_WIDTH = 1.0

    @JvmStatic
    val kV : Double
        get() = 1.0 / rpmToVelocity(maxRpm)

    @JvmStatic
    val kA : Double
        get() = 0.0

    @JvmStatic
    val kStatic : Double
        get() = 0.0
    @JvmStatic
    val BASE_CONSTRAINTS : DriveConstraints
        get() = DriveConstraints(
            maxVel = 30.0,
            maxAccel = 30.0,
            maxJerk = 0.0,
            maxAngVel = Math.toRadians(180.0),
            maxAngAccel = Math.toRadians(180.0),
            maxAngJerk = 0.0
        )

    @JvmStatic
    fun encoderTicksToInches(ticks: Double): Double {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / MOTOR_CONFIG.ticksPerRev
    }

    @JvmStatic
    fun rpmToVelocity(rpm: Double): Double {
        return rpm * GEAR_RATIO * 2 * Math.PI * WHEEL_RADIUS / 60.0
    }

    @JvmStatic
    val maxRpm: Double
        get() = MOTOR_CONFIG.maxRPM *
                if (RUN_USING_ENCODER) MOTOR_CONFIG.achieveableMaxRPMFraction else 1.0

    @JvmStatic
    val ticksPerSec: Double
        get() =
            MOTOR_CONFIG.maxRPM * MOTOR_CONFIG.ticksPerRev / 60.0

    @JvmStatic
    val motorVelocityF: Double
        get() =
            32767 / ticksPerSec
}
