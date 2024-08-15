package org.firstinspires.ftc.teamcode.teamCode;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.teamCode.Classes.BlueOpenCVPipeline;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Intake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.teamCode.Classes.RedFarDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.List;

@Config
@Autonomous

public class BlueBackdropPoateMerge extends LinearOpMode {
    AutoController autoController;
    public static int time_preloads = 800;
    public static int time_preloads2 = 1000;
    public static int time_safe = 2000;
    public static int time_collect = 2500;
    public static int time_collect1 = 300;

    public static double funny_heading1 = -3;
    public static double funny_heading2 = -10;
    public static int time_collect2 = 300;
    public static int time_pixel1 = 1000;
    public static int time_pixel2 = 300;
    public static int time_place = 250;
    public static int time_place2 = 500;
    public static int time_at_stack = 2000;
    public static int time_to_collect = 1000;
    public static int pixel_count=5;
    public BlueOpenCVPipeline blueOpenCVPipeline;

    ElapsedTime timer;
    ElapsedTime timer_collect;
    ElapsedTime time_left_of_auto;
    MecanumDrive drive;

    enum State {

        NOTHING,
        GOING_PRELOADS,
        PRELOADS,
        GOING_SAFE_COLLECT,
        SAFE_COLLECT,
        GOING_COLLECT,
        COLLECTING,
        GOING_SAFE_SCORE,
        SAFE_SCORE,
        GOING_SCORE,
        PARK, SCORE
    }
    public enum CYCLE_NO {
        CYCLE_1,
        CYCLE_2,
        CYCLE_3,
        PARK,
    }

    CYCLE_NO noOfCycle = CYCLE_NO.CYCLE_1;

    State CS = State.NOTHING, PS = State.NOTHING; //currentState/previousState

    public static double x_start = 15.5, y_start = 64, angle_start = 90;
    public static double x_purple_preload_right = 52, y_purple_preload_right = -38, angle_purple_preload_right = 171;
    public static double x_purple_preload_center = 52, y_purple_preload_center = -31.5, angle_purple_preload_center = 165;
    public static double x_purple_preload_left = 52, y_purple_preload_left = -28, angle_purple_preload_left = 180;

    public static double x_yellow_preload_right = 41, y_yellow_preload_right = -49, angle_yellow_preload_right = 182;
    public static double x_yellow_preload_center = 41, y_yellow_preload_center = -29, angle_yellow_preload_center = 180;
    public static double x_yellow_preload_left = 41, y_yellow_preload_left = -25, angle_yellow_preload_left = 180;

    public static double x_collect = -24, y_collect = -7.8, angle_collect = 180;
    public static double x_collect2 = -27, y_collect2 = -7.8, angle_collect2 = 182;
    public static double x_collect3 = -27, y_collect3 = -7.8, angle_collect3 = 180;
    public static double x_score = 52, y_score = -24.5, angle_score = 210;

    public static double x_safe = 20, y_safe = -11, angle_safe = 180;
    public static double x_safe2 = 20, y_safe2 = -14, angle_safe2 = 180;
    public static int poz_extendo_preloads_left = 1220;
    public static int poz_extendo_preloads_center = 900;
    public static int poz_extendo_preloads_right = 400;

    public static int poz_extendo_collect = 1250;
    public static int poz_extendo_collect2 = 1250;
    public static int poz_extendo_reverse_intake = 1300;
    public boolean exitLoop = false;
    public RedFarDetectionPipeline.Location location;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        timer = new ElapsedTime();
        time_left_of_auto = new ElapsedTime();
        timer_collect = new ElapsedTime();
        autoController = new AutoController(hardwareMap);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()
        );
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class,"Webcam 1"), cameraMonitorViewId);
        RedFarDetectionPipeline blueOpenCVPipeline = new RedFarDetectionPipeline(telemetry, true);
        camera.setPipeline(blueOpenCVPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened(){
                camera.startStreaming(960, 720, OpenCvCameraRotation.SENSOR_NATIVE);

            }
            @Override
            public void onError(int errorCode){
                //Teapa coae
            }
        });
        FtcDashboard.getInstance().startCameraStream(camera, 0);
        double voltage;
        VoltageSensor batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();
        voltage = batteryVoltageSensor.getVoltage();


        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);

        }

        Pose2d start_pose = new Pose2d(x_start, y_start,Math.toRadians(angle_start));

        Pose2d purpleLeft = new Pose2d(x_purple_preload_left, y_purple_preload_left, Math.toRadians(angle_purple_preload_left));
        Pose2d purpleCenter = new Pose2d(x_purple_preload_center, y_purple_preload_center, Math.toRadians(angle_purple_preload_center));
        Pose2d purpleRight = new Pose2d(x_purple_preload_right, y_purple_preload_right, Math.toRadians(angle_purple_preload_right));

        Pose2d interCollectFirstCycle = new Pose2d(x_safe, y_safe, Math.toRadians(angle_safe));

        Pose2d safe = new Pose2d(x_safe, y_safe, Math.toRadians(angle_safe));
        Pose2d safe2 = new Pose2d(x_safe2, y_safe2, Math.toRadians(angle_safe2));

        Pose2d collect = new Pose2d(x_collect, y_collect, -angle_collect);
        Pose2d collect2 = new Pose2d(x_collect2, y_collect2, -angle_collect2);
        Pose2d collect3 = new Pose2d(x_collect3, y_collect3, -angle_collect3);

        Pose2d score = new Pose2d(x_score, y_score, angle_score);


        drive = new MecanumDrive(hardwareMap, start_pose);


        Action goToPreloadsLeft = drive.actionBuilder(start_pose)
                .strafeToLinearHeading(new Vector2d(purpleLeft.position.x, -purpleLeft.position.y), purpleLeft.heading.inverse())
                .build();

        Action goToPreloadsCenter = drive.actionBuilder(start_pose)
                .strafeToLinearHeading(new Vector2d(purpleCenter.position.x, -purpleLeft.position.y), purpleCenter.heading.inverse())
                .build();

        Action goToPreloadsRight = drive.actionBuilder(start_pose)
                .strafeToLinearHeading(new Vector2d(purpleRight.position.x, -purpleRight.position.y), purpleRight.heading.inverse())
                .build();


        autoController.start();

        autoController.extendo.goToDrive();
        autoController.outtake.goToMoving();
        autoController.outtake.claw.goToPlace();

        while (opModeInInit()) {

            sleep(20);
            location = blueOpenCVPipeline.getLocation();
            //DETECTION
            drive.lazyImu.get().resetYaw();

            telemetry.addData("case", location.toString());
            telemetry.update();
            sleep(50);
        }

        waitForStart();

        CS = State.GOING_PRELOADS;

        time_left_of_auto.reset();
        while (opModeIsActive() && !isStopRequested() && !exitLoop) {
            try{
                switch (CS) {
                    case GOING_PRELOADS:

                        autoController.outtake.goToPreloads();
                        switch(location) {
                            case LEFT:
                                Actions.runBlocking(goToPreloadsLeft);
                                break;
                            case MIDDLE:
                                Actions.runBlocking(goToPreloadsCenter);
                                break;
                            case RIGHT:
                                Actions.runBlocking(goToPreloadsRight);
                                break;
                        }
                        CS = State.PRELOADS;
                        timer.reset();

                        break;

                    case PRELOADS:
                        switch(location)
                        {
                            case LEFT:
                                autoController.extendo.goToPoz(poz_extendo_preloads_left);
                                break;
                            case MIDDLE:
                                autoController.extendo.goToPoz(poz_extendo_preloads_center);
                                break;
                            case RIGHT:
                                autoController.extendo.goToPoz(poz_extendo_preloads_right);
                                break;
                        }
                        if(timer.milliseconds() > time_preloads) {
                            autoController.intake.currentState = IntakeSubsystem.State.AUTO_GOT_PIXELS_WAITING; //asta da reverse
                            autoController.outtake.claw.goToIntake();
                        }
                        if(timer.milliseconds() > time_preloads2) {
                            autoController.outtake.goToMoving();
                            autoController.extendo.goToDrive();
                            CS = State.GOING_SAFE_COLLECT;
                        }
//                        CS=State.PARK;
                        break;

                    case GOING_SAFE_COLLECT:

                        if(noOfCycle == CYCLE_NO.CYCLE_1) {
                            Actions.runBlocking(
                                    drive.actionBuilder(drive.pose)
                                            .strafeToLinearHeading(new Vector2d(safe.position.x, -safe.position.y), safe.heading.inverse())
                                            .build()
                            );
                        } else {
                            Actions.runBlocking(
                                    drive.actionBuilder(drive.pose)
                                            .strafeToLinearHeading(new Vector2d(safe2.position.x, -safe2.position.y), safe2.heading.inverse())
                                            .build()
                            );
                        }
                        CS = State.SAFE_COLLECT;
                        timer.reset();
                        break;

                    case SAFE_COLLECT:
//                    drive.lazyImu.get().resetYaw();
                        if(30-time_left_of_auto.seconds()<8)
                            CS = State.PARK;
                        else CS = State.GOING_COLLECT;

                        break;

                    case GOING_COLLECT:

                        switch(noOfCycle)
                        {
                            case CYCLE_1:
//                                Actions.runBlocking(new ParallelAction(drive.actionBuilder(drive.pose)
//                                        .strafeToLinearHeading(collect.position, collect.heading)
//                                        .build(), new InstantAction(() -> {
//                                    autoController.extendo.goToPoz(poz_extendo_collect);
//                                    autoController.intake.takePixelAuto(autoController.lastPixel);
//                                })));
//                                noOfCycle = CYCLE_NO.CYCLE_2;
                                Actions.runBlocking(drive.actionBuilder(drive.pose).lineToX(collect.position.x)
                                        .build());
                                autoController.extendo.goToPoz(poz_extendo_collect);
                                autoController.intake.intake4Bar.goTo(Intake4Bar.POSE.pixel5);
                                autoController.lastPixel = Intake4Bar.POSE.pixel5;
                                autoController.intake.takePixelAuto(autoController.lastPixel);
                                sleep(150);
                                break;
                            case CYCLE_2:
                                Actions.runBlocking(drive.actionBuilder(drive.pose).lineToX(collect2.position.x)
                                        .build());
                                autoController.extendo.goToPoz(poz_extendo_collect);
                                autoController.intake.intake4Bar.goTo(Intake4Bar.POSE.pixel3);
                                autoController.lastPixel = Intake4Bar.POSE.pixel3;
                                autoController.intake.takePixelAuto(autoController.lastPixel);
                                break;
                            case CYCLE_3:
//                                Actions.runBlocking(new ParallelAction(drive.actionBuilder(drive.pose)
//                                        .strafeToLinearHeading(collect.position, collect.heading)
//                                        .build(), new InstantAction(() -> {
//                                    autoController.extendo.goToPoz(poz_extendo_collect);
//                                    autoController.lastPixel = Intake4Bar.POSE.pixel1;
//                                    autoController.intake.takePixelAuto(autoController.lastPixel);
//                                })));
                                autoController.extendo.goToPoz(poz_extendo_collect);
                                autoController.lastPixel = Intake4Bar.POSE.pixel1;
                                autoController.intake.takePixelAuto(autoController.lastPixel);
                                Actions.runBlocking(drive.actionBuilder(safe).lineToX(collect3.position.x)
                                        .build());

                                noOfCycle = CYCLE_NO.PARK;

                        }
//                        sleep(500);
                        CS = State.COLLECTING;
                        timer.reset();
                        timer_collect.reset();
                        break;

                    case COLLECTING:
                        autoController.intake.closeLatch();

//                        while(autoController.intake.pololuSensor.detect()<2 && timer.milliseconds() < time_at_stack)
//                        {
//                            autoController.extendo.setPower(0.5);
//                            autoController.takePixelFromStack(pixel_count);
//                            pixel_count--;
//                        }

//                        if(timer.milliseconds() < time_collect1) {
//                            autoController.takePixelFromStack(pixel_count);
//                            pixel_count--;
//                        }
                        switch(noOfCycle){
                            case CYCLE_1:
                                if(timer.milliseconds() < time_collect1) {
                                    autoController.intake.intakeController.turnOn();
                                    autoController.takePixelC1();
                                    Actions.runBlocking(drive.actionBuilder(drive.pose).strafeToLinearHeading(new Vector2d(collect.position.x, 14 ), funny_heading1).build());
                                }
                            case CYCLE_2:
                                if(timer.milliseconds() < time_collect2) {
//                            autoController.intake.takePixelCycle1();8
                                    autoController.intake.intakeController.turnOn();
                                    autoController.takePixelC2();
                                    Actions.runBlocking(drive.actionBuilder(drive.pose).strafeToLinearHeading(new Vector2d(collect.position.x, 14), funny_heading1).build());
                                }
                        }

//|| timer_collect.milliseconds() < time_at_stack
                        //autoController.intake.pololuSensor.detect() == 2 || autoController.lastPixel == Intake4Bar.POSE.pixel0 ||
                        if(timer.milliseconds() > time_at_stack) {
                            autoController.extendo.goToPoz(poz_extendo_collect);
//                            autoController.intake.intakeController.reverse();
//                            sleep(150);
                            autoController.intake.currentState = IntakeSubsystem.State.AUTO_OFF;
                            autoController.intake.intakeController.turnOn();
                            sleep(150);
                            autoController.intake.holdLatch();
                            noOfCycle =  noOfCycle == CYCLE_NO.CYCLE_1 ? CYCLE_NO.CYCLE_2 : CYCLE_NO.PARK;
                            CS = State.GOING_SAFE_SCORE;
                            autoController.extendo.goDown();
                        }

//                        autoController.intake.closeLatch();
//                        switch (noOfCycle) {
//                            case CYCLE_1:
//                                if(autoController.intake.currentState != AUTO_FULL && timer_collect.milliseconds()<time_at_stack) {
//                                    autoController.intake.takePixelCycle1();
//                                }else {
//                                    autoController.currentState = AutoController.TransferState.SLIDES_RETRACTING;
//                                    noOfCycle=CYCLE_NO.PARK;
//                                    CS = State.GOING_SAFE_SCORE;
////                                    autoController.extendo.goDown();
//                                }
//                        }
                        break;
                    case GOING_SAFE_SCORE:

                        autoController.extendo.goDown();
                        autoController.intake.intakeController.reverse();
                        sleep(100);
                        //autoController.intake.turnOff();
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        .strafeToLinearHeading(new Vector2d(safe.position.x, -safe.position.y), safe.heading.inverse())
                                        .build()
                        );

                        timer.reset();
                        autoController.intake.intakeController.turnOff();
                        autoController.intake.openLatch();
                        autoController.startTransfer();
                        sleep(300);
                        autoController.intake.openLatch();
                        autoController.outtake.goToIntake();
                        autoController.startTransfer();
                        CS = State.GOING_SCORE;
                        sleep(1500);
                        break;

//                    case SAFE_SCORE:
//
//                        if(timer.milliseconds() > time_safe) {
//                            //autoController.startTransfer();
//                            CS = State.GOING_SCORE;
//                        }
//                        break;

                    case GOING_SCORE:

                        autoController.outtake.goToPlace();
                        autoController.outtake.rotation.go90Deg();
                        autoController.lift.goToPoz(650);
                        autoController.extendo.goToDrive();

                        Actions.runBlocking(drive.actionBuilder(new Pose2d(safe.position.x, -safe.position.y, safe.heading.inverse().toDouble()))
                                .strafeToLinearHeading(new Vector2d(score.position.x, -score.position.y), score.heading.inverse())
                                .build());

                        CS = State.SCORE;
                        timer.reset();
                        break;
                    case PARK:
                        autoController.runThread.set(false);
                        exitLoop = true;
                        break;
                    case SCORE:
                        if(timer.milliseconds() > time_place) {
                            autoController.outtake.claw.goToIntake();
                        }

                        if(timer.milliseconds() > time_pixel2) {
                            autoController.outtake.goToMoving();
                            autoController.lift.goDown();
                            switch(noOfCycle) {
                                case PARK:
                                    CS = State.PARK;
                                    break;
                                default:
                                    CS = State.GOING_SAFE_COLLECT;
                            }
                        }
                }
            } catch (Exception e) {
//                autoController.interrupt();
                autoController.runThread.set(false);
            }

            telemetry.addData("state", CS);
            telemetry.addData("timer", timer.milliseconds());
            telemetry.addData("time_left", 30-time_left_of_auto.seconds());
            telemetry.addData("intake state", autoController.intake.currentState.toString());
            telemetry.addData("timer collect" ,timer_collect.milliseconds());
            telemetry.update();
            drive.updatePoseEstimate();
        }
        if (isStopRequested()) {
            autoController.runThread.set(false);
        }
        autoController.runThread.set(false);
    }
}
