package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.interfaces.Mechanism;

public class ShuttleGate implements Mechanism<ShuttleGate.State> {
    private State state;
    private LeftGate left;
    private RightGate right;
    ShuttleGate(HardwareMap hwmap) {
        left = new LeftGate(hwmap);
        right = new RightGate(hwmap);
    }

    @Override
    public void setState(State state) {
        left.setState(state.getLeft());
        right.setState(state.getRight());
        this.state = state;
    }

    @Override
    public State getState() {
        return state;
    }

    public enum State {
        FOUNDATION(LeftGate.State.FOUNDATION, RightGate.State.FOUNDATION),
        OPEN(LeftGate.State.OPEN, RightGate.State.OPEN),
        CLOSED(LeftGate.State.CLOSED, RightGate.State.CLOSED);
        private LeftGate.State left;
        private RightGate.State right;
        State(LeftGate.State left, RightGate.State right) {
            this.left = left;
            this.right = right;
        }

        public LeftGate.State getLeft() {
            return left;
        }

        public RightGate.State getRight() {
            return right;
        }
    }
}
class LeftGate extends StatefulServo<LeftGate.State> {
    LeftGate(HardwareMap hwmap) {
        servo = hwmap.servo.get("leftGate");
    }
    enum State implements StatefulServo.State {
        FOUNDATION(1), OPEN(0), CLOSED(0.48);
        private double value;
        State(double value) {
            this.value = value;
        }

        @Override
        public double getPosition() {
            return value;
        }
    }
}
class RightGate extends StatefulServo<RightGate.State> {
    RightGate(HardwareMap hwmap) {
        servo = hwmap.servo.get("rightGate");
    }
    enum State implements StatefulServo.State {
        FOUNDATION(0), OPEN(0.8), CLOSED(0.5);
        private double value;
        State(double value) {
            this.value = value;
        }

        @Override
        public double getPosition() {
            return value;
        }
    }
}
