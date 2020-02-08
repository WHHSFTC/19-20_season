package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.implementations.Auto;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.VisionWall;

@Autonomous(name = "Vision From Wall Test", group = "Auto")
public class VisionWallTest extends Auto {
    @Override
    public void genesis() throws InterruptedException {
        super.genesis();
        bot.visionTest = new VisionWall(this, bot.alliance, "Webcam 1");
    }

    @Override
    public void run() throws InterruptedException {
        SkyStonePosition sky = bot.visionTest.findSkystone();
        telemetry.addLine("SkyStone in position: " + sky.getPosition());
        telemetry.update();
        switch(sky) {
            case ONE_FOUR:
                bot.driveTrain.goAngle(10, DriveTrain.BUILDING_ZONE, .5);
                break;
            case TWO_FIVE:
                bot.driveTrain.goAngle(10, bot.opponents_side, .5);
                break;
            case THREE_SIX:
                bot.driveTrain.goAngle(10, DriveTrain.LOADING_ZONE, .5);
                break;
        }
    }
}
