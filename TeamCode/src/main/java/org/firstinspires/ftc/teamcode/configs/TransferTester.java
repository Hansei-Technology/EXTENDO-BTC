package org.firstinspires.ftc.teamcode.configs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeSubsystem;

@Config
@TeleOp
public class TransferTester extends LinearOpMode {
    IntakeSubsystem intake;
    OuttakeSubsystem outtake;
    ExtendoControllerPID extendo;
    LiftController lift;
    ElapsedTime timer;

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
    public static int time_outtake_down = 1000;
    public static int time_for_claw = 1000;
    public static int time_outtake_up = 1000;

    @Override
    public void runOpMode() throws InterruptedException {
        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        extendo = new ExtendoControllerPID(hardwareMap);
        lift = new LiftController(hardwareMap);
        timer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {

            if(gamepad1.a) {
                outtake.claw.goToIntake();
            }

            if(gamepad1.y) {
                currentState = TransferState.NO_EXTENDO;
                outtake.goToReady();
                timer.reset();
                intake.openLatch(); //ro2 au probleme cu asta(exista un state special)
            }

            transferUpdate();

            telemetry.addData("state", currentState);
            telemetry.addData("timer", timer.milliseconds());
            telemetry.update();
        }
    }
    void transferUpdate() {
        switch (currentState) {
            case NO_EXTENDO:

                if(timer.milliseconds() > time_to_ready_outtake) {
                    currentState = TransferState.OUTTAKE_READY;
                }
                break;

            case SLIDES_RETRACTING:

                if (extendo.currentState == ExtendoControllerPID.States.RETRACTED &&
                        lift.currentState == LiftController.States.RETRACTED &&
                        timer.milliseconds() > time_to_ready_outtake) {
                    currentState = TransferState.OUTTAKE_READY;
                }
                break;

            case OUTTAKE_READY:

                outtake.goToIntake();
                currentState = TransferState.WAITING_FOR_OUTTAKE_DOWN;
                timer.reset();
                break;

            case WAITING_FOR_OUTTAKE_DOWN:

                if(timer.milliseconds() > time_outtake_down) {
                    currentState = TransferState.OUTTAKE_IN_PLACE;
                }
                break;

            case OUTTAKE_IN_PLACE:

                outtake.claw.goToPlace();
                currentState = TransferState.WAITING_FOR_CLAW;
                timer.reset();
                break;

            case WAITING_FOR_CLAW:

                if(timer.milliseconds() > time_for_claw) {
                    currentState = TransferState.WAITING_FOR_OUTTAKE_UP;
                    outtake.goToReady();
                    timer.reset();
                }
                break;

            case WAITING_FOR_OUTTAKE_UP:

                if(timer.milliseconds() > time_outtake_up) {
                    currentState = TransferState.TRASFER_READY;
                }
                break;

        }
        previousState = currentState;
    }
}
