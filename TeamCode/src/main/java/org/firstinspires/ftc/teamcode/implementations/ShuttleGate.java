package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

public class ShuttleGate implements Mechanism<ShuttleGate.State> {
    private State state;
    private LeftGate left;
    private RightGate right;
    ShuttleGate(OpModeIF opMode, String leftStr, String rightStr) {
        left = new LeftGate(opMode.getHardwareMap().servo.get(leftStr));
        right = new RightGate(opMode.getHardwareMap().servo.get(rightStr));
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

    @Override
    public void stop() {}

    public static class LeftGate extends StatefulServo<LeftGate.State> {
        LeftGate(Servo servo) {
            super(servo);
        }
        enum State implements StatefulServo.State {
            FOUNDATION(.58), OPEN(0), CLOSED(0.45);
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

    public static class RightGate extends StatefulServo<RightGate.State> {
        RightGate(Servo servo) {
            super(servo);
        }
        enum State implements StatefulServo.State {
            FOUNDATION(.44), OPEN(.9), CLOSED(0.6);
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
}
