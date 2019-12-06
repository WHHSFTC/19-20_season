package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
@Disabled
@Autonomous(name = "RedDepotBridgeStone", group = "Auto")
public class RedDepotBridgeStone extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() throws InterruptedException {

        bot = new Sursum(this);

        bot.init();

        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);

        waitForStart();

        // displace the first stone
        bot.driveTrain.goVector(60, 0, 1);

        // line up with quarry
        bot.driveTrain.goVector(-10-bot.ROBOT_WIDTH/2, 0, 1);

        // intake
        bot.flywheels.setPower(1);

        bot.belt.setPower(1);

        bot.driveTrain.goVector(0, 5, 1);

        Thread.sleep(500);

        bot.flywheels.setPower(0);

        bot.belt.setPower(0);

        // drive to foundation
        bot.driveTrain.goVector(-4-bot.ROBOT_WIDTH, 0, 1);

        bot.driveTrain.goVector(0, -83-bot.ROBOT_WIDTH, 1);

        bot.driveTrain.rotate(90);

        // open gate and hook foundation
        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);

        // output
        bot.belt.setPower(1);

        Thread.sleep(2000);

        bot.belt.setPower(0);

        // move foundation
        bot.driveTrain.goVector(-48, 0, 1);

        // release foundation
        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);

        // park
        bot.driveTrain.goVector(0, 48, 1);

        // stop the bot
        bot.stop();
    }
}
