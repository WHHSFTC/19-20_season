package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.implementations.Arm;
import org.firstinspires.ftc.teamcode.implementations.Claw;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.OutputSlides;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@TeleOp(group = "Tele", name = "Tele")
public class Tele extends LinearOpMode {
    private static final double DEADZONE = 0.05;
    private Sursum bot;
    private boolean turtle;
    private boolean turtleX;
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
            telemetry.update();
        }
        bot.stop();
    }

    // drives the drivetrain
    private void driveDriveTrain() {
        if(gamepad1.x && !turtleX) {
            turtle = !turtle;
        }
        double xpow = gamepad1.left_stick_x;
        double ypow = -gamepad1.left_stick_y;
        double zpow = gamepad1.right_stick_x;
        // ternaries for dead-zone logic
        xpow = Math.abs(xpow) > DEADZONE ? xpow : 0;
        ypow = Math.abs(ypow) > DEADZONE ? ypow : 0;
        zpow = Math.abs(zpow) > DEADZONE ? zpow : 0;
        double theta = Math.atan2(ypow, xpow); //angle of joystick
        double power = Math.pow(Math.max(Math.abs(xpow), Math.abs(ypow)), 2); //logarithmic drive
        power = turtle ? power/3 : power;
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

        ((DriveTrain) bot.driveTrain).setPowers(
                power * y + zpow,
                power * -x + zpow,
                power * -y + zpow,
                power * x + zpow
        );
    }

    // drives the output
    private void driveOutput() {
        if(gamepad2.dpad_down) bot.arm.setState(Arm.State.IN);
        if(gamepad2.dpad_left) bot.arm.setState(Arm.State.LEFT);
        if(gamepad2.dpad_up) bot.arm.setState(Arm.State.OUT);
        if(gamepad2.dpad_right) bot.arm.setState(Arm.State.RIGHT);
        if(gamepad2.left_bumper) bot.claw.setState(Claw.State.CLOSED);
        if(gamepad2.right_bumper) bot.claw.setState(Claw.State.OPEN);
        bot.outputSlides.setState(gamepad2.left_stick_y >= DEADZONE ? (double) gamepad2.left_stick_y/2 : 0);
        ((OutputSlides) bot.outputSlides).dumpEncoders(telemetry);
    }

    // drives the input
    private void driveInput() {
        if(gamepad2.a) {
            bot.flywheels.setPower(1);
            bot.belt.setPower(1);
        }
        if(gamepad2.y) {
            bot.flywheels.setPower(-1);
            bot.belt.setPower(-1);
        }
        if(gamepad2.x) {
            bot.flywheels.setPower(0);
            bot.belt.setPower(0);
        }
    }
}
