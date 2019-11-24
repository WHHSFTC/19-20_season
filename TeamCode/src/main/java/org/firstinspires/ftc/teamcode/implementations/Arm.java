package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.interfaces.Mechanism;

public class Arm implements Mechanism<Arm.State> {
    public void stop() {}
    private Elbow elbow;
    private Wrist wrist;
    private State state;

    Arm(LinearOpMode opMode, String elbowStr, String wristStr) {
        elbow = new Elbow(opMode.hardwareMap.servo.get(elbowStr));
        wrist = new Wrist(opMode.hardwareMap.servo.get(wristStr));
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        elbow.setState(state.getElbow());
        wrist.setState(state.getWrist());
        this.state = state;
    }

    public enum State {
        BELT(Elbow.State.IN, Wrist.State.BELT), IN(Elbow.State.OUT, Wrist.State.IN), LEFT(Elbow.State.OUT, Wrist.State.LEFT), OUT(Elbow.State.OUT, Wrist.State.OUT), RIGHT(Elbow.State.OUT, Wrist.State.RIGHT);
        private Elbow.State elbow;
        private Wrist.State wrist;
        State(Elbow.State elbow, Wrist.State wrist) {
            this.elbow = elbow;
            this.wrist = wrist;
        }

        public Elbow.State getElbow() {
            return elbow;
        }

        public Wrist.State getWrist() {
            return wrist;
        }
    }
}

class Elbow extends StatefulServo<Elbow.State> {
    Elbow(Servo servo) {
        super(servo);
    }
    // enumerates the directions of the Arm with servo positions
    // StatefulServo requires getPosition()
    enum State implements StatefulServo.State {
        IN(0.7), OUT(0.23);
        private double value;
        State(double value) {
            this.value = value;
        }
        public double getPosition() {
            return value;
        }
    }
}
class Wrist extends StatefulServo<Wrist.State> {
    Wrist(Servo servo) {
        super(servo);
    }
    enum State implements StatefulServo.State {
        // 0-1 is 270 degrees, so each 1/3 is 90 degrees apart
        OUT(1), RIGHT(0.68), IN(0.38), LEFT(0.09), BELT(0.25);
        private double value;
        State(double value) {
            this.value = value;
        }
        public double getPosition() {
            return value;
        }
    }
}
