package org.firstinspires.ftc.teamcode.configs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeSubsystem;

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
        TRASFER_READY
    }

    TransferState currentState = TransferState.NO_TRANSFER;
    TransferState previousState = TransferState.NO_TRANSFER;

    public static int time_to_ready_outtake = 500;
    public static int time_outtake_down = 200;
    public static int time_for_claw = 100;
    public static int time_outtake_up = 200;

    @Override
    public void runOpMode() throws InterruptedException {
        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        extendo = new ExtendoControllerPID(hardwareMap);
        lift = new LiftController(hardwareMap);
        timer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {
            switch (currentState) {
                case SLIDES_RETRACTING:
                    if (currentState != previousState) {
                        outtake.goToReady();
                        timer.reset();
                        intake.openLatch(); //ro2 au probleme cu asta(exista un state special)
                    }
                    if (extendo.currentState == ExtendoControllerPID.States.RETRACTED &&
                    lift.currentState == LiftController.States.RETRACTED &&
                    timer.milliseconds() > time_to_ready_outtake) {
                        currentState = TransferState.OUTTAKE_READY;
                    }
                    break;
                case OUTTAKE_READY:
                    outtake.goToIntake();
                    currentState = TransferState.WAITING_FOR_OUTTAKE_DOWN;
                    break;
                case WAITING_FOR_OUTTAKE_DOWN:
                    if (currentState != previousState) {
                        timer.reset();
                    }
                    if(timer.milliseconds() > time_outtake_down) {
                        currentState = TransferState.OUTTAKE_IN_PLACE;
                    }
                    break;
                case OUTTAKE_IN_PLACE:
                    outtake.claw.closeLeft();
                    outtake.claw.closeRight();
                    currentState = TransferState.WAITING_FOR_CLAW;
                    break;
                case WAITING_FOR_CLAW:
                    if (currentState != previousState) {
                        timer.reset();
                    }
                    if(timer.milliseconds() > time_for_claw) {
                        currentState = TransferState.WAITING_FOR_OUTTAKE_UP;
                    }
                    break;
                case WAITING_FOR_OUTTAKE_UP:
                    if(currentState != previousState) {
                        timer.reset();
                    }
                    if(timer.milliseconds() > time_outtake_up) {
                        currentState = TransferState.TRASFER_READY;
                    }
                    break;
            }
            previousState = currentState;
        }

    }
}
