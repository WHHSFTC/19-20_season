package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.RightSideArm;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(name = "SkyStone", group = "Auto")
public class SkyStone extends Auto {
    private Sursum bot;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        // drive towards stones
        bot.driveTrain.goAngle( 41-Sursum.ROBOT_LENGTH, DriveTrain.BLUE_SIDE, .25);

        bot.opMode.getTelemetry().addLine("Starting TensorFlow Search");
        bot.opMode.getTelemetry().update();
        SkyStonePosition skyStonePosition;
        try {
            skyStonePosition = bot.findSkystone();
        } catch (InterruptedException ex) {
            telemetry.addLine(ex.getMessage());
            telemetry.update();
            stop();
            return;
        }

        intakeSkystone();

        // heads back to go under skybridge
        bot.driveTrain.goAngle(12, bot.our_side, .25);
        bot.driveTrain.goAngle(skyStonePosition.getDistance() + 32, DriveTrain.BUILDING_ZONE, 1);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

//        driveTrain.goAngle(14, opponents_side, 1.0/4);

        // drops stone onto foundation
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
        sleep(500);

        // raises arm
        bot.sideArm.arm.setState(SideArm.Arm.State.UP);
        // closes claw so andrew doesn't have to make a new one
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
//        driveTrain.goAngle(6, our_side, 0.25);
        bot.driveTrain.goAngle(8, DriveTrain.LOADING_ZONE, 0.25);
//        // moving out to turn
//        driveTrain.goAngle(12, our_side, 1.0);
//
//        // turning back to face foundation
//        driveTrain.align(our_side);
//
//        // heads back to foundation
//        driveTrain.goAngle(14, opponents_side, 1.0/2);
//
//        // activate foundation hooks
//        shuttleGate.setState(ShuttleGate.State.FOUNDATION);
//        Thread.sleep(500);
//
//        // pulls foundation
//        driveTrain.goAngle(52, our_side, 1.0);
//
//        // deactivate foundation hooks
//        shuttleGate.setState(ShuttleGate.State.CLOSED);
//        Thread.sleep(500);
//
//        // parking
//        driveTrain.goAngle(54, DriveTrain.LOADING_ZONE, 0.5);
//

        // stopping bot
        stop();
    }

    private void intakeSkystone() {
        // backing up
        bot.driveTrain.goAngle(10, bot.our_side, .5);

        // turn so sidearm faces stones
        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // lines up sidearm
        switch (bot.alliance) {
            case RED:
                bot.driveTrain.goAngle(Sursum.SIDEARM_Y + Sursum.CAMERA_X, DriveTrain.BUILDING_ZONE, .25);
                break;
            case BLUE:
                bot.driveTrain.goAngle(-Sursum.SIDEARM_Y + Sursum.CAMERA_X, DriveTrain.BUILDING_ZONE, .25);
        }

        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        // moves forward to be line with stone
        bot.driveTrain.goAngle(20, bot.opponents_side,.5);

        // claw closes on stone
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
        sleep(500);

        // holding stone
        bot.sideArm.arm.setState(SideArm.Arm.State.HOLD);
        sleep(500);

        telemetry.addLine("Holding Stone");
        telemetry.update();
    }

    @Override
    public void stop() {
        super.stop();
        bot.visionTF.shutdown();
    }
}
