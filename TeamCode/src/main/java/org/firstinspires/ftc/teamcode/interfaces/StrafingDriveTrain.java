package org.firstinspires.ftc.teamcode.interfaces;

import com.qualcomm.robotcore.hardware.DcMotor;

public interface StrafingDriveTrain {
    // rotate about center angle degrees counterclockwise
    public void rotate(double angle);
    public void align(double angle, Side side);
    // defaults to front
    public void align(double angle);
    public enum Side {
        FRONT(90), BACK(270), LEFT(180), RIGHT(0);
        private double degrees;
        Side(double degrees) {
            this.degrees = degrees;
        }

        public double getDegrees() {
            return degrees;
        }
    }
    // strafe dist distance at angle degrees counterclockwise (relative to bot orientation)
    public void goAngle(double dist, double angle, double power);
    // strafe following vector
    public void goVector(double x, double y, double power);
    // follow arc around (centerX, centerY) for angle degrees counterclockwise
    public void goArc(double centerX, double centerY, double angle, double power);
    // begins driving at angle
    public void startAngle(double angle, double power);
    // log to telemetry
    public void dumpMotors();
    public void stop();
    public void halt();
    public void setModes(DcMotor.RunMode mode);
    public void setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior behavior);
    public void setHeading(double angle);
}
