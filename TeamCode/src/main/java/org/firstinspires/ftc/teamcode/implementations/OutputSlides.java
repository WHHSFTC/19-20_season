package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;

public class OutputSlides implements ContinuousMechanism {
    public OutputSlides(HardwareMap hwmap) {

    }
    @Override
    public void setValue(double value) throws IllegalArgumentException {

    }

    @Override
    public double getValue() {
        return 0;
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
