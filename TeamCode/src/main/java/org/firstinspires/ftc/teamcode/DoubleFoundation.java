package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;

/**
 * Created by khadija on 1/4/2020.
 */
@Autonomous(name ="Double skystone on Foundation", group = "Auto")
public class DoubleFoundation extends Auto {

    @Override
    public void run(){

        // drive towards stones
        bot.driveTrain.goAngle( 41-bot.ROBOT_LENGTH, bot.opponents_side, .5);
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        bot.opMode.getTelemetry().addLine("Starting TensorFlow Search");
        bot.opMode.getTelemetry().update();

        SkyStonePosition skyStonePosition;

        try {
            skyStonePosition = bot.findSkystone();
        }
        catch (InterruptedException ex) {
            telemetry.addLine(ex.getMessage());
            telemetry.update();
            requestOpModeStop();
            return;
        }

        intakeSkystone();

        // heads back to go under skybridge
        bot.driveTrain.goAngle(18, bot.our_side, .5);

        // goes 75 inches into building zone
        bot.driveTrain.goAngle(skyStonePosition.getDistance() + 73, DriveTrain.BUILDING_ZONE, .5);

        bot.sideArm.arm.setState(SideArm.Arm.State.HOLD);
        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        bot.driveTrain.goAngle(7,bot.opponents_side,.5);

        // drops stone
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        sleep(100);

        bot.sideArm.arm.setState(SideArm.Arm.State.UP);
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
        bot.driveTrain.goAngle(8,bot.our_side,.5);




        // second cycle
        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        bot.driveTrain.goAngle(skyStonePosition.getDistance() + (24 + 73), DriveTrain.LOADING_ZONE, .5);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);



        //drops arm and claw
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        sleep(100);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        bot.driveTrain.goAngle(2, bot.our_side, .75);
        // intake second stone
        bot.driveTrain.align(DriveTrain.LOADING_ZONE);
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        bot.driveTrain.goAngle(14, bot.opponents_side, .25);

        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        sleep(100);

        // heads back with stone
        bot.driveTrain.goAngle(14, bot.our_side, .5);
        bot.driveTrain.goAngle(skyStonePosition.getDistance() + 24 + 65, DriveTrain.BUILDING_ZONE, .5);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        bot.sideArm.arm.setState(SideArm.Arm.State.HOLD);

        bot.driveTrain.goAngle(10,bot.opponents_side,.5);


        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
        sleep(250);

        bot.sideArm.arm.setState(SideArm.Arm.State.UP);
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        bot.driveTrain.goAngle(8,bot.our_side,1);

        //bot.driveTrain.goAngle(6, bot.our_side, 1);
        bot.driveTrain.goAngle(50, DriveTrain.LOADING_ZONE, .5);
    }

    private void intakeSkystone(){
        // backing up
        //bot.driveTrain.goAngle(8, bot.our_side, .5);

        // turn so sidearm faces stones
        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // lines up sidearm
        //bot.driveTrain.goAngle(2, DriveTrain.LOADING_ZONE, .5);

        // bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        //bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        // moves forward to be line with stone
        bot.driveTrain.goAngle(15, bot.opponents_side,.75);

        // claw closes on stone
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        sleep(150);
    }

    @Override
    public void halt() throws InterruptedException {
        super.halt();
        bot.visionTF.shutdown();
    }
}
