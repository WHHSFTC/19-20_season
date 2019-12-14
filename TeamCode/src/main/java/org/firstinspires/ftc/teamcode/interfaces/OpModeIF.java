package org.firstinspires.ftc.teamcode.interfaces;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface OpModeIF {
    HardwareMap getHardwareMap();
    Telemetry getTelemetry();
    boolean opModeIsActive();
}
