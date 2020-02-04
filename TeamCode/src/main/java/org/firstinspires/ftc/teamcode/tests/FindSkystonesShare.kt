package org.firstinspires.ftc.teamcode.tests

import android.graphics.Color
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import java.util.*
import kotlin.math.roundToInt

@Autonomous(name = "Find Skystones Share", group = "Auto")
internal class FindSkystonesShare : LinearOpMode() {

    companion object {
        private const val RED = 0
        private const val GREEN = 1
        private const val BLUE = 2
    }

    private var colorSide = 1

    private var vuforia: VuforiaLocalizer? = null

    private var farTest = false
    private var centerTest = false
    private var closeTest = false

    private var testxStart = 351
    private var testyStart = 187
    private var testwidth = 722
    private var testheight = 120

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

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        initVuforia()
        waitForStart()
        while (opModeIsActive()) {
            findSkystone()
            telemetry.update()
        }
    }

    private fun initVuforia() {
        val parameters = VuforiaLocalizer.Parameters()

        parameters.vuforiaLicenseKey = "PASTE VUFORIA KEY HERE"
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK

        vuforia = ClassFactory.getInstance().createVuforia(parameters)

        vuforia!!.frameQueueCapacity = 1
        vuforia!!.enableConvertFrameToBitmap()
    }

    @Throws(InterruptedException::class)
    fun findSkystone() {
        farTest = false
        centerTest = false
        closeTest = false

        val bitmap = vuforia!!.convertFrameToBitmap(vuforia!!.frameQueue.take())
        val width = bitmap!!.width
        val height = bitmap.height

        val rawColorArray = IntArray(bitmap.width * bitmap.height)

        bitmap.getPixels(rawColorArray, 0, width, 0, 0, width, height)

        val pixels = Array(width) { Array(height) { DoubleArray(size = 3) } }

        for (j in 0 until height) {
            for (i in 0 until width) {
                //telemetry.addLine("width: " + i + " height: " + j + " value: "+((rawColorArray[j*width+i])&0xFF));

                val pixelValue: Int = rawColorArray[j * width + i]

                pixels[i][j][RED] = Color.red(pixelValue) as Double // (rawColorArray[j * width + i] shr 16 and 0xFF).toDouble()  RED
                pixels[i][j][GREEN] = Color.green(pixelValue) as Double // (rawColorArray[j * width + i] shr 8 and 0xFF).toDouble()  GREEN
                pixels[i][j][BLUE] = Color.blue(pixelValue) as Double // (rawColorArray[j * width + i] and 0xFF).toDouble()  BLUE
            }
        }

        val increment = testwidth / 3

        val block1Test = averageValues(
                pixels = pixels,
                xStart = testxStart + 10,
                xLength = increment - 20,
                yStart = testyStart,
                yLength = testheight
        )

        val block2Test = averageValues(
                pixels = pixels,
                xStart = testxStart + increment + 10,
                xLength = increment - 20,
                yStart = testyStart,
                yLength = testheight
        )

        val block3Test = averageValues(
                pixels = pixels,
                xStart = testxStart + increment * 2 + 10,
                xLength = increment - 20,
                yStart = testyStart,
                yLength = testheight
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
            centerTest = if (colorSide == 1) {
                true
            } else {
                true
            }
        } else if (block3Test[3] < block2Test[3] && block3Test[3] < block1Test[3]) {
            if (colorSide == 1) {
                farTest = true
            } else {
                closeTest = true
            }
        }

        telemetry.addLine("close $closeTest   center $centerTest    far $farTest")
        telemetry.update()
    }

    private fun averageValues(
            pixels: Array<Array<DoubleArray>>,
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

                temp[RED] += pixels[i][j][RED]
                temp[GREEN] += pixels[i][j][GREEN]
                temp[BLUE] += pixels[i][j][BLUE]
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
                        pixels[i][j][RED] > 90 &&
                        pixels[i][j][GREEN] > 90 &&
                        pixels[i][j][BLUE] < 120 &&
                        pixels[i][j][RED] + pixels[i][j][GREEN] > pixels[i][j][BLUE] * 2.7
                ) {
                    yellows++
                }

                if (pixels[i][j][RED] < 100 &&
                        pixels[i][j][GREEN] < 100 &&
                        pixels[i][j][BLUE] < 100 &&
                        (pixels[i][j][RED] + pixels[i][j][GREEN] + pixels[i][j][BLUE]) / 3 < 75
                ) {
                    blacks++
                }
            }
        }

        temp[3] = yellows.toDouble()
        temp[4] = blacks.toDouble()

        return temp
    }
}