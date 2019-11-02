package org.firstinspires.ftc.teamcode.interfaces;

public interface Mechanism<S> {
    public S getState();
    public void setState(S state) throws IllegalArgumentException;
}
