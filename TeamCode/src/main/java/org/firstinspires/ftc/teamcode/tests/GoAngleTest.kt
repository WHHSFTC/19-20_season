package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.implementations.Auto
import org.firstinspires.ftc.teamcode.implementations.DriveTrain

@Disabled
@Autonomous(name = "GoAngle Test", group = "Auto")
class GoAngleTest: Auto() {

    override fun genesis() {
        super.genesis()
        bot.driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT)
    }

    override fun run() {
        bot.driveTrain.goAngle(60.0, DriveTrain.LOADING_ZONE, .25)
    }
}