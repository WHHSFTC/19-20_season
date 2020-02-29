package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.firstinspires.ftc.teamcode.implementations.VisionFromWall;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "Testing Vision", group = "Auto")
public class VisionTest extends Auto {
    @Override
    public void genesis() throws InterruptedException {
        super.genesis();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        bot.camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        bot.camera.openCameraDevice();

        bot.pipeline = new VisionFromWall(bot, telemetry);
        bot.camera.setPipeline(bot.pipeline);

        bot.camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

    }

    @Override
    public void run() throws InterruptedException {
        VisionFromWall.Position position = bot.pipeline.getPosition();
        SkyStonePosition pos = bot.translateRelativePosition(position);
        bot.camera.stopStreaming();
        bot.camera.closeCameraDevice();
        telemetry.addData("Position", pos.toString());
        switch (pos) {
            case ONE_FOUR:
                bot.driveTrain.goAngle(16, DriveTrain.LOADING_ZONE, 0.25);
                break;
            case TWO_FIVE:
                bot.driveTrain.goAngle(8, DriveTrain.LOADING_ZONE, 0.25);
                break;
            default:
                //do nothing
        }
        // intake stone
        intake(48 - Sursum.ROBOT_WIDTH, 8, bot.opponents_side);
        bot.driveTrain.goAngle(pos.getDistance() + 84, DriveTrain.BUILDING_ZONE, 0.5);
        // output stone onto foundation
        output(8, 8, bot.opponents_side);

    }
    private void intake(double towards, double back, double direction) {
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
        bot.driveTrain.goAngle(towards, direction, 0.5);
        sleep(500);
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
        sleep(500);
        bot.driveTrain.goAngle(-back, direction, 0.25);
        bot.sideArm.arm.setState(SideArm.Arm.State.HOLD);
    }
    private void output(double towards, double back, double direction) {
        bot.driveTrain.goAngle(towards, direction, 0.25);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
        sleep(500);
        bot.sideArm.arm.setState(SideArm.Arm.State.UP);
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
        bot.driveTrain.goAngle(-back, direction, 0.5);
    }
}