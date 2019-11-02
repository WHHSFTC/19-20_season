package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;

public class DriveTrain implements StrafingDriveTrain {
    public DriveTrain(HardwareMap hwmap) {

    }
    // turns about center of robot
    @Override
    public void rotate(double angle) {

    }

    // strafes at polar vector
    @Override
    public void goAngle(double dist, double angle, double power) {

    }

    // follows arc
    @Override
    public void goArc(double centerX, double centerY, double angle, double power) {

    }

    // strafes at cartesian vector
    @Override
    public void goVector(double x, double y, double power) {

    }

    @Override
    public void dumpMotors() {

    }
}
