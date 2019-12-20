package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

public abstract class Auto extends LinearOpMode implements OpModeIF {

    protected Sursum bot;

    @Override
    public void runOpMode() throws InterruptedException {
        // init
        genesis();
        // wait for start
        waitForStart();
        // start
        begin();
        // run
        run();
        // stop
        halt();
    }

    public void genesis() throws InterruptedException {
        bot = new Sursum(this);
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);
        bot.init();
    }

    public void begin() throws InterruptedException {
        bot.start();
    }

    public abstract void run() throws InterruptedException;

    public void halt() throws InterruptedException {
        bot.stop();
    }

    @Override
    public Telemetry getTelemetry() {
        return telemetry;
    }

    @Override
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }
}
