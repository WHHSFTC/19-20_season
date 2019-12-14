package org.firstinspires.ftc.teamcode.implementations;

import org.firstinspires.ftc.teamcode.interfaces.Mechanism;

public abstract class SideArm {
    public Arm arm;
    public Claw claw;
    public interface Arm extends Mechanism<SideArm.Arm.State> {
        enum State {
            UP, HOLD, DOWN;
        }
    }
    public interface Claw extends Mechanism<SideArm.Claw.State> {
        enum State {
            OPEN, CLOSED;
        }
    }
}
