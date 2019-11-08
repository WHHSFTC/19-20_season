package org.firstinspires.ftc.teamcode.interfaces;

// S is the type of the state
public interface Mechanism<S> {
    // get and set the state
    public S getState();
    public void setState(S state) throws IllegalArgumentException;
    public void stop();
}
