package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

@Disabled
@Autonomous(name = "ArcTest", group = "Test")
public class ArcTest extends LinearOpMode implements OpModeIF {
    private Sursum bot;
    @Override
    public void runOpMode() throws InterruptedException {
        bot = new Sursum(this);
        bot.init();
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);
        waitForStart();
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);
        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);
        bot.driveTrain.goArc(ArcConstants.distance, ArcConstants.frontAngle, ArcConstants.turnAngle, ArcConstants.power);
        bot.stop();
    }

    @Config
    public static class ArcConstants {
        public static double distance = 14;
        public static double frontAngle = 90;
        public static double turnAngle = 90;
        public static double power = .5;
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
