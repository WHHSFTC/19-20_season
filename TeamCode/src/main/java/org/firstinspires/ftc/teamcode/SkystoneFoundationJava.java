package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.firstinspires.ftc.teamcode.implementations.VisionTF;

/**
 * Created by khadija on 1/29/2020.
 */
@Autonomous(name = "One Sky Stone and move Foundation : Park near bridge", group = "Auto")
public class SkystoneFoundationJava extends Auto {

    @Override
    public void genesis() throws InterruptedException {
        super.genesis();
        bot.visionTF = new VisionTF(this, "Webcam 1");
    }

    @Override
    public void run() throws InterruptedException {
        // servo calls
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        // preparing arm to grab
        bot.driveTrain.goAngle(41 - Sursum.ROBOT_LENGTH, bot.opponents_side, .5);

        bot.opMode.getTelemetry().addLine("Starting TensorFlow Search");
        bot.opMode.getTelemetry().update();

        // finding position of sky stone
        SkyStonePosition skyStonePosition;

        try {
            skyStonePosition = bot.findSkystone();
        } catch (InterruptedException ex) {
            telemetry.addLine(ex.getMessage());
            telemetry.update();
            requestOpModeStop();
            return;
        }

        // intaking the skystone
        intakeSkystone();

        // heads back to go under skybridge
        bot.driveTrain.goAngle(14.0, bot.our_side, .5);

        // goes 36 inches into building zone
        //If we keep losing time, then change the 65 to 70 and take out the other line
        bot.driveTrain.goAngle(skyStonePosition.getDistance() + 65, DriveTrain.BUILDING_ZONE, .5);

        bot.sideArm.arm.setState(SideArm.Arm.State.HOLD);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        bot.driveTrain.goAngle(14, bot.opponents_side, .5);

        // drops stone
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);

        sleep(250);

        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        sleep(100);

        bot.sideArm.arm.setState(SideArm.Arm.State.UP);

        sleep(250);

        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        bot.driveTrain.goAngle(10, bot.our_side, .75);
        // If we keep losing time, then take out this next line
        bot.driveTrain.goAngle(5,DriveTrain.BUILDING_ZONE,1);

        bot.driveTrain.align(bot.our_side);

        bot.driveTrain.goAngle(16, bot.opponents_side, .5);

        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);

        sleep(250);

        switch(bot.alliance) {
            case BLUE:
                bot.driveTrain.goArc(15, 90, 90, 0.75);
                break;
            case RED:
                bot.driveTrain.goArc(15, 90, -90, 0.75);
        }

        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);

        bot.driveTrain.goAngle(24.0, DriveTrain.BUILDING_ZONE, .5);

        bot.driveTrain.goAngle(24.0, bot.our_side, .5);


        bot.driveTrain.goAngle(48.0, DriveTrain.LOADING_ZONE, .5);

    }

    private void intakeSkystone() {
        // turn so sidearm faces stones
        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // lines up sidearm
        bot.driveTrain.goAngle(2.0, DriveTrain.LOADING_ZONE, .5);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // moves forward to be line with stone
        bot.driveTrain.goAngle(13.0, bot.opponents_side, .75);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // claw closes on stone
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        sleep(250);
    }

}
