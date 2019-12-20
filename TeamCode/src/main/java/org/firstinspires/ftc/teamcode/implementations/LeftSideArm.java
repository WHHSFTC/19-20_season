package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

public class LeftSideArm extends SideArm {
    private OpModeIF opMode;
    public LeftSideArm(OpModeIF opMode, String armStr, String clawStr) {
        this.opMode = opMode;
        arm = new Arm(opMode.getHardwareMap().servo.get(armStr));
        claw = new Claw(opMode.getHardwareMap().servo.get(clawStr));
    }

    public static class Arm implements Mechanism<SideArm.Arm.State>, SideArm.Arm {
        // the servo that controls the arm
        public StatefulServo<LeftSideArm.Arm.State> servo;
        // constructor
        Arm(Servo servo) {
            this.servo = new StatefulServo<State>(servo) {};
        }

        public void setState(SideArm.Arm.State state) {
            // converts the generic SideArm.Arm.State to LeftSideArm.Arm.State
            servo.setState(Enum.valueOf(State.class, state.toString()));
        }

        @Override
        public SideArm.Arm.State getState() {
            // converts the LeftSideArm.Arm.State to SideArm.Arm.State
            return Enum.valueOf(SideArm.Arm.State.class, servo.getState().toString());
        }

        public enum State implements StatefulServo.State {
            UP(0.36), HOLD(0.56), DOWN(0.79);
            private double position;
            State(double position) {
                this.position = position;
            }
            @Override
            public double getPosition() {
                return position;
            }
        }

        @Override
        public void stop() {
            servo.stop();
        }
    }
    public static class Claw implements Mechanism<SideArm.Claw.State>, SideArm.Claw {
        public StatefulServo<LeftSideArm.Claw.State> servo;
        Claw(Servo servo) {
            this.servo = new StatefulServo<State>(servo) {};
        }
        public void setState(SideArm.Claw.State state) {
            // converts the generic SideArm.Claw.State to LeftSideArm.Claw.State
            servo.setState(Enum.valueOf(Claw.State.class, state.toString()));
        }

        @Override
        public SideArm.Claw.State getState() {
            // converts the LeftSideArm.Claw.State to SideArm.Claw.State
            return Enum.valueOf(SideArm.Claw.State.class, servo.getState().toString());
        }
        public enum State implements StatefulServo.State {
            OPEN(0.49), CLOSED(.89);
            double position;
            State(double position) {
                this.position = position;
            }
            @Override
            public double getPosition() {
                return position;
            }
        }

        @Override
        public void stop() {
            servo.stop();
        }
    }
}
