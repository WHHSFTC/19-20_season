package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

public class Claw implements Mechanism<Claw.State> {
    private State state;
    // inner claw is closer to wrist
    private InnerClaw innerClaw;
    // outer claw farther from wrist
    private OuterClaw outerClaw;

    public Claw(OpModeIF opMode, String innerStr, String outerStr) {
        innerClaw = new InnerClaw(opMode.getHardwareMap().servo.get(innerStr));
        outerClaw = new OuterClaw(opMode.getHardwareMap().servo.get(outerStr));
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
        OPEN(InnerClaw.State.OPEN, OuterClaw.State.OPEN),
        CLOSED(InnerClaw.State.CLOSED, OuterClaw.State.CLOSED),
        INNER(InnerClaw.State.OPEN, OuterClaw.State.CLOSED);
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

        @Override
        public State getInit() {
            return State.OPEN;
        }

        enum State implements StatefulServo.State {
            OPEN(0.4), CLOSED(0.56);
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

        @Override
        public State getInit() {
            return State.FIT;
        }

        enum State implements StatefulServo.State {
            OPEN(0.61), CLOSED(0.5), FIT(0.0);
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