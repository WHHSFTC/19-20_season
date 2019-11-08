package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.implementations.Sursum;
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF;

@Autonomous(group = "Auto", name = "Red1")
public class Red1 extends LinearOpMode implements OpModeIF {
    private Sursum bot;
    @Override
    public void runOpMode() {
        bot = new Sursum(hardwareMap, telemetry, this);
    }
    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        }catch ()
    }
}
