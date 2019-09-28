package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveTrain {
    public DcMotor motorLF, motorLB, motorRB, motorRF;
    DriveTrain(HardwareMap hwmap) {
        motorLF = hwmap.dcMotor.get("motorLF");
        motorLB = hwmap.dcMotor.get("motorLB");
        motorRB = hwmap.dcMotor.get("motorRB");
        motorRF = hwmap.dcMotor.get("motorRF");
    }
    public void setDrivePowers(double lb, double lf, double rf, double rb) {
        motorLB.setPower(lb);
        motorLF.setPower(lf);
        motorRF.setPower(rf);
        motorRB.setPower(rb);
    }
}
