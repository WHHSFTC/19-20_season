package org.firstinspires.ftc.teamcode.implementations;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.*;

@Deprecated
public class VisionFromWallJava extends OpenCvPipeline {
    enum PPosition {
        RIGHT, LEFT, MIDDLE, NULL;
    }

    final Scalar lowerYellow = new Scalar(15, 100, 100);
    final Scalar upperYellow = new Scalar(30, 255, 255);

    private PPosition skyStonePosition = PPosition.NULL;

    private Telemetry telemetry;
    private Mat matHSV;

    public VisionFromWallJava(Telemetry tl) {
        this.telemetry = tl;
        matHSV = new Mat();
    }

    public PPosition getPosition() {
        return skyStonePosition;
    }

    @Override
    public Mat processFrame(Mat input) {
        Mat ret = new Mat();
        try {
            Imgproc.cvtColor(input, matHSV, Imgproc.COLOR_BGR2HSV);

            ArrayList<Mat> channels = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                channels.add(new Mat());
            }
            Core.split(matHSV, channels);

            Mat h = channels.get(0);
            Mat s = channels.get(1);
            Mat v = channels.get(2);

            Imgproc.equalizeHist(v, v);

            List<Mat> combined = new ArrayList<>();
            combined.add(h);
            combined.add(s);
            combined.add(v);

            Core.merge(combined, matHSV);

            Mat mask = new Mat();

            Core.inRange(matHSV, lowerYellow, upperYellow, mask);

            Core.bitwise_and(input, input, ret, mask);

            List<MatOfPoint> contours = new ArrayList<>();

            Mat heirarchy = new Mat();

            Imgproc.findContours(mask, contours, heirarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);

            Imgproc.drawContours(ret, contours, -1, new Scalar(0, 255, 0), 3);

            List<Integer> widths = new ArrayList<>();

            for (MatOfPoint c : contours) {
                MatOfPoint2f copy = new MatOfPoint2f(c.toArray());
                RotatedRect rect = Imgproc.minAreaRect(copy);

                MatOfPoint box = new MatOfPoint();
                Imgproc.boxPoints(rect, box);

                Core.MinMaxLocResult a = Core.minMaxLoc(box);

                widths.add((int)(a.maxVal - a.minVal));
            }

            int maxWidth = 0;
            int maxI = 0;

            for (int i = 0; i < widths.size(); i++) {
                if (widths.get(i) > maxWidth) {
                    maxWidth = widths.get(i);
                    maxI = i;
                }
            }

            MatOfPoint2f copy1 = new MatOfPoint2f(contours.get(maxI).toArray());
            RotatedRect rect1 = Imgproc.minAreaRect(copy1);

            Point[] vertices = new Point[4];
            rect1.points(vertices);

            List<MatOfPoint> boxes = new ArrayList<>();
            boxes.add(new MatOfPoint(vertices));

            Imgproc.drawContours(ret, boxes, 0, new Scalar(0, 0, 255), 2);

            Point[] temp = boxes.get(0).toArray();

            Arrays.sort(temp, (p1, p2) -> (int) ((p1.x + p1.y) - (p2.x + p2.y)));

            Point botRight = temp[temp.length - 1];
            Point topLeft = temp[0];
            Point botLeft = temp[1];

            double m = (botLeft.y - botRight.y) / (botLeft.x - botRight.x);

            int xOffSet = 50;
            int yOffset = 25;
            int width = 25;
            int height = 25;

            PPosition location = PPosition.NULL;

            for (int i = 0; i < 3; i++ ) {
                int x = (int)botRight.x - 175 * i - xOffSet;
                int y = (int)(m * (x - botRight.x) + botRight.y - yOffset);
                Imgproc.rectangle(ret,
                        new Point((x-width), (y-height)),
                        new Point(x, y),
                        new Scalar(255, 0, 0),
                        5
                );

                Scalar avg = Core.mean(mask.rowRange(y - height, y).colRange(x - width, x));
                location = Arrays.stream(avg.val).sum() == 0 ? PPosition.values()[i] : location;
            }

            skyStonePosition = location;

        } catch (Exception e) {
            telemetry.addLine("[ERROR] " + e);
            telemetry.update();
        }

        return ret;
    }
}
