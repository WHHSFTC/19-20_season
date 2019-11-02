package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.interfaces.Mechanism;

public class Claw implements Mechanism<Claw.State> {
    private State state;
    private InnerClaw innerClaw;
    private OuterClaw outerClaw;

    public Claw(HardwareMap hwmap) {
        innerClaw = new InnerClaw(hwmap);
        outerClaw = new OuterClaw(hwmap);
    }

    public void setState(State state) throws IllegalArgumentException {
        innerClaw.setState(state.getInner());
        outerClaw.setState(state.getOuter());
        this.state = state;
    }

    public State getState() {
        return state;
    }

    enum State {
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
}

class InnerClaw extends StatefulServo<InnerClaw.State> {
    InnerClaw(HardwareMap hwmap) {
        servo = hwmap.servo.get("inner");
    }
    enum State implements StatefulServo.State {
        OPEN(0), CLOSED(0.5);
        double value;
        State(double value) {
            this.value = value;
        }
        public double getPosition() {
            return value;
        }
    }
}
class OuterClaw extends StatefulServo<OuterClaw.State> {
    OuterClaw(HardwareMap hwmap) {
        servo = hwmap.servo.get("outer");
    }
    enum State implements StatefulServo.State {
        OPEN(0.5), CLOSED(0);
        double value;
        State(double value) {
            this.value = value;
        }
        public double getPosition() {
            return value;
        }
    }
}
