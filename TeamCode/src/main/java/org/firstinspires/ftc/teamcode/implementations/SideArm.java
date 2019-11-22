package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class SideArm {
    public Arm arm;
    public Claw claw;
    private LinearOpMode opMode;
    public SideArm(LinearOpMode opMode, String armStr, String clawStr) {
        this.opMode = opMode;
        arm = new Arm(opMode.hardwareMap.servo.get(armStr));
        claw = new Claw(opMode.hardwareMap.servo.get(clawStr));
    }
    public static class Arm extends StatefulServo<Arm.State> {
        Arm(Servo servo) {
            super(servo);
        }
        public enum State implements StatefulServo.State {
            UP(1), DOWN(0);
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
            OPEN(1), CLOSE(0);
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
