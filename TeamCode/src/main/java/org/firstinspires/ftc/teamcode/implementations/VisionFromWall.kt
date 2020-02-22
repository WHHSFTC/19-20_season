package org.firstinspires.ftc.teamcode.implementations

import org.firstinspires.ftc.teamcode.interfaces.OpModeIF
import org.openftc.easyopencv.OpenCvPipeline
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.lang.Exception

class VisionFromWall(val opmode: OpModeIF) : OpenCvPipeline() {
    enum class Position {
        RIGHT, LEFT, MIDDLE, NULL
    }

    private class HSV(val h: Mat, val s: Mat, val v: Mat) {
        val list : List<Mat> = listOf(h, s, v)
    }

    companion object {
        val lowerYellow: Scalar = Scalar(15.0, 100.0, 100.0)
        val upperYellow: Scalar = Scalar(30.0, 255.0, 255.0)
    }

    var POSITION: Position = Position.NULL
        private set

    override fun processFrame(input: Mat): Mat {
        try {
            opmode.telemetry.addLine("Position 1")
            opmode.telemetry.update()

            var matHSV = Mat()

            Imgproc.cvtColor(input, matHSV, Imgproc.COLOR_BGR2HSV)

            opmode.telemetry.addLine("Position 2")
            opmode.telemetry.update()

            val dummyChannels: List<Mat> = ArrayList()

            Core.split(matHSV, dummyChannels)

            opmode.telemetry.addLine("Position 3")
            opmode.telemetry.update()

            val hsvSplit = HSV(
                    h = dummyChannels[0],
                    s = dummyChannels[1],
                    v = dummyChannels[2]
            )

            opmode.telemetry.addLine("Position 4")
            opmode.telemetry.update()

            Imgproc.equalizeHist(hsvSplit.v, hsvSplit.v)

            opmode.telemetry.addLine("Position 5")
            opmode.telemetry.update()

            Core.merge(hsvSplit.list, matHSV)

            opmode.telemetry.addLine("Position 6")
            opmode.telemetry.update()

            var mask = Mat()
            var res = Mat()
            Core.inRange(matHSV, lowerYellow, upperYellow, mask)

            opmode.telemetry.addLine("Position 7")
            opmode.telemetry.update()

            Core.bitwise_and(input, input, res, mask)

            opmode.telemetry.addLine("Position 8")
            opmode.telemetry.update()

            var contours: List<MatOfPoint> = ArrayList()
            var hierarchy = Mat()

            opmode.telemetry.addLine("Position 9")
            opmode.telemetry.update()

            Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

            opmode.telemetry.addLine("Position 10")
            opmode.telemetry.update()

            Imgproc.drawContours(input, contours, -1, Scalar(0.0, 255.0, 0.0), 3)

            opmode.telemetry.addLine("Position 11")
            opmode.telemetry.update()

            val widths: MutableList<Double> = ArrayList()

            opmode.telemetry.addLine("Position 12")
            opmode.telemetry.update()

            for (c: MatOfPoint in contours) {
                //            var copy: MatOfPoint2f = MatOfPoint2f()
                //            c.convertTo(copy, CvType.CV_32F)

                opmode.telemetry.addLine("Position Loop: 1")
                opmode.telemetry.update()

                val copy: MatOfPoint2f = MatOfPoint2f(*c.toArray())

                opmode.telemetry.addLine("Position Loop: 2")
                opmode.telemetry.update()

                val rect: RotatedRect = Imgproc.minAreaRect(copy)

                opmode.telemetry.addLine("Position Loop: 3")
                opmode.telemetry.update()

                val vertices: Array<Point> = Array(size = 4) { Point() }

                opmode.telemetry.addLine("Position Loop: 4")
                opmode.telemetry.update()

                rect.points(vertices)
                vertices.sortBy { it.x + it.y }
                widths.add(vertices[-1].x - vertices[0].x)
            }

            opmode.telemetry.addLine("Position 13")
            opmode.telemetry.update()

            var maxWidth = 0.0
            var maxI = 0
            for (i in 0 until widths.size) {
                if (widths[i] > maxWidth) {
                    maxWidth = widths[i]
                    maxI = i
                }
            }

            opmode.telemetry.addLine("Position 14")
            opmode.telemetry.update()

            val copy = MatOfPoint2f()
            contours[maxI].convertTo(copy, CvType.CV_32F)
            val rect: RotatedRect = Imgproc.minAreaRect(copy)

            opmode.telemetry.addLine("Position 15")
            opmode.telemetry.update()

            val vertices: Array<Point> = Array(size = 4) { Point() }
            rect.points(vertices)

            opmode.telemetry.addLine("Position 16")
            opmode.telemetry.update()

            val boxContours: MutableList<MatOfPoint> = ArrayList()
            boxContours.add(MatOfPoint(*vertices))

            opmode.telemetry.addLine("Position 17")
            opmode.telemetry.update()

            Imgproc.drawContours(input, boxContours, 0, Scalar(0.0, 0.0, 255.0), 2)

            opmode.telemetry.addLine("Position 18")
            opmode.telemetry.update()

            boxContours.sortBy { it.toArray().sumBy { p: Point -> (p.x + p.y).toInt() } }

            opmode.telemetry.addLine("Position 19")
            opmode.telemetry.update()

            val botRight = boxContours[-1].toArray()[0]
            val topLeft = boxContours[0].toArray()[0]
            val botLeft = boxContours[1].toArray()[0]

            opmode.telemetry.addLine("Position 20")
            opmode.telemetry.update()

            val m: Double = (botLeft.y - botRight.y) / (botLeft.x - botRight.y)

            val xOffSet = 50
            val yOffSet = 25
            val width = 25
            val height = 25

            var location: Position = Position.NULL

            opmode.telemetry.addLine("Position 21")
            opmode.telemetry.update()

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
                location = if (avg === Scalar(0.0)) Position.values()[i] else location

            }

            POSITION = location
            
        } catch (e: Exception) {
            opmode.telemetry.addData("[ERROR]", e.message)
            opmode.telemetry.update()
        }

        return input
    }

}