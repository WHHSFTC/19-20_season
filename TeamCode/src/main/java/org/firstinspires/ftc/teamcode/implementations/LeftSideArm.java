package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

public class LeftSideArm {
    public Arm arm;
    public Claw claw;
    private OpModeIF opMode;
    public LeftSideArm(OpModeIF opMode, String armStr, String clawStr) {
        this.opMode = opMode;
        arm = new Arm(opMode.getHardwareMap().servo.get(armStr));
        claw = new Claw(opMode.getHardwareMap().servo.get(clawStr));
    }

    public void setArmPosition(LeftSideArm.Arm.State state) {
        arm.setPosition(state.getPosition());
    }

    public void setClawPosition(LeftSideArm.Claw.State state) {
        claw.setPosition(state.getPosition());
    }

    public static class Arm extends StatefulServo<Arm.State> {
        Arm(Servo servo) {
            super(servo);
        }
        public enum State implements StatefulServo.State {
            UP(0.36), HOLD(0.56), DOWN(0.79); //todo positions
            double position;
            State(double position) {
                this.position = position;
            }
            @Override
            public double getPosition() {
                return position;
            }
        }
    }
    public static class Claw extends StatefulServo<Claw.State> {
        Claw(Servo servo) {
            super(servo);
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
    }

}
