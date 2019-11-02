package org.firstinspires.ftc.teamcode.interfaces;

@FunctionalInterface
public interface SensorListener<T> {
    public void onChange(T oldValue, T newValue);
}
