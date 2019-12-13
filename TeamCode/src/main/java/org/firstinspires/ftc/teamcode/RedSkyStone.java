package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@Autonomous(name = "RedSkyStone", group = "Auto")
public class RedSkyStone extends LinearOpMode {
    private Sursum bot;

    @Override
    /**
     * SET UP AT POSITION 1 (IN FRONT OF SKY_STONE) WITH FLYWHEELS POINTING TOWARDS STONES
     */
    public void runOpMode() throws InterruptedException {

        // creating bot
        bot = new Sursum(this);

        // initialization
        bot.init(Sursum.Alliance.RED);

        // set global heading
        bot.driveTrain.setHeading(DriveTrain.BLUE_SIDE);

        waitForStart();

        // setting bot zero power settings to brake so that when we get hit by another bot we don't move along with them
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);

        // auto to drop sky-stones begins
        bot.skystoneFoundationRed();
        // auto ends

        // shuts down tensor-flow
        bot.visionTF.shutdown();

        // stops all bot actions
        bot.stop();
    }
}
