package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

@TeleOp(name = "Encoder Test", group = "Test")
class BoreThroughEncoderTest : LinearOpMode(), OpModeIF {

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        val motorEncoder: DcMotor = this.hardwareMap.dcMotor["motorLF"]

        motorEncoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorEncoder.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        motorEncoder.mode = DcMotor.RunMode.RUN_USING_ENCODER

        telemetry.addData("INIT", "[DONE]")
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            telemetry.addData("Encoder Position", motorEncoder.currentPosition.toString())
            telemetry.update()
        }
    }

    override fun getHardwareMap(): HardwareMap {
        return this.hardwareMap
    }

    override fun getTelemetry(): Telemetry {
        return this.telemetry
    }
}
