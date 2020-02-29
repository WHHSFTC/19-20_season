package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.drive.Drive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.implementations.Alliance;
import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.firstinspires.ftc.teamcode.implementations.VisionFromWall;
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * Created by khadija on 2/29/2020.
 */
@Autonomous(name = "Doundle Foundation",group = "test")
public class FlyStoneation extends Auto {
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
                    bot.driveTrain.goAngle(16, DriveTrain.LOADING_ZONE, 0.25);
                    break;
                case TWO_FIVE:
                    bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
                    bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
                    bot.driveTrain.goAngle(4,DriveTrain.LOADING_ZONE,0.25);
                    bot.driveTrain.goAngle(41-bot.ROBOT_LENGTH,bot.opponents_side,0.5);
                    break;
                default:
                    //do nothing
                    bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
                    bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
                    bot.driveTrain.goAngle(3,DriveTrain.BUILDING_ZONE,0.5);
                    bot.driveTrain.goAngle(42-bot.ROBOT_LENGTH,bot.opponents_side,0.5);
            }

            // intake stone
            intake();
            bot.driveTrain.goAngle(pos.getDistance() + 45, DriveTrain.BUILDING_ZONE, 0.5);

            bot.sideArm.arm.setState(SideArm.Arm.State.HOLD);
            sleep(200);

            bot.driveTrain.goAngle(20, DriveTrain.BUILDING_ZONE,0.5);

            // output stone onto foundation
            bot.driveTrain.goAngle(13,bot.opponents_side,0.5);
            bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
            bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
            sleep(100);
            bot.sideArm.arm.setState(SideArm.Arm.State.UP);
            sleep(100);

            bot.driveTrain.goAngle(15,bot.our_side,0.5);
            bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
            bot.driveTrain.align(DriveTrain.LOADING_ZONE);

            // go back to second stone
            bot.driveTrain.goAngle(pos.getDistance() + (65+28), DriveTrain.LOADING_ZONE, 0.5);
            bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
            bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
            intake();
            // go to second output on foundation
            bot.driveTrain.goAngle(5,bot.our_side,0.5);
            bot.driveTrain.goAngle(pos.getDistance() + 72, DriveTrain.BUILDING_ZONE, 0.5);
            bot.sideArm.arm.setState(SideArm.Arm.State.HOLD);
            sleep(200);
            bot.driveTrain.goAngle(24, DriveTrain.BUILDING_ZONE,0.5);

            bot.driveTrain.goAngle(15,bot.opponents_side,0.5);
            bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);
            bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
            sleep(100);
            bot.sideArm.arm.setState(SideArm.Arm.State.UP);
            sleep(100);

            bot.driveTrain.goAngle(10,bot.our_side,0.5);
            bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);


          /*  // pivot foundation
            bot.driveTrain.align(bot.opponents_side, StrafingDriveTrain.Side.BACK);
            bot.driveTrain.goAngle(13, bot.opponents_side, 0.25);
            bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
            bot.driveTrain.goArc(15.0, 90.0, bot.alliance == Alliance.RED ? -90.0: 90.0, 1.0, 6.0);
            bot.driveTrain.goAngle(5.0, bot.opponents_side, .5);
            bot.driveTrain.goAngle(14.0, DriveTrain.BUILDING_ZONE, .5);
           */
          bot.driveTrain.goAngle(40,DriveTrain.LOADING_ZONE,1);
        }
        private void intake() {
            // turn so sidearm faces stones
            bot.driveTrain.align(DriveTrain.LOADING_ZONE);

            // lines up sidearm
            //bot.driveTrain.goAngle(2.0, DriveTrain.LOADING_ZONE, .5);

            // moves forward to be line with stone
            bot.driveTrain.goAngle(13.0, bot.opponents_side, .75);

            // claw closes on stone
            bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

            sleep(250);

            bot.driveTrain.goAngle(13,bot.our_side,0.25);
        }
        /*private void output(double towards, double back, double direction) {
            bot.driveTrain.goAngle(towards, direction, 0.25);
            bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);
            sleep(500);
            bot.sideArm.arm.setState(SideArm.Arm.State.UP);
            bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);
            bot.driveTrain.goAngle(-back, direction, 0.5);
        }
        */

}
