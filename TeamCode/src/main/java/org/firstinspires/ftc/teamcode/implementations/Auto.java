package org.firstinspires.ftc.teamcode.implementations;

public abstract class Auto extends OpModeRunner {
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
//        FtcDashboard dashboard = FtcDashboard.getInstance();
//        this.telemetry = dashboard.getTelemetry();
        bot = new Summum(this);
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);
        bot.init();
        telemetry.update();
    }

    public void begin() throws InterruptedException {
        bot.start();
    }

    public abstract void run() throws InterruptedException;

    public void halt() throws InterruptedException {
        bot.stop();
    }
}
