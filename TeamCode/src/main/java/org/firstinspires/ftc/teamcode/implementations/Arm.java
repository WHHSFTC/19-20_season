package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Arm extends StatefulServo<Arm.State> {
    Arm(HardwareMap hwmap) {
        servo = hwmap.servo.get("arm");
    }
    enum State implements StatefulServo.State {
        OUT(1), LEFT(2/3), IN(1/3), RIGHT(0);
        double value;
        State(double value) {
                          this.value = value;
                                             }
        public double getPosition() {
                                  return value;
                                               }
    }
}
