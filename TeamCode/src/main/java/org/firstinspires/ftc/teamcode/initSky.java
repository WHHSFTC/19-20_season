package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

/**
 * Created by khadija on 12/27/2019.
 */
@Disabled
@Autonomous(name = "InitSky", group = "Auto")
public class initSky extends Auto {
    public OpenCvCamera Cam;
    public Customdetect skyStoneDetector;


    String skystonePos = "none";
    double xPos = 2.0;
    @Override
    public void run() {
        /*
     * Instantiate an OpenCvCamera object for the camera we'll be using.
     * In this sample, we're using the phone's internal camera. We pass it a
     * CameraDirection enum indicating whether to use the front or back facing
     * camera, as well as the view that we wish to use for camera monitor (on
     * the RC phone). If no camera monitor is desired, use the alternate
     * single-parameter constructor instead (commented out below)
     */
        int cameraMonitorViewId = bot.opMode.getHardwareMap().appContext.getResources().getIdentifier("cameraMonitorViewId", "id", bot.opMode.getHardwareMap().appContext.getPackageName());
        Cam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View
        //phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK);

        /*
         * Open the connection to the camera device
         */
        Cam.openCameraDevice();


        /*
         * Specify the image processing pipeline we wish to invoke upon receipt
         * of a frame from the camera. Note that switching pipelines on-the-fly
         * (while a streaming session is in flight) *IS* supported.
         */
       // skyStoneDetector.useDefaults();

        skyStoneDetector = new Customdetect();
        Cam.setPipeline(skyStoneDetector);

        /*
         * Tell the camera to start streaming images to us! Note that you must make sure
         * the resolution you specify is supported by the camera. If it is not, an exception
         * will be thrown.
         *
         * Also, we specify the rotation that the camera is used in. This is so that the image
         * from the camera sensor can be rotated such that it is always displayed with the image upright.
         * For a front facing camera, rotation is defined assuming the user is looking at the screen.
         * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
         * away from the user.
         */
        Cam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);


        while (bot.opMode.opModeIsActive()) {
            xPos = skyStoneDetector.foundRectangle().x;

            if (xPos <= -2.0) { //TODO Tune these numbers
                skystonePos= "left";
            } else if (xPos > -2.0 && xPos < 2.0) {
                skystonePos = "center";
            } else if  (xPos >= 2.0) {
                skystonePos = "right";
            }

            bot.opMode.getTelemetry().addData("xPos: ", xPos);
            bot.opMode.getTelemetry().addData("SkyStone Position: ", skystonePos);
            bot.opMode.getTelemetry().update();
        }

        Cam.stopStreaming();


        while (opModeIsActive()) {
            if (skystonePos == "left") {
                bot.driveTrain.goAngle(50,bot.opponents_side,1);
                bot.driveTrain.rotate(90);
                bot.driveTrain.align(90);
                bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
                bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
                bot.driveTrain.goAngle(3,180,.5);
                bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
                bot.driveTrain.goAngle(53,bot.our_side,.5);
            } else if (skystonePos == "center") {
                bot.driveTrain.goAngle(50,bot.opponents_side,1);
                bot.driveTrain.rotate(90);
                bot.driveTrain.align(90);
                bot.driveTrain.goAngle(12,DriveTrain.LOADING_ZONE,.5);
                bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
                bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
                bot.driveTrain.goAngle(3,180,.5);
                bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
                bot.driveTrain.goAngle(53,bot.our_side,.5);
            } else if (skystonePos == "right") {
                bot.driveTrain.goAngle(50,bot.opponents_side,1);
                bot.driveTrain.rotate(90);
                bot.driveTrain.align(90);
                bot.driveTrain.goAngle(24,DriveTrain.LOADING_ZONE,.5);
                bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
                bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
                bot.driveTrain.goAngle(3,180,.5);
                bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
                bot.driveTrain.goAngle(53,bot.our_side,.5);
            }
            bot.opMode.getTelemetry().update();
        }
    }
    @Override
    public void halt() throws InterruptedException {
        super.halt();
    }
}
