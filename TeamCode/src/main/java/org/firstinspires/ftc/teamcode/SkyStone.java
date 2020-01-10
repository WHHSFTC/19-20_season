package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.openftc.easyopencv.OpenCvCamera;

@Autonomous(name = "SkyStone", group = "Auto")
public class SkyStone extends Auto {

    @Override
    public void run() {
        // drive towards stones
        bot.driveTrain.goAngle( 41-Sursum.ROBOT_LENGTH, bot.opponents_side, .5);

        // preparing arm to grab
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

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
        bot.driveTrain.goAngle(12, bot.our_side, .5);

        // goes 36 inches into building zone
        bot.driveTrain.goAngle(skyStonePosition.getDistance() + 36, DriveTrain.BUILDING_ZONE, .25);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // drops stone
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        sleep(250);

        // arm up to not get hit by sky bridge
        bot.sideArm.arm.setState(SideArm.Arm.State.UP);
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        // second cycle
        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // heading back to get second sky stone
        bot.driveTrain.goAngle(skyStonePosition.getDistance() + (24 + 36), DriveTrain.LOADING_ZONE, .25);

        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        sleep(250);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // adjustment
        bot.driveTrain.goAngle(2, bot.our_side, .75);

        // driving to get the stone
        bot.driveTrain.goAngle(14, bot.opponents_side, .25);
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        sleep(250);

        // heads back with stone
        bot.driveTrain.goAngle(14, bot.our_side, .5);
        bot.driveTrain.goAngle(skyStonePosition.getDistance() + 24 + 36, DriveTrain.BUILDING_ZONE, .25);

        // drop stone
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        sleep(250);

        // bring up arm
        bot.sideArm.arm.setState(SideArm.Arm.State.UP);
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        // adjustment
        bot.driveTrain.goAngle(2, bot.opponents_side, .5);

        // park
        bot.driveTrain.goAngle(16, DriveTrain.LOADING_ZONE, .5);



        //THIRD CYCLE
 /*       if((skyStonePosition.getPosition() != 1.0)){

            bot.driveTrain.align(DriveTrain.LOADING_ZONE);
            bot.driveTrain.goAngle(skyStonePosition.getDistance() + (24 + 27), DriveTrain.LOADING_ZONE, .75);

            bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
            bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
            sleep(250);

            bot.driveTrain.align(DriveTrain.LOADING_ZONE);

            // intake  stone
            bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
            bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

            bot.driveTrain.goAngle(12, bot.opponents_side, .75);
            bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
            sleep(250);

            // heads back with stone

            bot.driveTrain.goAngle(14, bot.our_side, .75);
            bot.driveTrain.goAngle(skyStonePosition.getDistance() + 24 + 15, DriveTrain.BUILDING_ZONE, 1);

            // drop stone
            bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
            sleep(250);

            bot.sideArm.arm.setState(SideArm.Arm.State.UP);
            bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

            bot.driveTrain.goAngle(2, bot.opponents_side, 1);
            bot.driveTrain.goAngle(16, DriveTrain.LOADING_ZONE, 1);
        }
        else{
            bot.driveTrain.goAngle(2, bot.opponents_side, .5);
            bot.driveTrain.goAngle(16, DriveTrain.LOADING_ZONE, .5);
        }

  */
    }



    private void intakeSkystone() {
        // backing up
        //bot.driveTrain.goAngle(8, bot.our_side, .5);

        // turn so sidearm faces stones
        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // lines up sidearm

       bot.driveTrain.goAngle(2, DriveTrain.LOADING_ZONE, .5);

       // bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        //bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        // moves forward to be line with stone
        bot.driveTrain.goAngle(13, bot.opponents_side,.75);

        // claw closes on stone
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
        sleep(250);
    }

    @Override
    public void halt() throws InterruptedException {
        super.halt();
        bot.visionTF.shutdown();
    }
}
