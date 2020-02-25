package org.firstinspires.ftc.teamcode.implementations

import android.graphics.Bitmap
import android.graphics.Color
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
import kotlin.math.roundToInt

class SursumFinder(opMode: OpModeIF) : Summum(opMode) {

    companion object {
        private const val RED = 0
        private const val GREEN = 1
        private const val BLUE = 2
    }

    val telemetry: Telemetry = opMode.telemetry

    val hardwareMap: HardwareMap = opMode.hardwareMap

    private var colorSide = 1

    private var vuforia: VisionVuforia = VisionVuforia(opMode = opMode, webcam = "Webcam 1")

    private var farTest = false
    private var centerTest = false
    private var closeTest = false

    private var testXStart = 351
    private var testYStart = 187
    private var testWidth = 722
    private var testHeight = 120

    @Throws(InterruptedException::class)
    override fun findSkystone(): SkyStonePosition {
        farTest = false
        centerTest = false
        closeTest = false

        val bitmap: Bitmap = vuforia.bitmap!!
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

        // determine how tests work
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
}
