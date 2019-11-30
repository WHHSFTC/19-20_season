package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(name = "RedSkyStone", group = "Auto")
public class RedSkyStone extends LinearOpMode {
    private Sursum bot;
    @Override
    public void runOpMode() throws InterruptedException {
        bot = new Sursum(this);
        bot.init();
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);
        waitForStart();
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);
//        bot.driveTrain.goAngle(-bot.vision.getSkyStone(DriveTrain.BUILDING_ZONE), DriveTrain.BUILDING_ZONE, .5);
        bot.driveTrain.goAngle(45-bot.ROBOT_LENGTH, DriveTrain.BLUE_SIDE, .5);
        SkyStonePosition position = bot.findSkystone();
        bot.driveTrain.goAngle(18, DriveTrain.RED_SIDE, .25);
        bot.driveTrain.rotate(90);
        bot.driveTrain.goAngle(bot.SIDEARM_Y + bot. CAMERA_X, DriveTrain.BUILDING_ZONE, .25);
        bot.rightArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.rightArm.claw.setState(SideArm.Claw.State.OPEN);
        bot.driveTrain.goAngle(20 + (bot.ROBOT_LENGTH - bot.ROBOT_WIDTH)/2, DriveTrain.BLUE_SIDE, .25);
        bot.rightArm.claw.setState(SideArm.Claw.State.CLOSED);
        Thread.sleep(1000);
        bot.rightArm.arm.setState(SideArm.Arm.State.HOLD);
        bot.driveTrain.goAngle(8, DriveTrain.RED_SIDE, .25);
        bot.driveTrain.goAngle(position.getDistance() + 72, DriveTrain.BUILDING_ZONE, .25);
        bot.driveTrain.goAngle(8, DriveTrain.BLUE_SIDE, .25);
        bot.rightArm.claw.setState(SideArm.Claw.State.OPEN);
        Thread.sleep(1000);
        bot.rightArm.claw.setState(SideArm.Claw.State.CLOSED);
        bot.driveTrain.goAngle(8, DriveTrain.RED_SIDE, .5);
        bot.driveTrain.goAngle(position.getDistance() + 96, DriveTrain.LOADING_ZONE, .25);
        bot.rightArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.driveTrain.goAngle(8, DriveTrain.BLUE_SIDE, .25);
        bot.rightArm.claw.setState(SideArm.Claw.State.CLOSED);
        Thread.sleep(1000);
        bot.rightArm.arm.setState(SideArm.Arm.State.HOLD);
        bot.driveTrain.goAngle(8, DriveTrain.RED_SIDE, .25);
        bot.driveTrain.goAngle(position.getDistance() + 96, DriveTrain.LOADING_ZONE, .25);
        bot.driveTrain.goAngle(8, DriveTrain.BLUE_SIDE, .25);
        bot.rightArm.claw.setState(SideArm.Claw.State.OPEN);
        bot.stop();
    }
}
