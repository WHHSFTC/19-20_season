package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Summum;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

@Disabled
@Autonomous(name = "AlignTest", group = "Test")
public class AlignTest extends LinearOpMode implements OpModeIF {
    private Summum bot;
    @Override
    public void runOpMode() {
        bot = new Summum(this);
        bot.init();
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);
        waitForStart();
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);
        bot.driveTrain.align(DriveTrain.BLUE_SIDE);
        bot.stop();
    }

    @Override
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    @Override
    public Telemetry getTelemetry() {
        return telemetry;
    }
}
