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
        VisionFromWall.Position position = bot.pipeline.getPOSITION();
        SkyStonePosition pos = bot.translateRelativePosition(position);
        switch (pos) {
            case ONE_FOUR:
                bot.driveTrain.goAngle(18, DriveTrain.LOADING_ZONE, 0.25);
                break;
            case TWO_FIVE:
                bot.driveTrain.goAngle(10, DriveTrain.LOADING_ZONE, 0.25);
                break;
            case THREE_SIX:
                bot.driveTrain.goAngle(2, DriveTrain.LOADING_ZONE, 0.25);
        }
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
        bot.driveTrain.goAngle(48 - Sursum.ROBOT_WIDTH, bot.opponents_side, 0.5);
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
        sleep(500);
        bot.driveTrain.goAngle(8, bot.our_side, 0.25);
    }
}