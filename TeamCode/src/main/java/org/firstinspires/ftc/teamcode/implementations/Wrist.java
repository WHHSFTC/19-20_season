package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Wrist extends StatefulServo<Wrist.State> {
    Wrist(HardwareMap hwmap) {
        servo = hwmap.servo.get("wrist");
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
