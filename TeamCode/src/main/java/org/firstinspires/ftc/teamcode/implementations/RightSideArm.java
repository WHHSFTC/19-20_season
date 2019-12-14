package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

public class RightSideArm {
    public Arm arm;
    public Claw claw;
    private OpModeIF opMode;
    public RightSideArm(OpModeIF opMode, String armStr, String clawStr) {
        this.opMode = opMode;
        arm = new Arm(opMode.getHardwareMap().servo.get(armStr));
        claw = new Claw(opMode.getHardwareMap().servo.get(clawStr));
    }

    public void setArmPosition(RightSideArm.Arm.State state) {
        arm.setPosition(state.getPosition());
    }

    public void setClawPosition(RightSideArm.Claw.State state) {
        claw.setPosition(state.getPosition());
    }

    public static class Arm extends StatefulServo<Arm.State> {
        Arm(Servo servo) {
            super(servo);
        }
        public enum State implements StatefulServo.State {
            UP(0.62), HOLD(0.4), DOWN(.18); //todo positions
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
    }

}
