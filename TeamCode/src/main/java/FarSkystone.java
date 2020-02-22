import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.firstinspires.ftc.teamcode.implementations.VisionTF;

/**
 * Created by khadija on 2/22/2020.
 */
@Autonomous (name = "Far Skystone", group = "Test")
public class FarSkystone extends Auto {
    @Override
    public void genesis() throws InterruptedException {
        super.genesis();
        bot.visionTF = new VisionTF(this, "Webcam 1");
    }

    @Override
    public void run() throws InterruptedException {

        //moves the bot to the far skystones
        bot.driveTrain.goAngle(60,DriveTrain.LOADING_ZONE,0.5);
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
        bot.driveTrain.goAngle(41, bot.our_side, .5);

        // moving the arm to hold position
        bot.sideArm.arm.setState(SideArm.Arm.State.HOLD);

        // goes 36 inches into building zone
        bot.driveTrain.goAngle(skyStonePosition.getDistance() +115 , DriveTrain.BUILDING_ZONE, .5);



        // alignment
//        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // moving to side of foundation to drop stone on it
        bot.driveTrain.align(bot.opponents_side);
        bot.driveTrain.goAngle(8,DriveTrain.BUILDING_ZONE,0.5);

        // drops stone
        bot.sideArm.arm.setState(SideArm.Arm.State.DOWN);

        sleep(250);

        bot.sideArm.claw.setState(SideArm.Claw.State.OPEN);

        sleep(100);

        bot.sideArm.arm.setState(SideArm.Arm.State.UP);

        sleep(250);

        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);


        bot.driveTrain.goAngle(50, DriveTrain.LOADING_ZONE, .5);
    }

    private void intakeSkystone() {
        // turn so sidearm faces stones
//        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // lines up sidearm
//        bot.driveTrain.goAngle(2.0, DriveTrain.LOADING_ZONE, .5);

        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // moves forward to be line with stone
        bot.driveTrain.goAngle(13.0, bot.opponents_side, .75);

//        bot.driveTrain.align(DriveTrain.LOADING_ZONE);

        // claw closes on stone
        bot.sideArm.claw.setState(SideArm.Claw.State.CLOSED);

        sleep(250);
    }
}
