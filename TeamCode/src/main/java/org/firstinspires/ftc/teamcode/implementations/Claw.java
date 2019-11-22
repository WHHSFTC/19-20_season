package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.interfaces.Mechanism;

public class Claw implements Mechanism<Claw.State> {
    private State state;
    // inner claw is closer to wrist
    private InnerClaw innerClaw;
    // outer claw farther from wrist
    private OuterClaw outerClaw;

    public Claw(LinearOpMode opMode, String innerStr, String outerStr) {
        innerClaw = new InnerClaw(opMode.hardwareMap.servo.get(innerStr));
        outerClaw = new OuterClaw(opMode.hardwareMap.servo.get(outerStr));
    }

    public void setState(State state) throws IllegalArgumentException {
        innerClaw.setState(state.getInner());
        outerClaw.setState(state.getOuter());
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public enum State {
        OPEN(InnerClaw.State.OPEN, OuterClaw.State.OPEN), CLOSED(InnerClaw.State.CLOSED, OuterClaw.State.CLOSED);
        private InnerClaw.State inner;
        private OuterClaw.State outer;
        State(InnerClaw.State inner, OuterClaw.State outer) {
            this.inner = inner;
            this.outer = outer;
        }

        public InnerClaw.State getInner() {
            return inner;
        }

        public OuterClaw.State getOuter() {
            return outer;
        }
    }
    public void stop() {}

    static class InnerClaw extends StatefulServo<InnerClaw.State> {
        InnerClaw(Servo servo) {
            super(servo);
        }
        enum State implements StatefulServo.State {
            OPEN(1), CLOSED(0.45);
            private double value;
            State(double value) {
                this.value = value;
            }
            public double getPosition() {
                return value;
            }
        }
    }
    static class OuterClaw extends StatefulServo<OuterClaw.State> {
        OuterClaw(Servo servo) {
            super(servo);
        }
        enum State implements StatefulServo.State {
            OPEN(0), CLOSED(0.58);
            private double value;
            State(double value) {
                this.value = value;
            }
            public double getPosition() {
                return value;
            }
        }
    }
}

