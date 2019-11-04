package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;

public class DriveTrain implements StrafingDriveTrain {
    private DcMotor motorRF;
    private DcMotor motorLF;
    private DcMotor motorLB;
    private DcMotor motorRB;
    public DriveTrain(HardwareMap hwmap) {
        motorRF = hwmap.dcMotor.get("motorRF");
        motorLF = hwmap.dcMotor.get("motorLF");
        motorLB = hwmap.dcMotor.get("motorLB");
        motorRB = hwmap.dcMotor.get("motorRB");
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

    public void setPowers(double rf, double lf, double lb, double rb) {
        motorRF.setPower(rf);
        motorLF.setPower(lf);
        motorLB.setPower(lb);
        motorRB.setPower(rb);
    }
}
