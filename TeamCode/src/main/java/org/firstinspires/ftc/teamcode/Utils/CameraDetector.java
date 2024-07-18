package org.firstinspires.ftc.teamcode.Utils;

import com.acmerobotics.dashboard.FtcDashboard;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;


public class CameraDetector
{
    public enum Result
    {
        NONE,
        LEFT,
        CENTER,
        RIGHT
    }



    public OpenCvWebcam camera;
    public ctsStartDetection ctsStartDetectionPipeline;

    public CameraDetector(OpenCvWebcam camera)
    {
        this.camera = camera;

        ctsStartDetectionPipeline = new ctsStartDetection(dashboard.getTelemetry());

        camera.setPipeline(ctsStartDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);
                dashboard.startCameraStream(camera, 120);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
    }

    FtcDashboard dashboard = FtcDashboard.getInstance();
//    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
//    double fx = 578.272;
//    double fy = 578.272;
//    double cx = 402.145;
//    double cy = 221.506;

    // UNITS ARE METERS
//    double tagsize = 0.166;

    // IDs of sleves

//    int Left = 1, Middle = 2, Right = 3;

//    AprilTagDetection tagOfInterest = null;



    public Result detect()
    {

//         * The INIT-loop:
//         * This REPLACES waitForStart!
        ctsStartDetection.PixelPos currentDetections = ctsStartDetectionPipeline.getDetectedPos();

        if(currentDetections == ctsStartDetection.PixelPos.RIGHT)
            return Result.RIGHT;
        else if (currentDetections == ctsStartDetection.PixelPos.LEFT)
            return Result.LEFT;
        else if (currentDetections == ctsStartDetection.PixelPos.CENTER)
            return Result.CENTER;
        else
            return Result.NONE;

    }

    public void stop()
    {
        dashboard.stopCameraStream();
        camera.stopStreaming();
        camera.closeCameraDevice();
    }
}
