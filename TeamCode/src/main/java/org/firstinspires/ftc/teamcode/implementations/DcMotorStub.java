package org.firstinspires.ftc.teamcode.implementations;


import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import java.util.Locale;

// a stub that implements the methods of DcMotor for logging
public class DcMotorStub implements com.qualcomm.robotcore.hardware.DcMotor {
    private MotorConfigurationType motorType;
    private ZeroPowerBehavior zeroPowerBehavior;
    private boolean powerFloat;
    private int targetPosition;
    private double power;
    private Direction direction;
    private RunMode mode;
    private LinearOpMode opMode;

    DcMotorStub(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "MotorType: %s%nZeroPowerBehavior: %s%nPowerFloat: %b%nTargetPosition: %d%nPower: %a%nDirection: %s%nRunMode %s%n", motorType, zeroPowerBehavior, powerFloat, targetPosition, powerFloat, direction, mode);
    }

    @Override
    public MotorConfigurationType getMotorType() {
        return motorType;
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        this.motorType = motorType;
    }

    @Override
    public DcMotorController getController() {
        return null;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        this.zeroPowerBehavior = zeroPowerBehavior;
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return zeroPowerBehavior;
    }

    @Override
    public void setPowerFloat() {
        powerFloat = true;
    }

    @Override
    public boolean getPowerFloat() {
        return powerFloat;
    }

    @Override
    public void setTargetPosition(int position) {
        targetPosition = position;
    }

    @Override
    public int getTargetPosition() {
        return targetPosition;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void setMode(RunMode mode) {
        this.mode = mode;
    }

    @Override
    public RunMode getMode() {
        return mode;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setPower(double power) {
        this.power = power;
    }

    @Override
    public double getPower() {
        return power;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return "Stub";
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }
}
