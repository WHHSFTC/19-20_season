package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

public class RightSideArm extends SideArm {
    private OpModeIF opMode;
    public RightSideArm(OpModeIF opMode, String armStr, String clawStr) {
        this.opMode = opMode;
        arm = new Arm(opMode.getHardwareMap().servo.get(armStr));
        claw = new Claw(opMode.getHardwareMap().servo.get(clawStr));
    }
    public static class Arm implements Mechanism<SideArm.Arm.State>, SideArm.Arm {
        // the servo that controls the arm
        public StatefulServo<RightSideArm.Arm.State> servo;
        // constructor
        Arm(Servo servo) {
            this.servo = new StatefulServo<RightSideArm.Arm.State>(servo) {};
        }

        public void setState(SideArm.Arm.State state) {
            // converts the generic SideArm.Arm.State to RightSideArm.Arm.State
            servo.setState(Enum.valueOf(RightSideArm.Arm.State.class, state.toString()));
        }

        @Override
        public SideArm.Arm.State getState() {
            // converts the RightSideArm.Arm.State to SideArm.Arm.State
            return Enum.valueOf(SideArm.Arm.State.class, servo.getState().toString());
        }

        public enum State implements StatefulServo.State {
            UP(0.62), HOLD(0.4), DOWN(.18); //todo positions
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
        public StatefulServo<RightSideArm.Claw.State> servo;
        Claw(Servo servo) {
            this.servo = new StatefulServo<RightSideArm.Claw.State>(servo) {};
        }
        public void setState(SideArm.Claw.State state) {
            // converts the generic SideArm.Claw.State to RightSideArm.Claw.State
            servo.setState(Enum.valueOf(RightSideArm.Claw.State.class, state.toString()));
        }

        @Override
        public SideArm.Claw.State getState() {
            // converts the RightSideArm.Claw.State to SideArm.Claw.State
            return Enum.valueOf(SideArm.Claw.State.class, servo.getState().toString());
        }
        public enum State implements StatefulServo.State {
            OPEN(0.52), CLOSED(0.05);
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
