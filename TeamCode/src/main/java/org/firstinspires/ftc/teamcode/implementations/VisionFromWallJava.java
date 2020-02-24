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

public class VisionFromWallJava extends OpenCvPipeline {
    enum PPosition {
        RIGHT, LEFT, MIDDLE, NULL;
    }

    final Scalar lowerYellow = new Scalar(15, 100, 100);
    final Scalar upperYellow = new Scalar(30, 255, 255);

    PPosition sky_stone_position = PPosition.NULL;

    private Telemetry telemetry;
    private Mat matHSV;

    public VisionFromWallJava(Telemetry tl) {
        this.telemetry = tl;
        matHSV = new Mat();
    }

    @Override
    public Mat processFrame(Mat input) {
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

            Mat res = new Mat();

            Core.bitwise_and(input, input, res, mask);

            List<MatOfPoint> contours = new ArrayList<>();

            Mat heirarchy = new Mat();

            Imgproc.findContours(mask, contours, heirarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);

            Imgproc.drawContours(res, contours, -1, new Scalar(0, 255, 0), 3);

            List<Integer> widths = new ArrayList<>();

            for (MatOfPoint c : contours) {
                MatOfPoint2f copy = new MatOfPoint2f(c.toArray());
                RotatedRect rect = Imgproc.minAreaRect(copy);

                MatOfPoint box = new MatOfPoint();
                Imgproc.boxPoints(rect, box);

                Core.MinMaxLocResult a = Core.minMaxLoc(box);

                widths.add((int)(a.maxLoc.x - a.minLoc.x));
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

            MatOfPoint box1 = new MatOfPoint();
            Imgproc.boxPoints(rect1, box1);

            ArrayList<MatOfPoint> boxes = (ArrayList<MatOfPoint>) Arrays.asList(box1);

            Imgproc.drawContours(input, boxes, 0, new Scalar(0, 0, 255), 2);

            Collections.sort(
                    boxes,
                    (mop1, mop2) -> Integer.compare((int)Core.sumElems(mop1).val[0], (int)Core.sumElems(mop2).val[0])
                    );

            Point botRight = boxes.get(boxes.size() - 1).toArray()[0];
            Point topLeft = boxes.get(0).toArray()[0];
            Point botLeft = boxes.get(1).toArray()[0];

            double m = (botLeft.y - botRight.y) / (botLeft.y - botRight.y);

            int xOffSet = 50;
            int yOffset = 25;
            int width = 25;
            int height = 25;

            PPosition location = PPosition.NULL;

            for (int i = 0; i < 3; i++ ) {
                int x = (int)botRight.x - 175 * i - xOffSet;
                int y = (int)(m * (x - botRight.x) + botRight.y - yOffset);
                Imgproc.rectangle(input,
                        new Point((x-width), (y-height)),
                        new Point(x, y),
                        new Scalar(255, 0, 0),
                        5
                        );

                Scalar avg = Core.mean(mask.rowRange(y - height, y).colRange(x - width, x));
                location = Arrays.stream(avg.val).sum() == 0 ? PPosition.values()[i] : location;
            }

            sky_stone_position = location;

        } catch (Exception e) {
            telemetry.addData("[ERROR]", e);
            telemetry.update();
        }

        return input;
    }
}
