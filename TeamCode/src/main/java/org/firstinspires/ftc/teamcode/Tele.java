package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.implementations.Arm;
import org.firstinspires.ftc.teamcode.implementations.Claw;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.OutputSlides;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.Sursum;

@TeleOp(group = "Tele", name = "Tele")
public class Tele extends LinearOpMode {
    private static final double DEADZONE = 0.05;
    private Sursum bot;
    private boolean turtle;
    private boolean turtleX;
    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        bot.init();
        bot.leftArm.arm.setPosition(1);
        bot.rightArm.arm.setPosition(.86);
        bot.leftArm.claw.setPosition(.94);
        bot.rightArm.claw.setPosition(0);
        waitForStart();
        while (opModeIsActive()) {
            driveDriveTrain();
            driveInput();
            driveOutput();
            if(gamepad1.dpad_down) bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);
            if(gamepad1.dpad_up) bot.shuttleGate.setState(ShuttleGate.State.OPEN);
            if(gamepad1.dpad_left || gamepad1.dpad_right) bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
            bot.driveTrain.dumpMotors();
            telemetry.update();
        }
        bot.stop();
    }

  //// drives the drivetrain
  //private void driveDriveTrain() {
  //    if(gamepad1.x && !turtleX) {
  //        turtle = !turtle;
  //    }
  //    turtleX = gamepad1.x;
  //    telemetry.addData("turtle", turtle);
  //    double xpow = gamepad1.left_stick_x;
  //    double ypow = -gamepad1.left_stick_y;
  //    double zpow = gamepad1.right_stick_x;

  //    double theta = Math.atan2(ypow, xpow); //angle of joystick
  //    double power = Math.pow(Math.max(Math.abs(xpow), Math.abs(ypow)), 2); //logarithmic drive
  //    // ternaries for dead-zone logic
  //    xpow = Math.abs(xpow) > DEADZONE ? xpow : 0;
  //    ypow = Math.abs(ypow) > DEADZONE ? ypow : 0;
  //    zpow = Math.abs(zpow) > DEADZONE ? zpow : 0;

  //    double zpower = Math.pow(Math.abs(zpow),2);
  //    double x = Math.cos(theta);
  //    double y = Math.sin(theta);
  //    double z = Math.signum(zpow);

  //    ((DriveTrain) bot.driveTrain).setPowers(
  //            power * (y-x) - zpower * z,
  //            power * (-y-x) - zpower * z,
  //            power * (-y+x) - zpower * z,
  //            power * (y+x) - zpower * z );

  //    // offset of pi/4 makes wheels strafe correctly at cardinal and intermediate directions

  //    telemetry.addData("xpow", xpow);
  //    telemetry.addData("zpow", zpow);
  //    telemetry.addData("ypow", ypow);
  //    telemetry.addData("theta", theta);

  //}
    // drives the drivetrain
    private void driveDriveTrain() {
        if(gamepad1.x && !turtleX) {
            turtle = !turtle;
        }
        telemetry.addData("turtle", turtle);
        turtleX = gamepad1.x;
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
                power * -y + zpow,
                power * x + zpow,
                power * y + zpow,
                power * -x + zpow
        );
    }

    // drives the output
    private void driveOutput() {
        if(gamepad2.dpad_down) bot.arm.setState(Arm.State.IN);
        if(gamepad2.dpad_left) bot.arm.setState(Arm.State.LEFT);
        if(gamepad2.dpad_up) bot.arm.setState(Arm.State.OUT);
        if(gamepad2.dpad_right) bot.arm.setState(Arm.State.RIGHT);
        if(gamepad2.b) bot.arm.setState(Arm.State.BELT);
        if(gamepad2.left_bumper) bot.claw.setState(Claw.State.CLOSED);
        if(gamepad2.right_bumper) bot.claw.setState(Claw.State.OPEN);
        bot.outputSlides.setState(Math.abs(gamepad2.left_stick_y) >= DEADZONE ? (double) gamepad2.left_stick_y : 0);
        ((OutputSlides) bot.outputSlides).dumpEncoders();
    }

    // drives the input
    private void driveInput() {
        if(gamepad1.a) {
            bot.flywheels.setPower(-2.0/3);
            bot.belt.setPower(1);
        }
        if(gamepad1.y) {
            bot.flywheels.setPower(2.0/3);
            bot.belt.setPower(-1);
        }
        if(gamepad1.b) {
            bot.flywheels.setPower(0);
            bot.belt.setPower(0);
        }
    }
}
