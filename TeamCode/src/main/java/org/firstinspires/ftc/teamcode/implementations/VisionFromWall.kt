package org.firstinspires.ftc.teamcode.implementations

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline
import kotlin.collections.ArrayList

class VisionFromWall @JvmOverloads constructor(var bot: Sursum, tl: Telemetry? = null) : OpenCvPipeline() {
    var POSITION: Position
    var telemetry: Telemetry
    var matHSV: Mat

    enum class Position {
        RIGHT, LEFT, MIDDLE, NULL
    }

    companion object {
        var lowerYellow: Scalar = Scalar(15.0, 100.0, 100.0)
        var upperYellow: Scalar = Scalar(30.0, 255.0, 255.0)
    }

    init {
        POSITION = Position.NULL
        telemetry = tl!!
        matHSV = Mat()
    }

    override fun processFrame(input: Mat?): Mat {
        var ret = Mat()
        try {
            Imgproc.cvtColor(input, matHSV, Imgproc.COLOR_BGR2HSV)

            var channels = ArrayList<Mat>()

            for (i: Int in 0..2) {
                channels.add(Mat())
            }

            Core.split(matHSV, channels)

            var h: Mat = channels[0]
            var s: Mat = channels[1]
            var v: Mat = channels[2]

            Imgproc.equalizeHist(v, v)

            var combined: MutableList<Mat> = ArrayList()
            combined.add(h)
            combined.add(s)
            combined.add(v)

            Core.merge(combined, matHSV)

            telemetry.addData("[MATHSV]", Core.mean(matHSV))

            var mask = matHSV.inRange(lowerBound = lowerYellow, upperBound = upperYellow)
            telemetry.addData("[MASK]", Core.mean(mask))

            Core.bitwise_and(input, input, ret, mask)

            telemetry.addData("[RET]", Core.mean(ret))

            var contours: List<MatOfPoint> = ArrayList()
            var hierarchy = Mat()
            Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

            Imgproc.drawContours(ret, contours, -1, Scalar(0.0, 255.0, 0.0), 3)

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

            var copy1 = MatOfPoint2f(*(contours[maxI].toArray()))
            var rect1: RotatedRect = Imgproc.minAreaRect(copy1)

            var vertices: Array<Point> = Array(size = 4) { Point() }
            rect1.points(vertices)

            var boxes: MutableList<MatOfPoint> = ArrayList()
            boxes.add(MatOfPoint(*vertices))

            Imgproc.drawContours(ret, boxes, 0, Scalar(0.0, 0.0, 255.0), 2)

            var temp: Array<Point> = boxes[0].toArray()
            temp.sortBy { p1: Point -> p1.x + p1.y }

            var botRight: Point = temp[temp.size - 1]
            var topLeft: Point = temp[0]
            var botLeft: Point = temp[1]

            var m: Double = (botLeft.y - botRight.y) / (botLeft.x - botRight.x)

            var xOffSet = 50
            var yOffset = 25
            var width = 25
            var height = 25

            var location = Position.NULL

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

                var avg: Scalar = Core.mean(mask.rowRange(y - height, y).colRange(x - width, x))

                location = if (avg.`val`.sum() == 0.0) Position.values()[i] else location
            }

            POSITION = location

        } catch (e: Exception) {
            telemetry.addData("[ERROR]", e)
            e.stackTrace.toList().stream().forEach { x -> telemetry.addLine(x.toString()) }
            telemetry.update()
        }
        (bot.opMode as LinearOpMode).waitForStart()
        return ret
    }

    fun Mat.inRange(lowerBound: Scalar, upperBound: Scalar): Mat {
        var ret = Mat(this.rows(), this.cols(), 0)
        for (r: Int in 0 until this.rows()) {
            for (c: Int in 0 until this.cols()) {
                ret[r, c] = if (this[r, c].inBetween(lower = lowerBound, upper = upperBound)) Scalar(255.0) else Scalar(0.0)
                if(r == c && r % 10 == 0) telemetry.addData("$r,$c", ret[r, c]!!.contentToString())
            }
        }
        return ret
    }

    private fun DoubleArray.inBetween(lower: Scalar, upper: Scalar): Boolean {
        return this[0] >= lower.`val`[0]
                && this[0] <= upper.`val`[0]
                && this[1] >= lower.`val`[1]
                && this[1] <= upper.`val`[1]
                && this[2] >= lower.`val`[2]
                && this[2] <= upper.`val`[2]
    }

    private operator fun Mat.set(r: Int, c: Int, value: Scalar): Scalar {
        this.row(r).col(c).setTo(value)
        return value
    }
}