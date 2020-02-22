package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF

class VisionVuforia(opMode: OpModeIF, webcam: String) {

    companion object {
        private const val KEY =
                "AZjnTyD/////AAABmWbY5Kf/tUDGlNmyg0to/Ocsr2x5NKR0bN0q9InlH4shr90xC/iovUPDBu+PWzwD2+F8moAWhCpUivQDuKp/j2IHVtyjoKOQvPkTaXAb1IgPtAM6pMDltXDTkQ8Olwds22Z97Wdx+RAPK8WrC809Hj+JDZJJ3/Lx3bqAwcR1TRJ4OejxkWVSAKvFX8rOp5gE82jPNEv1bQ5S+iTgFtToZNQTj2ldtYJjoSkyUHqfODyV3JUazYSu82UEak0My2Ks/zIXYrDEY0y5MgNzRr9pzg3AiA8pbUT3SVk3SSUYmjlml+H9HovgDuiGrnJnmNMSjQGfcGpliGW6fs61ePYuAHvN4+Rwa1esR/prFgYKrTTn"
        private val parameters = VuforiaLocalizer.Parameters()
    }

    private var vuforia: VuforiaLocalizer? = null

    init {
        parameters.vuforiaLicenseKey = KEY
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK
        parameters.cameraName = opMode.hardwareMap.get(WebcamName::class.java, webcam)

        vuforia = ClassFactory.getInstance().createVuforia(parameters)

        vuforia!!.frameQueueCapacity = 1
        vuforia!!.enableConvertFrameToBitmap()
    }

    val bitmap = vuforia!!.convertFrameToBitmap(vuforia!!.frameQueue.take())
}
