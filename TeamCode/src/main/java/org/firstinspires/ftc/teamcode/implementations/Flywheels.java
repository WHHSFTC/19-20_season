package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;

// acts like a crservo but is actually two crservos in opposite directions
public class Flywheels implements CRServo {
    private CRServo servoLeft;
    private CRServo servoRight;
    public Flywheels(LinearOpMode opMode) {
        servoLeft = opMode.hardwareMap.crservo.get("leftFly");
        servoRight = opMode.hardwareMap.crservo.get("rightFly");
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
        power *= 0.5;
        if(power > .5 || power < -0.5) throw new IllegalArgumentException("Power out of -0.5 to 0.5 range");
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
