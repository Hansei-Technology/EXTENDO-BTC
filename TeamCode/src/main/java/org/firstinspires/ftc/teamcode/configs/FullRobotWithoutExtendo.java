package org.firstinspires.ftc.teamcode.configs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Utils.StickyGamepad;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Intake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeSubsystem;

@TeleOp
public class FullRobotWithoutExtendo extends LinearOpMode {
    LiftController lift;
    IntakeSubsystem intake;
    OuttakeSubsystem outtake;
    MecanumDrive drive;
    StickyGamepad sg1;
    ElapsedTime transferTimer;

    enum TransferState {
        NO_TRANSFER,
        SLIDES_RETRACTING,
        OUTTAKE_READY,
        WAITING_FOR_LATCH,
        WAITING_FOR_OUTTAKE_DOWN,
        OUTTAKE_IN_PLACE,
        WAITING_FOR_CLAW,
        WAITING_FOR_OUTTAKE_UP,
        TRASFER_READY,
        NO_EXTENDO
    }

    TransferState currentState = TransferState.NO_TRANSFER;
    TransferState previousState = TransferState.NO_TRANSFER;

    public static int time_to_ready_outtake = 1000;
    public static int time_outtake_down = 250;
    public static int time_for_claw = 200;
    public static int time_outtake_up = 250;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        lift = new LiftController(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        sg1 = new StickyGamepad(gamepad1, this);
        transferTimer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_down) intake.takePixel(Intake4Bar.POSE.pixel1);

            if (gamepad1.a) lift.goDown();
            if (gamepad1.b)  {
                lift.goToLow();
                outtake.goToPlace();
            }
            lift.setPower(gamepad1.right_trigger - gamepad1.left_trigger);

//            if (gamepad1.y) {
//                outtake.goToReady();
//                intake.openLatch();
//            }
//            if (gamepad1.x) outtake.goToIntake();

            if(gamepad1.x) outtake.goToMoving();

            if (sg1.left_bumper) outtake.claw.toggleLeft();
            if (sg1.right_bumper) outtake.claw.toggleRight();

            if(gamepad1.y) {
                currentState = TransferState.NO_EXTENDO;
                outtake.claw.goToIntake();
                outtake.goToMoving();
                transferTimer.reset();
                intake.openLatch(); //ro2 au probleme cu asta(exista un state special)
            }


            transferUpdate();
            drive.robotCentric(gamepad1);
            lift.update();
            intake.update();
            sg1.update();

            telemetry.addData("transfer", currentState);
            telemetry.addData("intke", intake.currentState);
            telemetry.update();
        }
    }

    void transferUpdate() {
        switch (currentState) {
            case NO_TRANSFER:

                if(intake.isFull()) {
                    currentState = TransferState.NO_EXTENDO;
                    outtake.goToMoving();
                    outtake.claw.goToIntake();
                    transferTimer.reset();
                }
                break;

            case NO_EXTENDO:

                if(transferTimer.milliseconds() > time_to_ready_outtake) {
                    currentState = TransferState.OUTTAKE_READY;
                }
                break;

//            case SLIDES_RETRACTING:
//
//                if (extendo.currentState == ExtendoControllerPID.States.RETRACTED &&
//                        lift.currentState == LiftController.States.RETRACTED &&
//                        transferTimer.milliseconds() > time_to_ready_outtake) {
//                    currentState = TransferState.OUTTAKE_READY;
//                }
//                break;

            case OUTTAKE_READY:

                outtake.goToIntake();
                currentState = TransferState.WAITING_FOR_OUTTAKE_DOWN;
                transferTimer.reset();
                break;

            case WAITING_FOR_OUTTAKE_DOWN:

                if(transferTimer.milliseconds() > time_outtake_down) {
                    currentState = TransferState.OUTTAKE_IN_PLACE;
                }
                break;

            case OUTTAKE_IN_PLACE:

                outtake.claw.goToPlace();
                currentState = TransferState.WAITING_FOR_CLAW;
                transferTimer.reset();
                break;

            case WAITING_FOR_CLAW:

                if(transferTimer.milliseconds() > time_for_claw) {
                    currentState = TransferState.WAITING_FOR_OUTTAKE_UP;
                    outtake.goToMoving();
                    transferTimer.reset();
                }
                break;

            case WAITING_FOR_OUTTAKE_UP:

                if(transferTimer.milliseconds() > time_outtake_up) {
                    if(intake.pololuSensor.detect() == 0) {
                        currentState = TransferState.NO_TRANSFER;
                        intake.closeLatch();
                    } else {
                        //NU A REUSIT SA IA PIXELII
                        currentState = TransferState.NO_TRANSFER;
                    }
                    
                }
                break;

        }
        previousState = currentState;
    }

}
