package org.firstinspires.ftc.teamcode.interfaces;

public interface ContinuousMechanism {
    // set the value
    public void setValue(double value) throws IllegalArgumentException;
    // get the value
    public double getValue();
    // get the max
    public double getMax();
    // get the min
    public double getMin();
}
