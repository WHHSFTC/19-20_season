package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Arm extends StatefulServo<Arm.State> {
    // initializes the hardware
    // servo is provided by StatefulServo<Arm.State>
    Arm(HardwareMap hwmap) {
        servo = hwmap.servo.get("arm");
    }
    // enumerates the directions of the Arm with servo positions
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
