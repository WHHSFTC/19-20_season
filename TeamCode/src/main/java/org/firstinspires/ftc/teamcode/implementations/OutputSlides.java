package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;

// drives slide motors using sensors and encoders
public class OutputSlides implements ContinuousMechanism {
    public OutputSlides(HardwareMap hwmap) {

    }
    @Override
    public void setState(Double value) throws IllegalArgumentException {

    }

    @Override
    public Double getState() {
        return 0.0;
    }

    @Override
    public double getMax() {
        return 0;
    }

    @Override
    public double getMin() {
        return 0;
    }
}
