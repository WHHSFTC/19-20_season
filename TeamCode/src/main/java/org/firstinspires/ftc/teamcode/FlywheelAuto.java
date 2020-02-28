package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.drive.Drive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.implementations.Alliance;
import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;

@Autonomous(name = "Flywheel intake", group = "Auto")
public class FlywheelAuto extends Auto {
    @Override
    public void run() throws InterruptedException {
        // goes out to push first stone away
        bot.driveTrain.goAngle(50-bot.ROBOT_WIDTH/2, bot.opponents_side, 0.5);

        // intakes second stone
        bot.flywheels.setPower(-2.0/3);
        bot.belt.setPower(1);
        bot.driveTrain.goAngle(8, DriveTrain.LOADING_ZONE, 0.25);
        sleep(1000);
        bot.flywheels.setPower(0);

        // withdraws
        bot.driveTrain.goAngle(4, DriveTrain.BUILDING_ZONE, 0.25);
        bot.driveTrain.goAngle(4 + bot.ROBOT_WIDTH/2, bot.our_side, 0.5);

        // goes to foundation and hooks on
        bot.driveTrain.align(bot.our_side);
        bot.driveTrain.goAngle(70, DriveTrain.BUILDING_ZONE, 0.5);
        bot.driveTrain.goAngle(6, bot.opponents_side, 0.25);
        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);

        // arc to move foundation
        switch (bot.alliance) {
            case BLUE:
                bot.driveTrain.goArc(15.0, 90.0, 90.0, 1.0, 6.0);
                break;
            case RED:
                bot.driveTrain.goArc(15.0, 90.0, -90.0, 1.0, 6.0);
        }

        // ram foundation into wall
        bot.driveTrain.goAngle(14, DriveTrain.BUILDING_ZONE, .5);

        // output
        bot.shuttleGate.setState(ShuttleGate.State.OPEN);
        sleep(1000);

        // go back for third stone from bridge
        bot.driveTrain.goAngle(72, DriveTrain.LOADING_ZONE, 0.5);
        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
        bot.driveTrain.goAngle(8 + bot.ROBOT_WIDTH/2, bot.opponents_side, 0.25);

        // intake
        bot.flywheels.setPower(-2.0/3);
        bot.driveTrain.goAngle(8, DriveTrain.LOADING_ZONE, 0.25);
        sleep(1000);
        bot.flywheels.setPower(0);

        // withdraw
        bot.driveTrain.goAngle(4, DriveTrain.BUILDING_ZONE, 0.25);
        bot.driveTrain.goAngle(4 + bot.ROBOT_WIDTH/2, bot.our_side, 0.5);

        // go to foundation
        bot.driveTrain.align(DriveTrain.LOADING_ZONE);
        bot.driveTrain.goAngle(80, DriveTrain.BUILDING_ZONE, 0.5);

        // output
        bot.shuttleGate.setState(ShuttleGate.State.OPEN);
        sleep(1000);

        // park, close gate
        bot.driveTrain.goAngle(45, DriveTrain.LOADING_ZONE, 0.5);
        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
    }
}
