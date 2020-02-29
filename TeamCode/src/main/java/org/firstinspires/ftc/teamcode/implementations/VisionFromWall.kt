package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline
import kotlin.collections.ArrayList

class VisionFromWall @JvmOverloads constructor(var bot: Sursum, tl: Telemetry? = null) : OpenCvPipeline() {
    var position: Position
    var telemetry: Telemetry
    var matHSV: Mat

    /**
     * Enum class of relative stone positions
     */
    enum class Position {
        RIGHT, MIDDLE, LEFT, NULL
    }

    /**
     * companion object holding const values for lower and upper bounds of yellow in HSV color space
     */
    companion object {
        var lowerYellow: Scalar = Scalar(12.0, 100.0, 100.0)
        var upperYellow: Scalar = Scalar(32.0, 255.0, 255.0)
    }

    /**
     * default init call, constructor
     */
    init {
        position = Position.NULL
        telemetry = tl!!
        matHSV = Mat()
    }

    /**
     * process Frame that is called every tick by OpenCVPipeline
     */
    override fun processFrame(input: Mat): Mat {
        /**accumulator variable**/
        var ret = Mat()
        try {
            /**converting from RGB color space to HSV color space**/
            Imgproc.cvtColor(input, matHSV, Imgproc.COLOR_RGB2HSV_FULL)

            var channels = ArrayList<Mat>()

            for (i: Int in 0..2) {
                channels.add(Mat())
            }

            /**splitting the HSV Mat into its separate channels H, S, V respectively**/
            Core.split(matHSV, channels)

            var h: Mat = channels[0]
            var s: Mat = channels[1]
            var v: Mat = channels[2]

            /**equalization of the Value channel**/
            Imgproc.equalizeHist(v, v)

            var combined: MutableList<Mat> = ArrayList()
            combined.add(h)
            combined.add(s)
            combined.add(v)

            /**re-merging equalized V with H, S, V**/
            Core.merge(combined, matHSV)

            /**checking if any pixel is within the yellow bounds to make a black and white mask**/
            var mask = Mat(matHSV.rows(), matHSV.cols(), CvType.CV_8UC1)
            Core.inRange(matHSV, lowerYellow, upperYellow, mask)

            /**applying to input and putting it on ret in black or yellow**/
            Core.bitwise_and(input, input, ret, mask)

            /**finding contours on mask**/
            var contours: List<MatOfPoint> = ArrayList()
            var hierarchy = Mat()
            Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

            Imgproc.drawContours(ret, contours, -1, Scalar(0.0, 255.0, 0.0), 3)

            /**finding widths of each contour**/
            var widths: MutableList<Int> = ArrayList()
            for (c: MatOfPoint in contours) {
                var copy = MatOfPoint2f(*c.toArray())
                var rect: RotatedRect = Imgproc.minAreaRect(copy)

                var box = MatOfPoint()
                Imgproc.boxPoints(rect, box)

                var a: Core.MinMaxLocResult = Core.minMaxLoc(box)
                widths.add((a.maxVal - a.minVal).toInt())
            }

            var maxWidth = 0
            var maxI = 0
            for (i in widths.indices) {
                if (widths[i] > maxWidth) {
                    maxWidth = widths[i]
                    maxI = i
                }
            }

            /**finding biggest contours**/
            var copy1 = MatOfPoint2f(*(contours[maxI].toArray()))
            var rect1: RotatedRect = Imgproc.minAreaRect(copy1)

            var vertices: Array<Point> = Array(size = 4) { Point() }
            rect1.points(vertices)

            var boxes: MutableList<MatOfPoint> = ArrayList()
            boxes.add(MatOfPoint(*vertices))

            Imgproc.drawContours(ret, boxes, 0, Scalar(0.0, 0.0, 255.0), 2)

            var temp: Array<Point> = boxes[0].toArray()
            temp.sortBy { p1: Point -> p1.x + p1.y }

            /**getting bottom right and bottom left of the box**/
            var botRight: Point = temp[temp.size - 1]
            var topLeft: Point = temp[0]
            var botLeft: Point = temp[1]

            /**finding slope so testing locations will match this slope to always be in the center**/
            var m: Double = (botLeft.y - botRight.y) / (botLeft.x - botRight.x)

            var xOffSet = 50
            var yOffset = 35
            var width = 25
            var height = 25

            var location = Position.NULL

            /**testing locations**/
            for (i: Int in 0..2) {
                var x: Int = botRight.x.toInt() - 175 * i - xOffSet
                var y: Int = (m * (x - botRight.x) + botRight.y - yOffset).toInt()
                Imgproc.rectangle(
                        ret,
                        Point((x - width).toDouble(), (y - height).toDouble()),
                        Point(x.toDouble(), y.toDouble()),
                        Scalar(255.0, 0.0, 0.0),
                        5
                )

                /**finding the average of each box**/
                var avg: Scalar = Core.mean(mask.rowRange(y - height, y).colRange(x - width, x))

                telemetry.addData("[AVERAGE ${Position.values()[i]}]", avg.toString())

                /**box with stone will have a sum of approximately 0.0**/
                location = if (avg.`val`.sum() <= 10.0) Position.values()[i] else location
            }

            position = location
            telemetry.addData("[LOCATION]", position.toString())

        } catch (e: Exception) {
            /**error handling, prints stack trace for specific debug**/
            telemetry.addData("[ERROR]", e)
            e.stackTrace.toList().stream().forEach { x -> telemetry.addLine(x.toString()) }
        }
        telemetry.update()

        /**returns the black and yellow mask with contours drawn to see logic in action**/
        return ret
    }
}
