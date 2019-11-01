package org.firstinspires.ftc.teamcode.interfaces;

public interface Sensor<T> {
    // get value
    public T getValue();
    // add listener to changes
    public void addListener(SensorListener<T> listener);
}