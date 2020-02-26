package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.implementations.OutputSlides;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

@Disabled
@TeleOp(name = "Push for Encoders", group = "Tele")
public class PushForEncoders extends LinearOpMode implements OpModeIF {
    private Sursum bot;

    @Override
    public void runOpMode() throws InterruptedException {
        initBot();

        telemetry.addLine("Init: [DONE]");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            bot.driveTrain.dumpMotors();
            ((OutputSlides) bot.outputSlides).dumpEncoders();

            telemetry.update();
        }
    }

    private void initBot() {
        bot = new Sursum(this);
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public HardwareMap getHardwareMap() {
        return this.hardwareMap;
    }

    @Override
    public Telemetry getTelemetry() {
        return this.telemetry;
    }
}
