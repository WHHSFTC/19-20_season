package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.implementations.Arm;
import org.firstinspires.ftc.teamcode.implementations.Claw;
import org.firstinspires.ftc.teamcode.implementations.DriveTrain;
import org.firstinspires.ftc.teamcode.implementations.LeftSideArm;
import org.firstinspires.ftc.teamcode.implementations.OutputSlides;
import org.firstinspires.ftc.teamcode.implementations.RightSideArm;
import org.firstinspires.ftc.teamcode.implementations.ShuttleGate;
import org.firstinspires.ftc.teamcode.implementations.SideArm;
import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

@TeleOp(group = "Tele", name = "Tele")
public class SoloTele extends LinearOpMode implements OpModeIF {

    private static final double DEADZONE = 0.05;

    private Sursum bot;
    private boolean turtle;
    private boolean turtleX;

    @Override
    public void runOpMode() {
        bot = new Sursum(this);
        bot.leftArm.arm.setState(SideArm.Arm.State.UP);
        bot.rightArm.arm.setState(SideArm.Arm.State.UP);
        ((LeftSideArm.Claw) bot.leftArm.claw).servo.setPosition(1);
        ((RightSideArm.Claw) bot.rightArm.claw).servo.setPosition(0);
        waitForStart();
        while (opModeIsActive()) {
            driveDriveTrain();
            driveInput();
            driveOutput();
            if(gamepad1.dpad_down) bot.shuttleGate.setState(ShuttleGate.State.FOUNDATION);
            if(gamepad1.dpad_up) bot.shuttleGate.setState(ShuttleGate.State.OPEN);
            if(gamepad1.dpad_right) bot.shuttleGate.setState(ShuttleGate.State.CLOSED);
            //if(gamepad2.right_trigger > 0.5) ((OutputSlides) bot.outputSlides).runToZero();
            bot.driveTrain.dumpMotors();
            telemetry.update();
        }
        bot.stop();
    }

    // drives the drivetrain
    private void driveDriveTrain() {
        /*
        if(gamepad1.x && !turtleX) {
            turtle = !turtle;
        }
        turtleX = gamepad1.x;
        telemetry.addData("turtle", turtle);
        */
        double xpow = gamepad1.left_stick_x;
        double ypow = -gamepad1.left_stick_y;
        double zpow = gamepad1.right_stick_x;

        double theta = Math.atan2(ypow, xpow); //angle of joystick
        double power = Math.pow(Math.max(Math.abs(xpow), Math.abs(ypow)), 2); //logarithmic drive
        // ternaries for dead-zone logic
        xpow = Math.abs(xpow) > DEADZONE ? xpow : 0;
        ypow = Math.abs(ypow) > DEADZONE ? ypow : 0;
        zpow = Math.abs(zpow) > DEADZONE ? zpow : 0;

        double zpower = Math.pow(Math.abs(zpow),2);
        double x = Math.cos(theta);
        double y = Math.sin(theta);
        double z = Math.signum(zpow);

//        ((DriveTrain) bot.driveTrain).setPowers(
//                power * (y-x) + zpower * z,
//                power * (-y-x) + zpower * z,
//                power * (-y+x) + zpower * z,
//                power * (y+x) + zpower * z );

        ((DriveTrain) bot.driveTrain).setPowers(
                power * -(y-x) + zpower * z,
                power * -(-y-x) + zpower * z,
                power * -(-y+x) + zpower * z,
                power * -(y+x) + zpower * z );

        // offset of pi/4 makes wheels strafe correctly at cardinal and intermediate directions

        telemetry.addData("xpow", xpow);
        telemetry.addData("zpow", zpow);
        telemetry.addData("ypow", ypow);
        telemetry.addData("theta", theta);
    }

    // drives the output
    private void driveOutput() {

        if(gamepad1.left_bumper) bot.arm.setState(Arm.State.OUT);
        if(gamepad1.right_bumper) bot.arm.setState(Arm.State.RIGHT);
        if(gamepad1.dpad_left) bot.arm.setState(Arm.State.BELT);

        // X toggles claw servos
        if(gamepad1.x && (bot.claw.getState() == Claw.State.OPEN)) bot.claw.setState(Claw.State.CLOSED);
        if(gamepad1.x && (bot.claw.getState() == Claw.State.CLOSED)) bot.claw.setState(Claw.State.OPEN);

        bot.outputSlides.setState((double) gamepad1.right_trigger);
        bot.outputSlides.setState((-1.0) * gamepad1.left_trigger);
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

    @Override
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    @Override
    public Telemetry getTelemetry() {
        return telemetry;
    }
}
