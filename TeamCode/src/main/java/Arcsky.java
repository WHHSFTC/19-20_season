import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.VisionFromWall;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * Created by khadija on 2/29/2020.
 */
@Autonomous(name = "ArcSky", group = "test")
public class Arcsky extends Auto {
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
                bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
                bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
                //bot.driveTrain.goArc();
                break;
            case TWO_FIVE:
                bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
                bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
                bot.driveTrain.goAngle(4, DriveTrain.LOADING_ZONE, 0.25);
                bot.driveTrain.goAngle(41 - bot.ROBOT_LENGTH, bot.opponents_side, 0.5);
                break;
            default:
                //do nothing
                bot.driveTrain.goArc(19.0, 125.0,180,0.75,3.75);
                bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
                bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);

        }

    }
}
