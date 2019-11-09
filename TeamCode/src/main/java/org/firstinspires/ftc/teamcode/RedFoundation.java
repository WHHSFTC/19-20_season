package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(group = "Auto", name = "RedFoundation")
public class RedFoundation extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        bot.init();
        telemetry.addData("Start", "Back side to the wall.");
        waitForStart();
        // drive half the width of the bot to the left to line up with center-ish of foundation
        bot.driveTrain.goVector(bot.ROBOT_WIDTH * -1/2, 0, 1);
        // drive forward 4 feet to the foundation
        bot.driveTrain.goVector(0, 42, 1);
        // drives until foundation
        bot.driveTrain.startAngle(90, 0.25);
        while (bot.ods.getDistance(DistanceUnit.INCH) > 2) {}
        bot.driveTrain.halt();
        // grab the foundation
        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);
        // drive backward 4 feet with the foundation
        bot.driveTrain.goVector(0,-48, 1);
        // release the foundation
        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
        // strafe right to park
        bot.driveTrain.goVector(48,0,1);
        // stop the bot
        bot.stop();
    }
}
