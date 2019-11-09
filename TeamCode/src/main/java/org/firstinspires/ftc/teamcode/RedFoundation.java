package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(group = "Auto", name = "RedFoundation")
public class RedFoundation extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(hardwareMap, telemetry, this);
        bot.init();
        telemetry.addData("Start", "Back side to the wall.");
        waitForStart();
        bot.driveTrain.goVector(0, 48, 1);
        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);
        bot.driveTrain.goVector(0,-48, 1);
        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
        bot.driveTrain.goVector(27,0,1);
        bot.stop();
    }
}
