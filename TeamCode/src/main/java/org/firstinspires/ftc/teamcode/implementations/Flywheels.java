package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;

public class Flywheels implements CRServo {
    private CRServo servoLeft;
    private CRServo servoRight;
    public Flywheels(HardwareMap hwmap) {
        servoLeft = hwmap.crservo.get("leftFly");
        servoRight = hwmap.crservo.get("rightFly");
    }
    @Override
    public ServoController getController() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getPortNumber() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void setDirection(Direction direction) {
        servoLeft.setDirection(direction);
        servoRight.setDirection(direction == Direction.FORWARD ? Direction.REVERSE : Direction.FORWARD);
    }

    @Override
    public Direction getDirection() {
        return servoLeft.getDirection();
    }

    @Override
    public void setPower(double power) {
        if(power > 1 || power < -1) throw new IllegalArgumentException("Power out of -1 to 1 range");
        servoLeft.setPower(power);
        servoRight.setPower(power);
    }

    @Override
    public double getPower() {
        return servoLeft.getPower();
    }

    @Override
    public Manufacturer getManufacturer() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public String getDeviceName() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public String getConnectionInfo() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getVersion() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void close() {
        throw new RuntimeException("Stub!");
    }
}
