package org.firstinspires.ftc.teamcode.Utils;

import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ctsStartDetection extends OpenCvPipeline {

    Telemetry telemetry;

    public enum PixelPos {
        LEFT,
        CENTER,
        RIGHT,
        NONE
    }

    public enum RobotPosition {
        REDLEFT,
        REDRIGHT,
        BLUELEFT,
        BLUERIGHT
    }

    public PixelPos detectedPos;

    public PixelPos getDetectedPos() {
        return detectedPos;
    }

    public void setRobotPosition (RobotPosition pos) {
        // placeholder values until tested
        switch (pos) {
            case REDLEFT:
                leftTop = new Point(0, 167);
                leftBottom = new Point(38, 205);
                centerTop = new Point(99, 177);
                centerBottom = new Point(161, 212);
                rightTop = new Point(198, 218);
                rightBottom = new Point(255, 182);
                break;
            case REDRIGHT:
                leftTop = new Point(0, 167);
                leftBottom = new Point(38, 205);
                centerTop = new Point(99, 177);
                centerBottom = new Point(161, 212);
                rightTop = new Point(198, 218);
                rightBottom = new Point(255, 182);
                break;
            case BLUELEFT:
                leftTop = new Point(0, 167);
                leftBottom = new Point(38, 205);
                centerTop = new Point(99, 177);
                centerBottom = new Point(161, 212);
                rightTop = new Point(198, 218);
                rightBottom = new Point(255, 182);
                break;
            case BLUERIGHT:
                leftTop = new Point(0, 167);
                leftBottom = new Point(38, 205);
                centerTop = new Point(99, 177);
                centerBottom = new Point(161, 212);
                rightTop = new Point(198, 218);
                rightBottom = new Point(255, 182);
                break;
        }
    }


    public ctsStartDetection(Telemetry telemetry) {
        this.telemetry = telemetry;

    }

    // Regions defined by two corner points
    public Point
            leftTop = new Point(66.6, 119)
            , leftBottom = new Point(34, 143)
            , centerTop = new Point(111, 121)
            , centerBottom = new Point(137, 148)
            , rightTop = new Point(192, 148)
            , rightBottom = new Point(216, 123)
            ;

    public boolean debug = true;

    // HSV color bounds for red and blue
    public Scalar lowerRed = new Scalar(0, 136, 0);
    public Scalar upperRed = new Scalar(11, 255, 255);
    public Scalar lowerBlue = new Scalar(104, 100, 83);
    public Scalar upperBlue = new Scalar(114, 255, 255);

    // Helper method to check if pixel is red or blue
    private boolean isRedOrBlue(double[] pixel) {
        Mat pixelMat = new Mat(1, 1, CvType.CV_8UC3);
        pixelMat.put(0, 0, pixel);

        Mat redMask = new Mat();
        Core.inRange(pixelMat, lowerRed, upperRed, redMask);
        boolean isRed = Core.countNonZero(redMask) > 0;

        Mat blueMask = new Mat();
        Core.inRange(pixelMat, lowerBlue, upperBlue, blueMask);
        boolean isBlue = Core.countNonZero(blueMask) > 0;

        pixelMat.release();
        redMask.release();
        blueMask.release();

        return isRed || isBlue;
    }

    @Override
    public Mat processFrame(Mat inputMat) {
        int width = inputMat.cols();
        int height = inputMat.rows();

        Imgproc.cvtColor(inputMat, inputMat, Imgproc.COLOR_RGBA2RGB);
        Imgproc.GaussianBlur(inputMat, inputMat, new Size(5, 5), 0);

        // Convert to HSV
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(inputMat, hsvMat, Imgproc.COLOR_RGB2HSV);

        // Define regions as rectangles
        Rect leftRegion = new Rect(mapPoint(leftTop, width, height), mapPoint(leftBottom, width, height));
        Rect centerRegion = new Rect(mapPoint(centerTop, width, height), mapPoint(centerBottom, width, height));
        Rect rightRegion = new Rect(mapPoint(rightTop, width, height), mapPoint(rightBottom, width, height));

        // Initialize counters
        int[] sum = new int[3];
        int[] count = new int[3];

        // Process each region
        processRegion(hsvMat, leftRegion, sum, count, 0);
        processRegion(hsvMat, centerRegion, sum, count, 1);
        processRegion(hsvMat, rightRegion, sum, count, 2);

        // Determine highest density
        detectedPos = determinePosition(sum, count);

        // Draw regions and update telemetry
        drawRegions(inputMat, leftRegion, rightRegion, centerRegion, sum);
        telemetry.addData("Detected Position: ", detectedPos);
        telemetry.update();

        return inputMat;
    }

    // Helper method to map 0-255 range points to screen coordinates
    private Point mapPoint(Point p, int width, int height) {
        return new Point(p.x / 255 * width, p.y / 255 * height);
    }

    // Helper method to process each region
    private void processRegion(Mat hsvMat, Rect region, int[] sum, int[] count, int index) {
        byte[] maskData = new byte[3];
        for (int i = region.y; i < region.y + region.height; i += 3) { // Skip every 3 rows
            for (int j = region.x; j < region.x + region.width; j += 3) { // Skip every 3 columns
                hsvMat.get(i, j, maskData);
                if (isRedOrBlue(new double[]{maskData[0] & 0xFF, maskData[1] & 0xFF, maskData[2] & 0xFF})) {
                    sum[index]++;
                }
                count[index]++;
            }
        }
    }

    // Helper method to determine the position with the highest density
    private PixelPos determinePosition(int[] sum, int[] count) {
        int maxDensityIndex = 0;
        double maxDensity = 0;
        for (int i = 0; i < 3; i++) {
            double density = (double) sum[i] / count[i];
            if (density > maxDensity) {
                maxDensity = density;
                maxDensityIndex = i;
            }
        }
        return PixelPos.values()[maxDensityIndex];
    }

    // Helper method to draw regions
    private void drawRegions(Mat img, Rect left, Rect right, Rect center, int[] sum) {
        Scalar red = new Scalar(255, 0, 0);
        Scalar white = new Scalar(255, 255, 255);

        Imgproc.rectangle(img, left, sum[0] > 0 ? red : white);
        Imgproc.rectangle(img, center, sum[1] > 0 ? red : white);
        Imgproc.rectangle(img, right, sum[2] > 0 ? red : white);
    }
}
