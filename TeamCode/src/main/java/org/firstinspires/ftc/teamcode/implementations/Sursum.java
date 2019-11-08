package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;
import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;

// the bot
public class Sursum {
    // declarations
    public StrafingDriveTrain driveTrain;
    public Mechanism shuttleGate;
    public ContinuousMechanism outputSlides;
    public Mechanism arm;
    public Mechanism claw;
    public CRServo flywheels;
    public DcMotor belt;
    // initialization
    public Sursum(HardwareMap hwmap, Telemetry telemetry, OpModeIF opMode) {
        driveTrain = new DriveTrain(hwmap, telemetry, opMode);
        shuttleGate = new ShuttleGate(hwmap);

        // output {{{
        outputSlides = new OutputSlides(hwmap);
        arm = new Arm(hwmap);
        claw = new Claw(hwmap);
        // }}}

        // intake {{{
        flywheels = new Flywheels(hwmap);
        belt = hwmap.dcMotor.get("belt");
        // }}}
    }
    public void init() {

    }
    public void stop() {
        driveTrain.stop();
        shuttleGate.stop();
        outputSlides.stop();
        arm.stop();
        claw.stop();
        flywheels.setPower(0);
        belt.setPower(0);
    }
}
