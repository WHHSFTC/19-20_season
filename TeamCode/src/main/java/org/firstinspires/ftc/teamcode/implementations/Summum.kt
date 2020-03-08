package org.firstinspires.ftc.teamcode.implementations

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.DigitalChannel
import org.firstinspires.ftc.teamcode.interfaces.ContinuousMechanism
import org.firstinspires.ftc.teamcode.interfaces.Mechanism
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
import org.firstinspires.ftc.teamcode.interfaces.StrafingDriveTrain
import org.openftc.easyopencv.OpenCvCamera

/**
 * Bot Class
 */
open class Summum(var opMode: OpModeIF) {
    // fields that changed based on what side we start on
    var our_side = 0.0
    var opponents_side = 0.0

    // declarations of all systems
    @JvmField
    var driveTrain: StrafingDriveTrain = DriveTrain(opMode, "RFWheel", "LFWheel", "LBWheel", "RBWheel")

    @JvmField
    var output: Output = Output(opMode, "LSlides", "RSlides", "HSlides", "Inner", "Outer")

    @JvmField
    var flywheels: DcMotorSimple = FlyWheels(opMode, "LFlywheel", "RFlywheel")

    @JvmField
    var foundation: FoundationHooks = FoundationHooks(opMode = opMode, leftStr = "LHook", rightStr = "RHook")

//    @JvmField
//    var capStone: CapStone = CapStone(opMode = opMode, str = "capMech")

    private val allianceSwitch: DigitalChannel = opMode.hardwareMap.digitalChannel["allianceSwitch"]

    lateinit var alliance: Alliance

    lateinit var camera: OpenCvCamera
    lateinit var pipeline: VisionFromWall

    fun init() {
        init(if (allianceSwitch.state)  Alliance.BLUE else Alliance.RED)
        opMode.telemetry.addData("Alliance", alliance.toString())
    }

    fun initAuto() {
        output.claw.state = Claw.State.INIT
        init()
    }

    fun initTele() {
        output.claw.state = Claw.State.INNER
        init()
        driveTrain.setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
    }

    fun init(alliance: Alliance?) {
        this.alliance = alliance!!
        when (this.alliance) {
            Alliance.RED -> {
                our_side = DriveTrain.RED_SIDE
                opponents_side = DriveTrain.BLUE_SIDE
            }
            Alliance.BLUE -> {
                our_side = DriveTrain.BLUE_SIDE
                opponents_side = DriveTrain.RED_SIDE
            }
        }
        // reset the motors
        driveTrain.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        driveTrain.setModes(DcMotor.RunMode.RUN_USING_ENCODER)

        // set zero power behaviors to float so Kaden can turn the bot
        driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT)
        output.slides.state = HorizontalSlides.State.IN
        foundation.state = FoundationHooks.State.UP
        // tell Kaden the bot can now be pushed
        opMode.telemetry.addLine("Initialization DONE")
    }

    fun translateRelativePosition(`val`: VisionFromWall.Position): SkyStonePosition {
        if (`val` === VisionFromWall.Position.NULL) {
            opMode.telemetry.addLine("[ERROR] Found Position.NULL, returning SkyStonePosition.ONE_FOUR")
            opMode.telemetry.update()
            return SkyStonePosition.ONE_FOUR
        }
        if (`val` === VisionFromWall.Position.MIDDLE) {
            return SkyStonePosition.TWO_FIVE
        }
        return if (alliance == Alliance.RED) {
            if (`val` === VisionFromWall.Position.LEFT) SkyStonePosition.ONE_FOUR else SkyStonePosition.THREE_SIX
        } else {
            if (`val` === VisionFromWall.Position.LEFT) SkyStonePosition.THREE_SIX else SkyStonePosition.ONE_FOUR
        }
    }

    /**
     * stops bot completely
     */
    fun stop() {
        driveTrain.stop()
        flywheels.power = 0.0
    }

    fun start() {
        driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE)
    }

    companion object {
        // Constants that contain magic numbers
        const val ROBOT_WIDTH = 17.75
        const val ROBOT_LENGTH = 17.75
        const val SIDEARM_Y = ROBOT_LENGTH / 2 - 5
        const val CAMERA_X = ROBOT_WIDTH / 2 - 2.5
        const val SIDE_ARM_OFFSET = 8.0 //inches
    }

    /**
     * Creation of all systems of the bot
     * @param opMode import current opMode to get initialize
     */
    init {
        allianceSwitch.mode = DigitalChannel.Mode.INPUT
    }
}