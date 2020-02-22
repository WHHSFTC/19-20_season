package org.firstinspires.ftc.teamcode.tests

import android.graphics.Bitmap
import android.graphics.Color
import com.vuforia.Image
import com.vuforia.PIXEL_FORMAT
import com.vuforia.Vuforia
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CloseableFrame
import kotlin.math.max
import kotlin.math.min

class VisionFromWallTest(var vuforia: VuforiaLocalizer) {

    enum class Position {
        LEFT, CENTER, RIGHT;
    }

    private fun bitmap(): Bitmap {
        var rgbImage: Image? = null

        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true)

        var closeableFrame: CloseableFrame? = null

        vuforia.frameQueueCapacity = 1

        while (rgbImage == null) {
            try {
                closeableFrame = vuforia.frameQueue.first()
                val numImages: Int = closeableFrame.numImages.toInt()
                for (i in 0 until numImages) {
                    if (closeableFrame.getImage(i).format == PIXEL_FORMAT.RGB565) {
                        rgbImage = closeableFrame.getImage(i)
                        if (rgbImage != null) {
                            break
                        }
                    }
                }
            } catch (exc: InterruptedException) {
            } finally {
                closeableFrame?.close()
            }
        }

        // copy the bitmap from the Vuforia frame
        val bitmap = Bitmap.createBitmap(rgbImage.width, rgbImage.height, Bitmap.Config.RGB_565)

        bitmap.copyPixelsFromBuffer(rgbImage.pixels)

        return bitmap
    }

    private fun cropYRange(bitMap: Bitmap): Range {
        val top: Int
        val bot: Int

        val x = 0
        var y = bitMap.height-1

        var previousPixel: Int = bitMap.getPixel(x, y--)

        while (bitMap.getPixel(x, y--) == previousPixel) { previousPixel = bitMap.getPixel(x, y+1) }

        bot = y

        previousPixel = bitMap.getPixel(x, y--)

        while (bitMap.getPixel(x, y--) == previousPixel) { previousPixel = bitMap.getPixel(x, y+1) }

        top = y

        return top too bot
    }

    private fun cropXRange(croppedYBitMap: Bitmap): Range {
        val left: Int
        val right: Int

        left = if (croppedYBitMap.getPixel(0, 0) != croppedYBitMap.getPixel(croppedYBitMap.width-1, 0)) {
            0
        } else {
            var c = 1
            var previousPixel: Int = croppedYBitMap.getPixel(0, 0)
            while (croppedYBitMap.getPixel(c++,0) == previousPixel) { previousPixel = croppedYBitMap.getPixel(c-1,0) }
            c
        }

        var x = croppedYBitMap.width-1
        val y = 0

        var previousPixel: Int = croppedYBitMap.getPixel(x--, y)

        while (croppedYBitMap.getPixel(x--, y) == previousPixel) { previousPixel = croppedYBitMap.getPixel(x+1, y) }

        right = x

        return left too right
    }

    private fun croppedYBitMap(source: Bitmap, yRange: Range): Bitmap {
        return Bitmap.createBitmap(source, 0, yRange.lower, source.width, yRange.range)
    }

    private fun cropRange(source: Bitmap): Map<String, Range> {
        val yRange = cropYRange(bitMap = source)
        val xRange = cropXRange(croppedYBitMap = croppedYBitMap(source = source, yRange = yRange))

        return mapOf("X" to xRange, "x" to xRange, "Y" to yRange, "y" to yRange)
    }

    private fun croppedBitMap(source: Bitmap, xRange: Range, yRange: Range): Bitmap {
        return Bitmap.createBitmap(source, xRange.lower, yRange.lower, xRange.range, yRange.range)
    }

    @Throws(IllegalStateException::class)
    private fun croppedBitMap(source: Bitmap): Bitmap {
        val crop = cropRange(source = source)

        return croppedBitMap(
                source = source,
                xRange = crop["x"] ?: error(message = "no x-range found"),
                yRange = crop["y"] ?: error(message = "no y- range found")
        )
    }

    fun findPosition(): Position? {
        val mapOfBit = bitmap()

        val croppedMapOfBit = croppedBitMap(source = mapOfBit)

        val compressedMapOfBit = croppedMapOfBit.compress(desiredWidth = 110, desiredHeight = 20, filter = true)

        // TODO current implementation is Khadija's method, check if works, may change to own method
        var yellowCountL = 1.0
        var yellowCountC = 1.0
        var yellowCountR = 1.0

        var blackCountL = 1.0
        var blackCountC = 1.0
        var blackCountR = 1.0

        var width: Int
        var pixel: Int

        val bitmapWidth = compressedMapOfBit.width
        val bitmapHeight = compressedMapOfBit.height

        val colWidth = (bitmapWidth.toDouble() / 6.0).toInt()
        val colorLStartCol = (bitmapWidth.toDouble() * (1.0 / 6.0) - colWidth.toDouble() / 2.0).toInt()
        val colorCStartCol = (bitmapWidth.toDouble() * (3.0 / 6.0) - colWidth.toDouble() / 2.0).toInt()
        val colorRStartCol = (bitmapWidth.toDouble() * (5.0 / 6.0) - colWidth.toDouble() / 2.0).toInt()

        var height = 0

        while (height < bitmapHeight) {
            width = colorLStartCol
            while (width < colorLStartCol + colWidth) {
                pixel = croppedMapOfBit.getPixel(width, height)
                if (Color.red(pixel) < 200 || Color.green(pixel) < 200 || Color.blue(pixel) < 200) {
                    yellowCountL += Color.red(pixel).toDouble()
                    blackCountL += Color.blue(pixel).toDouble()
                }
                ++width
            }
            width = colorCStartCol
            while (width < colorCStartCol + colWidth) {
                pixel = croppedMapOfBit.getPixel(width, height)
                if (Color.red(pixel) < 200 || Color.green(pixel) < 200 || Color.blue(pixel) < 200) {
                    yellowCountC += Color.red(pixel).toDouble()
                    blackCountC += Color.blue(pixel).toDouble()
                }
                ++width
            }
            width = colorRStartCol
            while (width < colorRStartCol + colWidth) {
                pixel = croppedMapOfBit.getPixel(width, height)
                if (Color.red(pixel) < 200 || Color.green(pixel) < 200 || Color.blue(pixel) < 200) {
                    yellowCountR += Color.red(pixel).toDouble()
                    blackCountR += Color.blue(pixel).toDouble()
                }
                ++width
            }
            ++height
        }

        val blackYellowRatioL = blackCountL / yellowCountL
        val blackYellowRatioC = blackCountC / yellowCountC
        val blackYellowRatioR = blackCountR / yellowCountR

        val pos: Position = if (blackYellowRatioL > blackYellowRatioC && blackYellowRatioL > blackYellowRatioR) {
            Position.LEFT
        } else if (blackYellowRatioC > blackYellowRatioL && blackYellowRatioC > blackYellowRatioR) {
            Position.CENTER
        } else {
            Position.RIGHT
        }

        Debug.log("black/yellow L: $blackCountL/$yellowCountL")
        Debug.log("black/yellow C: $blackCountC/$yellowCountC")
        Debug.log("black/yellow R: $blackCountR/$yellowCountR")

        return pos
    }
    class Range(lower: Int, upper: Int) {
        val lower = lower
        val upper = upper
        val range = upper - lower
    }

    private infix fun Int.too(other: Int): Range {
        return Range(lower = min(a = this, b = other), upper = max(a = this, b = other))
    }

    private fun Bitmap.compress(desiredWidth: Int, desiredHeight: Int, filter: Boolean): Bitmap {
        return Bitmap.createScaledBitmap(this, desiredWidth, desiredHeight, filter)
    }
}
