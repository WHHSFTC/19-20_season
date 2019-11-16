package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism;
import org.firstinspires.ftc.teamcode.interfaces.Mechanism;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;

// the bot
public class Sursum {
    public static final double ROBOT_WIDTH = 17.75;
    public static final double ROBOT_LENGTH = 17.75;
    // declarations
    public StrafingDriveTrain driveTrain;
    public Mechanism shuttleGate;
    public ContinuousMechanism outputSlides;
    public Mechanism arm;
    public Mechanism claw;
    public CRServo flywheels;
    public DcMotor belt;
    public DistanceSensor ods_back;
    public DistanceSensor ods_front;
    // initialization
    public Sursum(LinearOpMode opMode) {
        driveTrain = new DriveTrain(opMode);
        shuttleGate = new ShuttleGate(opMode);

        // output {{{
        outputSlides = new OutputSlides(opMode);
        arm = new Arm(opMode);
        claw = new Claw(opMode);
        // }}}

        // intake {{{
        flywheels = new Flywheels(opMode);
        belt = opMode.hardwareMap.dcMotor.get("belt");
        // }}}

        // sensors {{{
        ods_back = opMode.hardwareMap.get(DistanceSensor.class, "odsback"); //Foundation Hooks
        ods_front = opMode.hardwareMap.get(DistanceSensor.class, "odsfront"); //Flywheels
        // }}}
    }
    public void init() {
        driveTrain.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.setModes(DcMotor.RunMode.RUN_USING_ENCODER);
        shuttleGate.setState(ShuttleGate.State.CLOSED);
        arm.setState(Arm.State.BELT);
        claw.setState(Claw.State.OPEN);
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
