package org.firstinspires.ftc.teamcode.teamCode;



import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ClawController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Outtake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeJoint;
import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeRotation;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Storage;


import java.util.List;

@Config
@Autonomous

public class AUTONOM_SKELETON extends LinearOpMode {

    LiftController lift;
    OuttakeRotation outtakeRotation;
    Outtake4Bar outtake4Bar;
    OuttakeJoint outtakeJoint;
    IntakeController intake;
    Storage storage;
    ExtendoControllerPID slides;
    ClawController claw;

    enum State {

        NOTHING
    }
    State CS = State.NOTHING, PS = State.NOTHING; //currentState/previousState

    public static double x_start = -43, y_start = -61, angle_start = 270;

    /**
     * purple
     */

    public static double x_purple_preload_right = -40, y_purple_preload_right = -24, angle_purple_preload_right = 171.5;
    public static double x_purple_preload_center = -54, y_purple_preload_center = -26, angle_purple_preload_center = 177;
    public static double x_purple_preload_left = -62, y_purple_preload_left = -26, angle_purple_preload_left = 170;

    /**
     * yellow
     */

    public static double x_yellow_preload_right = 41, y_yellow_preload_right = -49, angle_yellow_preload_right = 182;
    public static double x_yellow_preload_center = 41, y_yellow_preload_center = -29, angle_yellow_preload_center = 180;
    public static double x_yellow_preload_left = 41, y_yellow_preload_left = -25, angle_yellow_preload_left = 180;

    /**
     * collect
     */

    public static double x_collect = -19, y_collect = -7, angle_collect = 180;
    public static double x_collect_middle = -20, y_collect_middle = -6.5, angle_collect_middle = 195;

    public static double x_inter_collect_cycle_2 = 30, y_inter_collect_cycle_2 = -2, angle_inter_collect_cycle_2 = 180;
    public static double x_collect_cycle_2 = -23.5, y_collect_cycle_2 = -5, angle_collect_cycle_2 = 180;

    public static double x_inter_collect_cycle_3 = 30, y_inter_collect_cycle_3 = -10.5, angle_inter_collect_cycle_3 = 182;
    public static double x_collect_cycle_3 = -26, y_collect_cycle_3 = -10.5, angle_collect_cycle_3 = 182;

    /**
     * score
     */

    public static double x_score = 47, y_score = -10, angle_score = 150;

    public static double x_score_second_cycle = 45, y_score_second_cycle = -6, angle_score_second_angle = 140;
    public static double x_score_third_cycle = 45, y_score_third_cycle = -10, angle_score_third_angle = 150;

    /**
     * intern collect
     */

    public static double x_inter_collect_first_cycle = -36, y_inter_collect_first_cycle = 0, angle_inter_collect_first_cycle = 180;

    /**
     * intern score
     */

    public static double x_inter_score_first_cycle = 30, y_inter_score_first_cycle = -7.5, angle_inter_score_first_cycle =180;

    @Override
    public void runOpMode() throws InterruptedException {

        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

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

        Pose2d interCollectFirstCycle = new Pose2d(x_inter_collect_first_cycle, y_inter_collect_first_cycle, Math.toRadians(angle_inter_collect_first_cycle));
        Pose2d interScoreFirstCycle = new Pose2d(x_inter_score_first_cycle, y_inter_score_first_cycle, Math.toRadians(angle_inter_score_first_cycle));

        Pose2d yellowRight = new Pose2d(x_yellow_preload_right, y_yellow_preload_right, Math.toRadians(angle_yellow_preload_right));
        Pose2d yellowCenter = new Pose2d(x_yellow_preload_center, y_yellow_preload_center, Math.toRadians(angle_yellow_preload_center));
        Pose2d yellowLeft = new Pose2d(x_yellow_preload_left, y_yellow_preload_left, Math.toRadians(angle_yellow_preload_left));

        Pose2d collect_inter_cycle2 = new Pose2d(x_inter_collect_cycle_2, y_inter_collect_cycle_2, Math.toRadians(angle_inter_collect_cycle_2));
        Pose2d collect_cycle2 = new Pose2d(x_collect_cycle_2, y_collect_cycle_2, Math.toRadians(angle_collect_cycle_2));

        Pose2d collect_inter_cycle3 = new Pose2d(x_inter_collect_cycle_3, y_inter_collect_cycle_3, Math.toRadians(angle_inter_collect_cycle_3));
        Pose2d collect_cycle3 = new Pose2d(x_collect_cycle_3, y_collect_cycle_3, Math.toRadians(angle_collect_cycle_3));

        Pose2d score_second_cycle = new Pose2d(x_score_second_cycle, y_score_second_cycle, Math.toRadians(angle_score_second_angle));
        Pose2d score_third_cycle = new Pose2d(x_score_third_cycle, y_score_third_cycle, Math.toRadians(angle_score_third_angle));


        lift = new LiftController(hardwareMap);
        outtakeRotation = new OuttakeRotation(hardwareMap);
        outtake4Bar = new Outtake4Bar(hardwareMap);
        outtakeJoint = new OuttakeJoint(hardwareMap);
        intake = new IntakeController(hardwareMap);
        storage = new Storage(hardwareMap);
        slides = new ExtendoControllerPID(hardwareMap);
        claw = new ClawController(hardwareMap);

        MecanumDrive drive = new MecanumDrive(hardwareMap, start_pose);


        Action traiectorie = drive.actionBuilder(start_pose)
                .strafeTo(purpleRight.position)
                .build();
        Action traiectorie2 = drive.actionBuilder(start_pose)
                .strafeTo(purpleRight.position)
                .build();


        while (opModeInInit()) {

            sleep(20);

            //DETECTION

            telemetry.addData("case", "");
            telemetry.update();
            sleep(50);
        }

        waitForStart();


        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {

            Actions.runBlocking(
                    traiectorie
            );

            Actions.runBlocking(
                    new SequentialAction(
                            traiectorie,
                            traiectorie2
                    )
            );

            switch (CS) {
                case NOTHING: {
                    break;
                }
            }

            updateALL();
        }
    }

    void updateALL() {
        lift.update();
        slides.update();
        telemetry.update();
    }
}
