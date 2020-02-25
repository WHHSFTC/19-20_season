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
    var driveTrain: StrafingDriveTrain
    var shuttleGate: Mechanism<ShuttleGate.State>
    var outputSlides: ContinuousMechanism
    var arm: Mechanism<Arm.State>
    var claw: Mechanism<Claw.State>
    var flywheels: DcMotorSimple
    var belt: DcMotor
    var leftArm: LeftSideArm
    var rightArm: RightSideArm
    var sideArm: SideArm? = null
    var visionTF: VisionTF? = null
    var alliance: Alliance? = null
    var visionFtc: VisionFTC? = null
    var visionTest: VisionWall? = null
    private val allianceSwitch: DigitalChannel
    var camera: OpenCvCamera? = null
    var pipeline: VisionFromWall? = null
    fun init() {
        init(if (allianceSwitch.state) Alliance.BLUE else Alliance.RED)
        opMode.telemetry.addData("Alliance", alliance.toString())
    }

    fun init(alliance: Alliance?) {
        this.alliance = alliance
        when (this.alliance) {
            Alliance.RED -> {
                our_side = DriveTrain.RED_SIDE
                opponents_side = DriveTrain.BLUE_SIDE
                sideArm = rightArm
            }
            Alliance.BLUE -> {
                our_side = DriveTrain.BLUE_SIDE
                opponents_side = DriveTrain.RED_SIDE
                sideArm = leftArm
            }
        }
        // reset the motors
        driveTrain.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        driveTrain.setModes(DcMotor.RunMode.RUN_USING_ENCODER)
        // set servo positions
        shuttleGate.state = ShuttleGate.State.CLOSED
        arm.state = Arm.State.BELT
        claw.state = Claw.State.OPEN
        leftArm.arm.state = SideArm.Arm.State.UP
        rightArm.arm.state = SideArm.Arm.State.UP
        leftArm.claw.state = SideArm.Claw.State.CLOSED
        rightArm.claw.state = SideArm.Claw.State.CLOSED
        // set zero power behaviors to float so Kaden can turn the bot
        driveTrain.setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.FLOAT)
        // tell Kaden the bot can now be pushed
        opMode.telemetry.addLine("Initialization DONE")
    }

    /**
     * finds the skystone unsing TensorFlow to detect
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    open fun findSkystone(): SkyStonePosition? {
        for (position in arrayOf(SkyStonePosition.THREE_SIX, SkyStonePosition.TWO_FIVE)) {
            if (visionTF!!.stone) {
                driveTrain.goAngle(3.25, DriveTrain.LOADING_ZONE, .25)
                return position
            }
            driveTrain.goAngle(8.0, DriveTrain.LOADING_ZONE, .25)
        }
        driveTrain.goAngle(3.25, DriveTrain.LOADING_ZONE, .25)
        return SkyStonePosition.ONE_FOUR
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

    fun translateRelativePosition(`val`: VisionFTC.SkystonePosition): SkyStonePosition {
        if (`val` === VisionFTC.SkystonePosition.CENTER_STONE) {
            return SkyStonePosition.TWO_FIVE
        }
        return if (alliance == Alliance.RED) {
            if (`val` === VisionFTC.SkystonePosition.LEFT_STONE) SkyStonePosition.ONE_FOUR else SkyStonePosition.THREE_SIX
        } else {
            if (`val` === VisionFTC.SkystonePosition.LEFT_STONE) SkyStonePosition.THREE_SIX else SkyStonePosition.ONE_FOUR
        }
    }

    /**
     * Intake mechanisms
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    fun intake() {
        flywheels.power = -2.0 / 3
        belt.power = -1.0
        Thread.sleep(500)
        flywheels.power = 0.0
        Thread.sleep(500)
        belt.power = 0.0
    }

    /**
     * stops bot completely
     */
    fun stop() {
        driveTrain.stop()
        shuttleGate.stop()
        outputSlides.stop()
        arm.stop()
        claw.stop()
        flywheels.power = 0.0
        belt.power = 0.0
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
        driveTrain = DriveTrain(opMode, "motorRF", "motorLF", "motorLB", "motorRB")
        shuttleGate = ShuttleGate(opMode, "leftGate", "rightGate")
        // output {{{
        outputSlides = OutputSlides(opMode, "spool1", "spool2")
        arm = Arm(opMode, "elbow", "wrist")
        claw = Claw(opMode, "inner", "outer")
        // }}}
// intake {{{
        flywheels = FlyWheels(opMode, "leftIn", "rightIn")
        belt = opMode.hardwareMap.dcMotor["belt"]
        leftArm = LeftSideArm(opMode, "leftArm", "leftClaw")
        rightArm = RightSideArm(opMode, "rightArm", "rightClaw")
        allianceSwitch = opMode.hardwareMap.digitalChannel["allianceSwitch"]
        allianceSwitch.mode = DigitalChannel.Mode.INPUT
        // }}}
    }
}