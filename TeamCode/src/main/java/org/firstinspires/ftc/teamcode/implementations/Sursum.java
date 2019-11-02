package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;
import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;

// the bot
public class Sursum {
    // declarations
    public StrafingDriveTrain driveTrain;
    public ContinuousMechanism outputSlides;
    public Mechanism arm;
    public Mechanism wrist;
    public Mechanism claw;
    public CRServo flywheels;
    public DcMotor belt;
    // initialization
    public Sursum(HardwareMap hwmap) {
        driveTrain = new DriveTrain(hwmap);

        // output {{{
        outputSlides = new OutputSlides(hwmap);
        arm = new Arm(hwmap);
        wrist = new Wrist(hwmap);
        claw = new Claw(hwmap);
        // }}}

        // intake {{{
        flywheels = new Flywheels(hwmap);
        belt = hwmap.dcMotor.get("belt");
        // }}}
    }
}
