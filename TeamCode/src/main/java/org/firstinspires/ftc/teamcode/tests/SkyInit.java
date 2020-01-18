package org.firstinspires.ftc.teamcode.tests;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

/**
 * Created by khadija on 1/17/2020.
 */
public class SkyInit extends Auto {
    VuforiaStuff.skystonePos pos;
    int stoneDiff;
    @Override
    public void run() {

        // finding position of sky stone
        switch(pos){
            case RIGHT:
                bot.driveTrain.goAngle(53, bot.opponents_side,.5);
                bot.driveTrain.align(DriveTrain.LOADING_ZONE);
                stoneDiff = 0;
                break;
            case CENTER:
                bot.driveTrain.goAngle(41,bot.opponents_side,.5);
                bot.driveTrain.goAngle(8,DriveTrain.LOADING_ZONE,.5);
                bot.driveTrain.align(DriveTrain.LOADING_ZONE);
                stoneDiff = 12;
                break;
            case LEFT:
                bot.driveTrain.goAngle(41,bot.opponents_side,.5);
                bot.driveTrain.goAngle(16,DriveTrain.LOADING_ZONE,.5);
                bot.driveTrain.align(DriveTrain.LOADING_ZONE);
                stoneDiff = 15;
                break;
        }
        // servo calls
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        // intaking the skystone
        intakeSkystone();

        // heads back to go under skybridge
        bot.driveTrain.goAngle(12, bot.our_side, .5);

        // goes 36 inches into building zone
        bot.driveTrain.goAngle(stoneDiff + 36, DriveTrain.BUILDING_ZONE, .5);

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
        bot.driveTrain.goAngle(stoneDiff + (24 + 36), DriveTrain.LOADING_ZONE, .75);

        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        sleep(250);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // driving to get the stone
        bot.driveTrain.goAngle(14, bot.opponents_side, .25);
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        sleep(250);

        // heads back with stone
        bot.driveTrain.goAngle(14, bot.our_side, .5);
        bot.driveTrain.align(DriveTrain.LOADING_ZONE);
        bot.driveTrain.goAngle(stoneDiff + 24 + 36, DriveTrain.BUILDING_ZONE, .25);

        // drop stone
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        sleep(250);

        // bring up arm
        bot.sideArm.arm.setState(SideArm.Arm.State.UP);
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        // adjustment
        bot.driveTrain.goAngle(2, bot.opponents_side, .75);

        // park
        bot.driveTrain.goAngle(16, DriveTrain.LOADING_ZONE, .75);




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
