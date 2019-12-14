package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

public abstract class Auto extends OpMode implements OpModeIF {
    private boolean active;
    protected Sursum bot;

    // methods from OpMode:
    @Override
    public void init() {
        bot = new Sursum(this);
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);
        bot.init();
    }

    @Override
    public void start() { bot.start(); }

    @Override
    public void loop() {}

    @Override
    public void stop() {
        bot.stop();
    }

    // methods from OpModeIF:
    @Override
    public boolean opModeIsActive() {
        return active;
    }

    @Override
    public Telemetry getTelemetry() {
        return telemetry;
    }

    @Override
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    // helper methods:
    protected void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            telemetry.addLine(ex.getMessage());
            telemetry.update();
        }
    }
}
