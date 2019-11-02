package org.firstinspires.ftc.teamcode.interfaces;

// functional interfaces can be supplied using anonymous clases, lambdas, or method references
@FunctionalInterface
public interface SensorListener<T> {
    public void onChange(T oldValue, T newValue);
}
