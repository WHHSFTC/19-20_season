package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

public abstract class Auto extends OpMode implements OpModeIF {
    private boolean active;
    protected Sursum bot;
    protected Sursum.Alliance alliance;

    @Override
    public void init() {
        alliance = bot.init();
    }
    @Override
    public void start() { bot.start(); }
    @Override
    public void loop() {}
    @Override
    public void stop() {
        bot.driveTrain.halt();
    }
}
