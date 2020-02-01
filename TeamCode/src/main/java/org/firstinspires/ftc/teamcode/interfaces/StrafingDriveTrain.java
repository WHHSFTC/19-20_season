package org.firstinspires.ftc.teamcode.interfaces;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

public interface StrafingDriveTrain {
    // rotate about center angle degrees counterclockwise
    void rotate(double angle);
    void align(double angle, Side side);

    // defaults to front
    void align(double angle);
    enum Side {
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
    void goAngle(double dist, double angle, double power);

    // strafe following vector
    void goVector(double x, double y, double power);
    void goArc(double distance, double frontAngle, double turnAngle, double power, double time) throws InterruptedException;

    // begins driving at angle
    void startAngle(double angle, double power);

    // log to telemetry
    void dumpMotors();
    void stop();
    void halt();
    void setModes(DcMotor.RunMode mode);
    void setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior behavior);
    void setHeading(double angle);

    // required for splines
    void setMotorPowers(double rf, double lf, double lb, double rb);
    List<Double> getWheelPositions();
    List<Double> getWheelVelocities();
    double getRawExternalHeading();
    void turn(double angle);
    void turnSync(double angle);
    Pose2d getLastError();
    void update();
    boolean isBusy();
    void waitForRest();
    TrajectoryBuilder trajectoryBuilder();
    void followTrajectory(Trajectory trajectory);
    void followTrajectorySync(Trajectory trajectory);
}
