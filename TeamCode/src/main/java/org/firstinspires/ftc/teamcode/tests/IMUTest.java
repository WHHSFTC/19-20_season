package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Summum;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

@Disabled
@TeleOp(name = "IMUTest", group = "Test")
public class IMUTest extends LinearOpMode implements OpModeIF {
    private Summum bot;
    @Override
    public void runOpMode() throws InterruptedException {
        bot = new Summum(this);
        bot.init();
        bot.driveTrain.setHeading(DriveTrain.LOADING_ZONE);
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT);
        telemetry.addData("raw", ((DriveTrain) bot.driveTrain).getRawHeading());
        telemetry.addData("heading", ((DriveTrain) bot.driveTrain).getHeading());
        telemetry.update();
        Orientation headings;
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("raw", ((DriveTrain) bot.driveTrain).getRawHeading());
            telemetry.addData("heading", ((DriveTrain) bot.driveTrain).getHeading());
            headings = ((DriveTrain) bot.driveTrain).getRawHeadings();
            telemetry.addData("first", headings.firstAngle);
            telemetry.addData("second", headings.secondAngle);
            telemetry.addData("third", headings.thirdAngle);
            telemetry.update();
        }
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
