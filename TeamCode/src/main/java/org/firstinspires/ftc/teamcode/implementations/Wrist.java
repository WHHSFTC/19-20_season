package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Wrist extends StatefulServo<Wrist.State> {
    // another stateful servo subclass
    Wrist(HardwareMap hwmap) {
        servo = hwmap.servo.get("wrist");
    }

    // enumerates the directions of the Wrist with servo positions
    // StatefulServo requires getPosition()
    enum State implements StatefulServo.State {
        // directions and their corresponding servo positions as defined by Andrew
	// 0-1 is 270 degrees, so each 1/3 is 90 degrees apart
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
