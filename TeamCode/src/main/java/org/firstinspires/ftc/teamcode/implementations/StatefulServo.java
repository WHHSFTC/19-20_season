package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.interfaces.Mechanism;

// wrapper for Servo that uses an enum of states
public abstract class StatefulServo<S extends StatefulServo.State> implements Mechanism<S>, Servo {
    // implemented by state enums of subclasses
    public interface State {
        // returns the position
        public double getPosition();
    }
    // servo is a servo motor
    protected Servo servo;
    // state represents the state
    private S state;
    public S getState() {
        return state;
    }
    public void setState(S state) {
        servo.setPosition(state.getPosition());
        this.state = state;
    }
    public void stop() {}

    @Override
    public ServoController getController() {
        return servo.getController();
    }

    @Override
    public Direction getDirection() {
        return servo.getDirection();
    }

    @Override
    public void setDirection(Direction direction) {
        servo.setDirection(direction);
    }

    @Override
    public double getPosition() {
        return servo.getPosition();
    }

    @Override
    public void setPosition(double position) {
        setPosition(position);
    }

    @Override
    public int getPortNumber() {
        return servo.getPortNumber();
    }

    @Override
    public void scaleRange(double min, double max) {
        servo.scaleRange(min, max);
    }

    @Override
    public Manufacturer getManufacturer() {
        return servo.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return servo.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return servo.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return servo.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        servo.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        servo.close();
    }
}
