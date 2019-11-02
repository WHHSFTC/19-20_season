package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.interfaces.Mechanism;

public abstract class StatefulServo<S extends StatefulServo.State> implements Mechanism<S> {
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
}
