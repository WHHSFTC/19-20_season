package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline
import java.util.*
import kotlin.collections.ArrayList

class VisionFromWall @JvmOverloads constructor(tl: Telemetry? = null) : OpenCvPipeline() {
    private var telemetry: Telemetry = tl!!

    enum class Position {
        RIGHT, LEFT, MIDDLE, NULL
    }

    companion object {
        val lowerYellow: Scalar = Scalar(15.0, 100.0, 100.0)
        val upperYellow: Scalar = Scalar(30.0, 255.0, 255.0)
    }

    var POSITION: Position = Position.NULL
        private set

    override fun processFrame(input: Mat?): Mat {
        val ret = Mat()
        try {
            val matHSV = Mat()
            Imgproc.cvtColor(input, matHSV, Imgproc.COLOR_BGR2HSV)

            val channels = ArrayList<Mat>()

            for (i: Int in 0..2) {
                channels.add(Mat())
            }

            Core.split(matHSV, channels)

            val h: Mat = channels[0]
            val s: Mat = channels[1]
            val v: Mat = channels[2]

            Imgproc.equalizeHist(v, v)

            val combined: MutableList<Mat> = ArrayList()
            combined.add(h)
            combined.add(s)
            combined.add(v)

            Core.merge(combined, matHSV)

            val mask = Mat()
            Core.inRange(matHSV, lowerYellow, upperYellow, mask)

            Core.bitwise_and(input, input, ret, mask)

            val contours: List<MatOfPoint> = ArrayList()
            val hierarchy = Mat()
            Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

            Imgproc.drawContours(ret, contours, -1, Scalar(0.0, 255.0, 0.0), 3)

            val widths: MutableList<Int> = ArrayList()
            for (c: MatOfPoint in contours) {
                val copy = MatOfPoint2f(*c.toArray())
                val rect: RotatedRect = Imgproc.minAreaRect(copy)

                val box = MatOfPoint()
                Imgproc.boxPoints(rect, box)

                val a: Core.MinMaxLocResult = Core.minMaxLoc(box)
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

            val copy1 = MatOfPoint2f(*contours[maxI].toArray())
            val rect1: RotatedRect = Imgproc.minAreaRect(copy1)

            val vertices: Array<Point> = Array(size = 4) { Point() }
            rect1.points(vertices)

            val boxes: MutableList<MatOfPoint> = ArrayList()
            boxes.add(MatOfPoint(*vertices))

            Imgproc.drawContours(ret, boxes, 0, Scalar(0.0, 0.0, 255.0), 2)

            val temp: Array<Point> = boxes[0].toArray()
            temp.sortBy { p1: Point -> p1.x + p1.y }

            val botRight: Point = temp[temp.size - 1]
            val topLeft: Point = temp[0]
            val botLeft: Point = temp[1]

            val m: Double = (botLeft.y - botRight.y) / (botLeft.x - botRight.x)

            val xOffSet = 50
            val yOffset = 25
            val width = 25
            val height = 25

            var location = Position.NULL

            for (i: Int in 0..2) {
                val x: Int = botRight.x.toInt() - 175 * i - xOffSet
                val y: Int = (m * (x - botRight.x) + botRight.y - yOffset).toInt()
                Imgproc.rectangle(
                        ret,
                        Point((x - width).toDouble(), (y - height).toDouble()),
                        Point(x.toDouble(), y.toDouble()),
                        Scalar(255.0, 0.0, 0.0),
                        5
                )

                val avg: Scalar = Core.mean(mask.rowRange(y - height, y).colRange(x - width, x))

                location = if (avg.`val`.sum() == 0.0) Position.values()[i] else location
            }

            POSITION = location

        } catch (e: Exception) {
            telemetry.addData("[ERROR]", e.message)
            telemetry.update()
        }

        return ret
    }

}