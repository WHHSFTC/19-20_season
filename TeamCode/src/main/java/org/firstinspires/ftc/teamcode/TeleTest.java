package org.firstinspires.ftc.teamcode;

public class TeleTest extends Robot {
    double deadzone = .05;
    float xPow, yPow, zPow;
    @Override
    public void runOpMode() {
        super.runOpMode();
        boolean in = false, out = false, pIn = false, pOut = false;
        waitForStart();
        while (opModeIsActive()) {
            getJoyStickValues();

            double pwr;
            double m;

            double theta = Math.atan2(yPow, xPow);
            double power = Math.pow(Math.max(Math.abs(xPow), Math.abs(yPow)),2);
            double zPower = Math.pow(Math.abs(zPow),2);
            double x = Math.cos(theta);
            double y = Math.sin(theta);

            double z = Math.signum(zPow);

            if (gamepad2.a) {setWristPosition(Position.INNER);}
            if (gamepad2.y) {setWristPosition(Position.LEFT);}
            if (gamepad2.b) {setWristPosition(Position.RIGHT);}
            if (gamepad2.x) {setWristPosition(Position.OUTER);}

            if (gamepad2.right_bumper && !pOut) {
                out = !out;
                setClawPositionO(out ? ClawPositionOuter.CLOSE : ClawPositionOuter.OPEN);
            }
            if (gamepad2.left_bumper && !pIn) {
                in = !in;
                setClawPositionI(in ? ClawPositionInner.CLOSE : ClawPositionInner.OPEN);
            }
            pIn = gamepad2.left_bumper;
            pOut = gamepad2.right_bumper;

            if (gamepad1.a) {setServoPower(.5);}
            if (gamepad1.b) {setServoPower(0);}
            if (gamepad1.y) {setServoPower(-.5);}


        }
    }
    public void getJoyStickValues() {
        yPow = gamepad1.left_stick_y;
        xPow = gamepad1.left_stick_x;
        zPow = gamepad1.right_stick_x;

        xPow = Math.abs(xPow) < deadzone ? 0 : xPow;
        yPow = Math.abs(yPow) < deadzone ? 0 : yPow;
    }
}
