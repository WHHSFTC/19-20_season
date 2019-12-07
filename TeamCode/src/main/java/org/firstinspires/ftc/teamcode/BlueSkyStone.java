package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.RightSideArm;
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(name = "BlueSkyStone", group = "Auto")
public class BlueSkyStone extends LinearOpMode {
    private Sursum bot;

    @Override
    /**
     * SET UP AT POSITION 1 (IN FRONT OF SKY_STONE) WITH FLYWHEELS POINTING TOWARDS STONES
     */
    public void runOpMode() throws InterruptedException {

        // creating bot
        bot = new Sursum(this);

        // initialization
        bot.init(Sursum.Alliance.BLUE);

        // set global heading
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);

        waitForStart();
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);

        bot.skystoneFoundationBlue();

        // stopping bot
        bot.visionTF.shutdown();
        bot.stop();
    }
}
