package org.firstinspires.ftc.teamcode.teamCode;



import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ClawController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Intake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Outtake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeJoint;
import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeRotation;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Storage;


import java.util.List;

@Config
@Autonomous

public class RedBackdrop extends LinearOpMode {
    AutoController autoController;
    public static int time_preloads = 800;
    public static int time_preloads2 = 1300;
    public static int time_safe = 500;
    public static int time_collect = 2500;
    public static int time_collect1 = 1000;
    public static int time_pixel1 = 1000;
    public static int time_pixel2 = 350;
    public static int time_place = 300;
    public static int time_place2 = 500;
    public static int time_lift = 300;

    public static int extendoPozPreload = 480;
    public static int extendoPozCollect = 1200;

    ElapsedTime timer;
    ElapsedTime timer_score;
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
        SCORE,
        PARK,
    }
    public enum CYCLE_NO {
        CYCLE_1,
        CYCLE_2,
        CYCLE_3,
        PARK
    }

    CYCLE_NO noOfCycle = CYCLE_NO.CYCLE_1;

    State CS = State.NOTHING, PS = State.NOTHING; //currentState/previousState

    public static double x_start = 15.5, y_start = -64, angle_start = -90;
    public static double x_purple_preload_right = 47, y_purple_preload_right = -45, angle_purple_preload_right = 158;
    public static double x_purple_preload_center = 48, y_purple_preload_center = -35, angle_purple_preload_center = 180;
    public static double x_purple_preload_left = 48, y_purple_preload_left = -35, angle_purple_preload_left = 180;

    public static double x_yellow_preload_right = 42, y_yellow_preload_right = -49, angle_yellow_preload_right = 182;
    public static double x_yellow_preload_center = 41, y_yellow_preload_center = -29, angle_yellow_preload_center = 180;
    public static double x_yellow_preload_left = 41, y_yellow_preload_left = -25, angle_yellow_preload_left = 180;

    public static double x_collect = -29.8, y_collect = -7.5, angle_collect = 179;
    public static double x_collect2 = -30.5, y_collect2 = -8, angle_collect2 = 179;
    public static double x_collect3 = -31.5, y_collect3 = -7, angle_collect3 = 181;
    public static double x_score = 47, y_score = -28, angle_score = -160;
    public static double x_park = 48, y_park = -22.5, angle_park = 180;

    public static double x_safe = 22, y_safe = -7.5, angle_safe = 180;
    @Override
    public void runOpMode() throws InterruptedException {

        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        timer = new ElapsedTime();
        autoController = new AutoController(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        double voltage;
        VoltageSensor batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();
        voltage = batteryVoltageSensor.getVoltage();


        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);

        }

        Pose2d start_pose = new Pose2d(x_start, y_start,Math.toRadians(angle_start));

        Pose2d purpleRight = new Pose2d(x_purple_preload_right, y_purple_preload_right, Math.toRadians(angle_purple_preload_right));
        Pose2d purpleCenter = new Pose2d(x_purple_preload_center, y_purple_preload_center, Math.toRadians(angle_purple_preload_center));
        Pose2d purpleLeft = new Pose2d(x_purple_preload_left, y_purple_preload_left, Math.toRadians(angle_purple_preload_left));

        Pose2d interCollectFirstCycle = new Pose2d(x_safe, y_safe, Math.toRadians(angle_safe));

        Pose2d safe = new Pose2d(x_safe, y_safe, Math.toRadians(angle_safe));

        Pose2d collect = new Pose2d(x_collect, y_collect, angle_collect);
        Pose2d collect2 = new Pose2d(x_collect2, y_collect2, angle_collect2);
        Pose2d collect3 = new Pose2d(x_collect3, y_collect3, angle_collect3);

        Pose2d score = new Pose2d(x_score, y_score, angle_score);
        Pose2d park = new Pose2d(x_park, y_park, angle_park);


        drive = new MecanumDrive(hardwareMap, start_pose);


        Action goToPreloads = drive.actionBuilder(start_pose)
                .strafeToLinearHeading(purpleRight.position, purpleRight.heading)
                .build();


        autoController.start();

        autoController.extendo.goToDrive();
        autoController.outtake.goToMoving();
        autoController.outtake.claw.goToPlace();

        while (opModeInInit()) {

            sleep(20);

            //DETECTION

            telemetry.addData("case", "");
            telemetry.update();
            sleep(50);
        }

        waitForStart();

        if (isStopRequested()) {
            autoController.interrupt();
            return;
        }

        CS = State.GOING_PRELOADS;


        while (opModeIsActive() && !isStopRequested()) {

            switch (CS) {
                case GOING_PRELOADS:

                    autoController.outtake.goToPreloads();
                    Actions.runBlocking(goToPreloads);
                    CS = State.PRELOADS;
                    timer.reset();

                    break;

                case PRELOADS:

                    autoController.extendo.goToPoz(extendoPozPreload);
                    if(timer.milliseconds() > time_preloads) {
                        autoController.intake.currentState = IntakeSubsystem.State.AUTO_GOT_PIXELS_WAITING; //asta da reverse
                        autoController.outtake.claw.goToIntake();
                    }
                    if(timer.milliseconds() > time_preloads2) {
                        CS = State.GOING_SAFE_COLLECT;
                        autoController.outtake.goToMoving();
                        autoController.extendo.goToDrive();
                    }
                    break;

                case GOING_SAFE_COLLECT:

                    Actions.runBlocking(
                            drive.actionBuilder(drive.pose)
                                    .strafeToLinearHeading(safe.position, safe.heading)
                                    .build()
                    );
                    CS = State.SAFE_COLLECT;
                    timer.reset();
                    break;

                case SAFE_COLLECT:

                    CS = State.GOING_COLLECT;

                    break;

                case GOING_COLLECT:

//                    Actions.runBlocking(goToCollect);
                    autoController.extendo.goToPoz(extendoPozCollect);
                    autoController.intake.takePixelAuto(autoController.lastPixel);

                    switch(noOfCycle)
                    {
                        case CYCLE_1:
                            Actions.runBlocking(drive.actionBuilder(drive.pose)
                                    .strafeToLinearHeading(collect.position, collect.heading)
                                    .build());
                            noOfCycle = CYCLE_NO.CYCLE_2;
                            break;
                        case CYCLE_2:
                            autoController.lastPixel = Intake4Bar.POSE.pixel3;
                            autoController.intake.takePixelAuto(autoController.lastPixel);

                            Actions.runBlocking(drive.actionBuilder(drive.pose)
                                    .strafeToLinearHeading(collect2.position, collect2.heading)
                                    .build());
                            noOfCycle = CYCLE_NO.PARK;
                            break;
//                        case CYCLE_3:
//                            Actions.runBlocking(drive.actionBuilder(drive.pose)
//                                    .strafeToLinearHeading(collect3.position, collect3.heading)
//                                    .build());

                    }

                    CS = State.COLLECTING;
                    timer.reset();
                    break;

                case COLLECTING:

                    //autoController.extendo.goToPoz(1130);
                    if(timer.milliseconds() > time_collect1) {
                        autoController.takeNextPixel();
                        timer.reset();
                    }

                    if(autoController.intake.pololuSensor.detect() == 2 || autoController.lastPixel == Intake4Bar.POSE.pixel0) {
                        sleep(300);
                        autoController.intake.intakeController.reverse();
                        sleep(300);
                        autoController.intake.currentState = IntakeSubsystem.State.AUTO_OFF;
                        autoController.intake.intakeController.turnOn();

                        CS = State.GOING_SAFE_SCORE;
                        autoController.extendo.goDown();
                    }
                    break;

                case GOING_SAFE_SCORE:

                    //autoController.intake.turnOff();
                    if (autoController.intake.pololuSensor.detect() == 0) {
                        CS = State.GOING_COLLECT;
                        break;
                    }
                    Actions.runBlocking(
                            drive.actionBuilder(drive.pose)
                                    .strafeToLinearHeading(safe.position, safe.heading)
                                    .build()
                    );
                    autoController.intake.intakeController.turnOff();
                    autoController.intake.openLatch();
                    autoController.startTransfer();
                    sleep(300);
                    autoController.outtake.goToIntake();
                    autoController.startTransfer();

                    //INCERCARE DE MAI MULTE ORI
//                    while (autoController.intake.pololuSensor.detect() > 0) {
//                        autoController.startTransfer();
//                        sleep(300);
//                        autoController.outtake.goToIntake();
//                        autoController.startTransfer();
//                        sleep(1000);
//                    }
                    timer.reset();
                    CS = State.SAFE_SCORE;
                    break;

                case SAFE_SCORE:
                    if(timer.milliseconds() > time_safe) {
                        CS = State.GOING_SCORE;
                    }
                    break;

                case GOING_SCORE:

                    autoController.outtake.goToPlace();
                    autoController.outtake.rotation.goLeft();
                    autoController.lift.goToPoz(650);
                    autoController.extendo.goToDrive();

                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .strafeToLinearHeading(score.position, score.heading)
                            .build());
                    CS = State.SCORE;
                    timer.reset();
                    break;

                case SCORE:
                    if(timer.milliseconds() > time_place) {
                        autoController.outtake.claw.goToIntake();
                    }

                    if(timer.milliseconds() > time_pixel2) {
                        autoController.outtake.goToMoving();
                        autoController.lift.goDown();

//                        if (noOfCycle == CYCLE_NO.PARK){
//                            CS = State.PARK;
//                            break;
//                        }
//                        if(timer_score.milliseconds()>time_lift){
//                            CS = State.GOING_SAFE_COLLECT;
//                        }
                        CS = State.GOING_SAFE_COLLECT;
                    }
//                    break;
//                case PARK:
//                    Actions.runBlocking(drive.actionBuilder(drive.pose)
//                            .strafeToLinearHeading(park.position, park.heading)
//                            .build());
//                    CS = State.NOTHING;
//                    break;

            }
            telemetry.addData("state", CS);
            telemetry.addData("timer", timer.milliseconds());
            telemetry.addData("no of cycle", noOfCycle.toString());
            telemetry.update();
            drive.updatePoseEstimate();
        }
        autoController.interrupt();
    }
}
