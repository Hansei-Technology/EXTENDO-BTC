package org.firstinspires.ftc.teamcode.teamCode;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Intake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeSubsystem;

@Config
public class AutoController extends Thread{

    public Intake4Bar.POSE lastPixel = Intake4Bar.POSE.pixel5;
    LiftController lift;
    public IntakeSubsystem intake;
    public OuttakeSubsystem outtake;
    ExtendoControllerPID extendo;
    
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

    public static int time_to_ready_outtake = 300;
    public static int time_outtake_down = 350;
    public static int time_for_claw = 300;
    public static int time_outtake_up = 350;
    
    public static double nr_of_cycle = 0;

    @Override
    public void run() {

        while (!isInterrupted()) {
            update();
        }
    }

    public void takeNextPixel() {
        intake.takePixelAuto(lastPixel);
        lastPixel = intake.intake4Bar.goDown1pixel(lastPixel);
    }

    public void update() {
        
        lift.update();
        extendo.update();
        intake.updateAuto();
        transferUpdate();
    }

    public void startTransfer() {
        currentState = TransferState.SLIDES_RETRACTING;
        lift.goDown();
        extendo.goDown();
        outtake.claw.goToIntake();
        outtake.goToMoving();
        transferTimer.reset();
        intake.openLatch();
    }
    
    public AutoController(HardwareMap map) {
        lift = new LiftController(map);
        intake = new IntakeSubsystem(map);
        outtake = new OuttakeSubsystem(map);
        extendo = new ExtendoControllerPID(map);
        transferTimer = new ElapsedTime();
    }


    void transferUpdate() {
        switch (currentState) {
//            case NO_TRANSFER:
//
//                if(intake.isFull()) {
//                    //currentState = TransferState.SLIDES_RETRACTING;
//                    outtake.goToMoving();
//                    outtake.claw.goToIntake();
//                    transferTimer.reset();
//                }
//                break;

            case SLIDES_RETRACTING:

                if (transferTimer.milliseconds() > time_to_ready_outtake) {
                    currentState = TransferState.OUTTAKE_READY;
                }
                break;

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
                        extendo.goToDrive();
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
