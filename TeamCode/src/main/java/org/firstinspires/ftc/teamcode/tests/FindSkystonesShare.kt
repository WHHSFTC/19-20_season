package org.firstinspires.ftc.teamcode.tests

import android.graphics.Bitmap
import android.graphics.Color
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.teamcode.implementations.SkyStonePosition
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
import java.util.*
import kotlin.math.roundToInt

@Autonomous(name = "Find Skystones Share", group = "Auto")
internal class FindSkystonesShare(var opMode: OpModeIF) : OpModeIF {

    companion object {
        const val RED = 0
        const val GREEN = 1
        const val BLUE = 2
    }

    private var colorSide = 1

    private var vuforia: VuforiaLocalizer? = null

    private var farTest = false
    private var centerTest = false
    private var closeTest = false

    private var testXStart = 351
    private var testYStart = 187
    private var testWidth = 722
    private var testHeight = 120

    // Telemetry outputs the average red value, average green value, average blue value,
    // number of yellow pixels, and number of black pixels.
    // we found the number of yellow pixels to be more accurate so that is what this
    // file does.
    // in order to run you need to define the pixel locations of the blocks... the pixels
    // must be measured from the bottom right pixel, though phone orientation may affect this
    //the test width is for the entire 3 block row. We found the best way to find these
    // measurements by opening up the built in Concept: VuMark Id and using a ruler to measure
    //the distance from the bottom right corner. Then use the entire distance across the view
    // to get the total x distance and using the fact that the camera is 100x720 to get
    //the partial x distance. This is repeated to get every number and then fine tuned.

//    @Throws(InterruptedException::class)
//    override fun runOpMode() {
//        initVuforia()
//        waitForStart()
//        while (opModeIsActive()) {
//            findSkystone()
//            telemetry.update()
//        }
//    }

    init {
        val parameters = VuforiaLocalizer.Parameters()

        parameters.vuforiaLicenseKey =
                "AZjnTyD/////AAABmWbY5Kf/tUDGlNmyg0to/Ocsr2x5NKR0bN0q9InlH4shr90xC/iovUPDBu+PWzwD2+F8moAWhCpUivQDuKp/j2IHVtyjoKOQvPkTaXAb1IgPtAM6pMDltXDTkQ8Olwds22Z97Wdx+RAPK8WrC809Hj+JDZJJ3/Lx3bqAwcR1TRJ4OejxkWVSAKvFX8rOp5gE82jPNEv1bQ5S+iTgFtToZNQTj2ldtYJjoSkyUHqfODyV3JUazYSu82UEak0My2Ks/zIXYrDEY0y5MgNzRr9pzg3AiA8pbUT3SVk3SSUYmjlml+H9HovgDuiGrnJnmNMSjQGfcGpliGW6fs61ePYuAHvN4+Rwa1esR/prFgYKrTTn"
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK

        vuforia = ClassFactory.getInstance().createVuforia(parameters)

        vuforia!!.frameQueueCapacity = 1
        vuforia!!.enableConvertFrameToBitmap()
    }

    @Throws(InterruptedException::class)
    fun findSkystone(): SkyStonePosition {
        farTest = false
        centerTest = false
        closeTest = false

        val bitmap: Bitmap = vuforia!!.convertFrameToBitmap(vuforia!!.frameQueue.take())!!
        val width = bitmap.width
        val height = bitmap.height

        val rawColorArray = IntArray(bitmap.width * bitmap.height)

        bitmap.getPixels(rawColorArray, 0, width, 0, 0, width, height)

        val pixels = Array(width) { Array(height) { DoubleArray(size = 3) } }

        for (j in 0 until height) {
            for (i in 0 until width) {
                //telemetry.addLine("width: " + i + " height: " + j + " value: "+((rawColorArray[j*width+i])&0xFF));

                val pixelValue: Int = rawColorArray[j * width + i]

                pixels[i][j][RED] = Color.red(pixelValue).toDouble() // (rawColorArray[j * width + i] shr 16 and 0xFF).toDouble()  RED
                pixels[i][j][GREEN] = Color.green(pixelValue).toDouble() // (rawColorArray[j * width + i] shr 8 and 0xFF).toDouble()  GREEN
                pixels[i][j][BLUE] = Color.blue(pixelValue).toDouble() // (rawColorArray[j * width + i] and 0xFF).toDouble()  BLUE
            }
        }

        val increment = testWidth / 3

        val block1Test = pixels.averageValues(
                xStart = testXStart + 10,
                xLength = increment - 20,
                yStart = testYStart,
                yLength = testHeight
        )

        val block2Test = pixels.averageValues(
                xStart = testXStart + increment + 10,
                xLength = increment - 20,
                yStart = testYStart,
                yLength = testHeight
        )

        val block3Test = pixels.averageValues(
                xStart = testXStart + increment * 2 + 10,
                xLength = increment - 20,
                yStart = testYStart,
                yLength = testHeight
        )

        //r,g,b,yellows,blacks
        telemetry.addLine(block1Test.contentToString())
        telemetry.addLine(block2Test.contentToString())
        telemetry.addLine(block3Test.contentToString())

        if (block1Test[3] < block2Test[3] && block1Test[3] < block3Test[3]) {
            if (colorSide == 1) {
                closeTest = true
            } else {
                farTest = true
            }
        } else if (block2Test[3] < block1Test[3] && block2Test[3] < block3Test[3]) {
            centerTest = true
        } else if (block3Test[3] < block2Test[3] && block3Test[3] < block1Test[3]) {
            if (colorSide == 1) {
                farTest = true
            } else {
                closeTest = true
            }
        }

        telemetry.addLine("close $closeTest   center $centerTest    far $farTest")
        telemetry.update()

        // figure out tests
        return SkyStonePosition.ONE_FOUR
    }

    private fun Array<Array<DoubleArray>>.averageValues(
            xStart: Int,
            xLength: Int,
            yStart: Int,
            yLength: Int
    ): DoubleArray {
        val temp = DoubleArray(5)

        var yellows = 0
        var blacks = 0

        val numPixels = xLength * yLength.toDouble()

        for (j in yStart..yStart + yLength) {
            for (i in xStart..xStart + xLength) {
                //telemetry.addLine("j: " + j + " i: " + i + "Values: " + pixels[i][j][0] +","+ pixels[i][j][1]+","+ pixels[i][j][2]);

                temp[RED] += this[i][j][RED]
                temp[GREEN] += this[i][j][GREEN]
                temp[BLUE] += this[i][j][BLUE]
            }
        }

        temp[0] /= numPixels
        temp[1] /= numPixels
        temp[2] /= numPixels

        temp[0] = temp[0].roundToInt().toDouble()
        temp[1] = temp[1].roundToInt().toDouble()
        temp[2] = temp[2].roundToInt().toDouble()

        for (j in yStart..yStart + yLength) {
            for (i in xStart..xStart + xLength) {
                //telemetry.addLine("j: " + j + " i: " + i + "Values: " + pixels[i][j][0] +","+ pixels[i][j][1]+","+ pixels[i][j][2]);

                if (
                        this[i][j][RED] > 90 &&
                        this[i][j][GREEN] > 90 &&
                        this[i][j][BLUE] < 120 &&
                        this[i][j][RED] + this[i][j][GREEN] > this[i][j][BLUE] * 2.7
                ) {
                    yellows++
                }

                if (
                        this[i][j][RED] < 100 &&
                        this[i][j][GREEN] < 100 &&
                        this[i][j][BLUE] < 100 &&
                        (this[i][j][RED] + this[i][j][GREEN] + this[i][j][BLUE]) / 3 < 75
                ) {
                    blacks++
                }
            }
        }

        temp[3] = yellows.toDouble()
        temp[4] = blacks.toDouble()

        return temp
    }

    override fun getHardwareMap(): HardwareMap {
        return opMode.hardwareMap
    }

    override fun getTelemetry(): Telemetry {
        return opMode.telemetry
    }

    override fun opModeIsActive(): Boolean {
        return opMode.opModeIsActive()
    }
}