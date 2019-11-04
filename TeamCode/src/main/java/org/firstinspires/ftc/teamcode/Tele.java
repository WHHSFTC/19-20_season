package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@TeleOp(group = "Tele", name = "Tele")
public class Tele extends LinearOpMode {
    private static final double DEADZONE = 0.05;
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(hardwareMap);
        bot.init();
        waitForStart();
        while (opModeIsActive()) {
            driveDriveTrain();
            driveInput();
            driveOutput();
            bot.driveTrain.dumpMotors();
            driveOutput();
        }
        bot.stop();
    }

    // drives the drivetrain
    private void driveDriveTrain() {
        double xpow = gamepad1.left_stick_x;
        double ypow = -gamepad1.left_stick_y;
        double zpow = gamepad1.right_stick_x;
        // ternaries for dead-zone logic
        xpow = Math.abs(xpow) > DEADZONE ? xpow : 0;
        ypow = Math.abs(ypow) > DEADZONE ? ypow : 0;
        zpow = Math.abs(zpow) > DEADZONE ? zpow : 0;
        double theta = Math.atan2(ypow, xpow); //angle of joystick
        double power = Math.pow(Math.max(Math.abs(xpow),Math.abs(ypow)),2); //logarithmic drive
        // offset of pi/4 makes wheels strafe correctly at cardinal and intermediate directions
        double cos = Math.cos(theta - Math.PI / 4);
        double sin = Math.sin(theta - Math.PI / 4);
        //eliminates incorrect signs resulting from double precision
        if(Math.abs(cos)<.0000001){
            cos=0;
        }
        if(Math.abs(sin)<.0000001){
            sin=0;
        }
        double x = Math.signum(cos);
        double y = Math.signum(sin);

        ((DriveTrain) bot.driveTrain).setPowers(power * y + zpow, power * -x + zpow, power * -y + zpow, power * x + zpow);
    }

    // drives the output
    private void driveOutput() {

    }

    // drives the input
    private void driveInput() {

    }
}
