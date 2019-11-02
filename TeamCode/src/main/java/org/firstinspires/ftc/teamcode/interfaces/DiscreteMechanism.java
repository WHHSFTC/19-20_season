package org.firstinspires.ftc.teamcode.interfaces;

public interface DiscreteMechanism<T> {
    public T getState();
    public void setState(T state);
}
