package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxController;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.lang.reflect.Field;

@Autonomous(name = "MotorCurrentTest", group = "Tests")
public class MotorCurrentTest extends LinearOpMode {
    private DcMotor motor0;
    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    @Override
    public void runOpMode() {
        motor0 = hardwareMap.dcMotor.get("0");
        motor1 = hardwareMap.dcMotor.get("1");
        motor2 = hardwareMap.dcMotor.get("2");
        motor3 = hardwareMap.dcMotor.get("3");
        telemetry.addData("0", getCurrent(motor0, LynxGetADCCommand.Channel.MOTOR0_CURRENT));
        telemetry.addData("1", getCurrent(motor1, LynxGetADCCommand.Channel.MOTOR1_CURRENT));
        telemetry.addData("2", getCurrent(motor2, LynxGetADCCommand.Channel.MOTOR2_CURRENT));
        telemetry.addData("3", getCurrent(motor3, LynxGetADCCommand.Channel.MOTOR3_CURRENT));
    }
    private double getCurrent(DcMotor motor, LynxGetADCCommand.Channel channel) {
        try {
            Field f;
            LynxModule hub;
            f = LynxController.class.getDeclaredField("module");
            f.setAccessible(true);
            hub = (LynxModule) f.get((LynxController) motor.getController());
            LynxGetADCCommand command = new LynxGetADCCommand(hub, channel, LynxGetADCCommand.Mode.ENGINEERING);
            return command.sendReceive().getValue();
        } catch (Exception ex) {
            telemetry.addLine(ex.getMessage());
        }
        return 0;
    }
}
