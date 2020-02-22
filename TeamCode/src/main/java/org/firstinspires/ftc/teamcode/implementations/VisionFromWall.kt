package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
import org.openftc.easyopencv.OpenCvPipeline
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class VisionFromWall(val opmode: OpModeIF, val name: String) : OpenCvPipeline() {
    enum class Position {
        LEFT, RIGHT, MIDDLE, NULL
    }

    lateinit var matHSV: Mat

    private class HSV(val h: Mat, val s: Mat, val v: Mat) {
        val list : List<Mat> = listOf(h, s, v)
    }

    companion object {
        val lowerYellow: Scalar = Scalar(15.0, 100.0, 100.0)
        val upperYellow: Scalar = Scalar(30.0, 255.0, 255.0)
    }

    override fun processFrame(input: Mat?): Mat {
        Imgproc.cvtColor(input, matHSV, Imgproc.COLOR_BGR2HSV)

        val dummyChannels: List<Mat> = ArrayList()

        Core.split(matHSV, dummyChannels)

        val hsvSplit: HSV = HSV(
                h = dummyChannels[0],
                s = dummyChannels[1],
                v = dummyChannels[2]
        )

        Imgproc.equalizeHist(hsvSplit.v, hsvSplit.v)

        Core.merge(hsvSplit.list, matHSV)

        var mask = Mat()
        var res = Mat()
        Core.inRange(matHSV, lowerYellow, upperYellow, mask)

        Core.bitwise_and(input, input, res, mask)

        var contours: List<MatOfPoint> = ArrayList()
        var hierarchy = Mat()

        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

        Imgproc.drawContours(res, contours, -1, Scalar(0.0, 255.0, 0.0), 3)

        val widths: MutableList<Double> = ArrayList()
        for (c: MatOfPoint in contours) {
//            var copy: MatOfPoint2f = MatOfPoint2f()
//            c.convertTo(copy, CvType.CV_32F)

            val copy: MatOfPoint2f = MatOfPoint2f(*c.toArray())

            val rect: RotatedRect = Imgproc.minAreaRect(copy)

            val vertices: Array<Point> = Array(size = 4) { Point() }

            rect.points(vertices)
            vertices.sort()
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

        val copy: MatOfPoint2f = MatOfPoint2f(*contours[maxI].toArray())
        val rect: RotatedRect = Imgproc.minAreaRect(copy)

        val vertices: Array<Point> = Array(size = 4) { Point() }
        rect.points(vertices)

        val boxContours: MutableList<MatOfPoint> = ArrayList()
        boxContours.add(MatOfPoint(*vertices))

        Imgproc.drawContours(res, boxContours, 0, Scalar(0.0, 0.0, 255.0), 2)

        var maxX = 0
        var maxY = 0
        var weight = 0

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
            Imgproc.rectangle(res,
                    Point((x - width).toDouble(), (y - height).toDouble()),
                    Point(x.toDouble(), y.toDouble()),
                    Scalar(255.0, 0.0, 0.0),
                    5
            )
        }

        return input!!
    }

}