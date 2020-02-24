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
        try {
            val matHSV = Mat()

            Imgproc.cvtColor(input, matHSV, Imgproc.COLOR_BGR2HSV)

            val dummyChannels: MutableList<Mat> = ArrayList()
            for (i in 0..2) {
                dummyChannels.add(Mat())
            }
            Core.split(matHSV, dummyChannels)

            val h = dummyChannels[0]
            val s = dummyChannels[1]
            val v = dummyChannels[2]

            Imgproc.equalizeHist(v, v)

            Core.merge(listOf(h, s, v), matHSV)

            val mask = Mat()
            Core.inRange(matHSV, lowerYellow, upperYellow, mask)

            val res = Mat()
            Core.bitwise_and(input, input, res, mask)

            val contours: List<MatOfPoint> = ArrayList()
            val hierarchy = Mat()

            Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

            Imgproc.drawContours(res, contours, -1, Scalar(0.0, 255.0, 0.0), 3)

            val widths: MutableList<Double> = ArrayList()

            for (c: MatOfPoint in contours) {

                val copy = MatOfPoint2f(*c.toArray())

                val rect: RotatedRect = Imgproc.minAreaRect(copy)

//                val box = MatOfPoint()
//                Imgproc.boxPoints(rect, box)
//
//                val a: Core.MinMaxLocResult = Core.minMaxLoc(box)
//
//                widths.add(a.maxLoc.x - a.minLoc.x)

                val vertices: Array<Point> = Array(size = 4) { Point() }

                rect.points(vertices)
                vertices.sortBy { it.x + it.y }
                widths.add(vertices[-1].x - vertices[0].x)
            }

            var maxWidth = 0.0
            var maxI = 0
            for (i in 0 until widths.size) {
                if (widths[i] > maxWidth) {
                    maxWidth = widths[i]
                    maxI = i
                }
            }

//            val copy = MatOfPoint2f()
//            contours[maxI].convertTo(copy, CvType.CV_32F)
            val copy = MatOfPoint2f(*contours[maxI].toArray())
            val rect: RotatedRect = Imgproc.minAreaRect(copy)

//            val box: MatOfPoint = MatOfPoint()
//            Imgproc.boxPoints(rect, box)
//
//            val boxes: ArrayList<MatOfPoint> = listOf(box) as ArrayList<MatOfPoint>
//
//            Imgproc.drawContours(input, boxes, 0, Scalar(0.0, 0.0, 255.0), 2)
//
//            boxes.sortWith(
//                    Comparator {
//                        mop1: MatOfPoint?, mop2: MatOfPoint? ->
//                        Core.sumElems(mop1).`val`[0].toInt().compareTo(Core.sumElems(mop2).`val`[0].toInt())
//                    }
//            )
//

            val vertices: Array<Point> = Array(size = 4) { Point() }
            rect.points(vertices)

            val boxContours: MutableList<MatOfPoint> = ArrayList()
            boxContours.add(MatOfPoint(*vertices))

            Imgproc.drawContours(res, boxContours, 0, Scalar(0.0, 0.0, 255.0), 2)

            boxContours.sortBy { it.toArray().sumBy { p: Point -> (p.x + p.y).toInt() } }

            val botRight = boxContours[-1].toArray()[0]
            val topLeft = boxContours[0].toArray()[0]
            val botLeft = boxContours[1].toArray()[0]

            val m: Double = (botLeft.y - botRight.y) / (botLeft.x - botRight.y)

            val xOffSet = 50
            val yOffSet = 25
            val width = 25
            val height = 25

            var location: Position = Position.NULL

            for (i in 0 until 3) {
                val x = (botRight.x - 175 * i - xOffSet).toInt()
                val y = (m * (x - botRight.x) + botRight.y - yOffSet).toInt()
                Imgproc.rectangle(input,
                        Point((x - width).toDouble(), (y - height).toDouble()),
                        Point(x.toDouble(), y.toDouble()),
                        Scalar(255.0, 0.0, 0.0),
                        5
                )

                val avg = Core.mean(mask.rowRange(y - height, y).colRange(x - width, x))
                location = if (avg.`val`.sum() == 0.0) Position.values()[i] else location
            }

            POSITION = location

        } catch (e: Exception) {
            telemetry.addData("[ERROR]", e.message)
            telemetry.update()
        }

        return input!!
    }

}