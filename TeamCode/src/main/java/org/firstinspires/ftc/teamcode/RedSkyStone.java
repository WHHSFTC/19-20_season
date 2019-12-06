package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.RightSideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(name = "RedSkyStone", group = "Auto")
public class RedSkyStone extends LinearOpMode {

    private static final double TILE = 24; // inches
    /**
     * get the distance in inches based on how many tiles are put into param
     * @param number_of_tiles is the number of tiles that the distance is needed for
     * @return total distance of the number of tiles
     */
    public static double tile_distance(double number_of_tiles) {return TILE*number_of_tiles;}

    private Sursum bot;

    private final double POWER = 1.0;

    @Override
    /**
     * SET UP AT POSITION 1 (IN FRONT OF SKY_STONE) WITH FLYWHEELS POINTING TOWARDS STONES
     */
    public void runOpMode() throws InterruptedException {

        // creating bot
        bot = new Sursum(this);

        // initialization
        bot.redInit();

        // set global heading
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);

        telemetry.addLine("Initialization DONE");
        telemetry.update();

        waitForStart();

        // drive towards stones
        bot.driveTrain.goAngle(tile_distance(.8), DriveTrain.BLUE_SIDE, POWER/2);

        telemetry.addLine("Starting TensorFlow Search");
        telemetry.update();

        SkyStonePosition sky_stone_position = bot.findSkystone();

        bot.pick_up_stone(90);

        // TODO FORK IF PARTNER PARKED IN LINE

        // heads back to wall to line up going under skybridge
        bot.driveTrain.goAngle(tile_distance(0.8), bot.our_side, POWER/4);

        // heads to wall to line up
        bot.driveTrain.goAngle(sky_stone_position.getDistance() - bot.SIDE_ARM_OFFSET + tile_distance(3.75), DriveTrain.BUILDING_ZONE, POWER/4);

        // heads back to foundation
        bot.driveTrain.goAngle(tile_distance(0.5), DriveTrain.LOADING_ZONE, POWER/4);

        // drops arm onto foundation
        bot.rightArm.setArmPosition(RightSideArm.Arm.State.DOWN);
        Thread.sleep(1000);

        // drops stone onto foundation
        bot.rightArm.setClawPosition(RightSideArm.Claw.State.OPEN);
        Thread.sleep(1000);

        // raises arm
        bot.rightArm.setArmPosition(RightSideArm.Arm.State.UP);
        Thread.sleep(1000);

        // closes claw so andrew doesn't have to make a new one
        bot.rightArm.setClawPosition(RightSideArm.Claw.State.CLOSED);
        Thread.sleep(100);

        // moving out to turn
        bot.driveTrain.goAngle(bot.ROBOT_LENGTH/2, bot.our_side, POWER/2);

        // turning back to face foundation
        bot.driveTrain.rotate(90);

        // heads back to foundation
        bot.driveTrain.goAngle(bot.ROBOT_LENGTH/2 + 3, bot.opponents_side, POWER/2);

        // activate foundation hooks
        bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);
        Thread.sleep(1000);

        // pulls foundation
        bot.driveTrain.goAngle(tile_distance(2.2), bot.our_side, POWER/2);

        // deactivate foundation hooks
        bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
        Thread.sleep(1000);

        // parking
        bot.driveTrain.goAngle(tile_distance(2), DriveTrain.LOADING_ZONE, POWER);


        // stopping bot
        bot.visionTF.shutdown();
        bot.stop();
    }
}
