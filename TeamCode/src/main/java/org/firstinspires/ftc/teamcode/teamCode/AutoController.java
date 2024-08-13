package org.firstinspires.ftc.teamcode.teamCode;


import static org.firstinspires.ftc.teamcode.teamCode.AutoController.TransferState.NO_TRANSFER;
import static org.firstinspires.ftc.teamcode.teamCode.AutoController.TransferState.OUTTAKE_IN_PLACE;
import static org.firstinspires.ftc.teamcode.teamCode.AutoController.TransferState.OUTTAKE_READY;
import static org.firstinspires.ftc.teamcode.teamCode.AutoController.TransferState.WAITING_FOR_CLAW;
import static org.firstinspires.ftc.teamcode.teamCode.AutoController.TransferState.WAITING_FOR_LATCH;
import static org.firstinspires.ftc.teamcode.teamCode.AutoController.TransferState.WAITING_FOR_OUTTAKE_DOWN;
import static org.firstinspires.ftc.teamcode.teamCode.AutoController.TransferState.WAITING_FOR_OUTTAKE_UP;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Intake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeSubsystem;

import java.util.concurrent.atomic.AtomicBoolean;

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

    TransferState currentState = NO_TRANSFER;
    TransferState previousState = NO_TRANSFER;

    public static int time_to_ready_outtake = 300;
    public static int time_outtake_down = 350;
    public static int time_for_claw = 300;
    public static int time_outtake_up = 350;
    public static int time_for_latch = 450;
    
    public static double nr_of_cycle = 0;
    public final AtomicBoolean runThread = new AtomicBoolean(true);

    @Override
    public void run() {

//        while (!isInterrupted()) {
//            update();
//        }

        while(runThread.get())
        {
            try{
                update();
            } catch (Exception e)
            {
                Thread.currentThread().interrupt();
            }
        }
        Thread.currentThread().interrupt();
    }

    public void takeNextPixel() {
        intake.takePixelAuto(lastPixel);
        lastPixel = intake.intake4Bar.goDown1pixel(lastPixel);
    }

    public void takePixelC1() throws InterruptedException {
        intake.takePixelAuto(Intake4Bar.POSE.pixel5);
        Thread.sleep(300);
        intake.takePixelAuto(Intake4Bar.POSE.pixel4);
    }

    public void takePixelC2() throws InterruptedException {
        intake.takePixelAuto(Intake4Bar.POSE.pixel3);
        Thread.sleep(300);
        intake.takePixelAuto(Intake4Bar.POSE.pixel2);
    }

    public void takePixelFromStack(int pixel_nr){
        intake.takePixel(pixel_nr);
    }


    public void update() {
        
        lift.update();
        extendo.update();
        intake.updateAuto();
        transferUpdate();
    }

    public void startTransfer() {
        currentState = WAITING_FOR_LATCH;
        lift.goDown();
//        extendo.goToOuttakeEscape();
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
//                    currentState = TransferState.SLIDES_RETRACTING;
//                    outtake.goToMoving();
//                    outtake.claw.goToIntake();
//                    transferTimer.reset();
//                    extendo.goDown();
//                }
//                break;
//
//            case SLIDES_RETRACTING:
//                extendo.goDown();
//                if (transferTimer.milliseconds() > time_to_ready_outtake) {
//                    currentState = TransferState.OUTTAKE_READY;
//                }
//                break;
//
//            case OUTTAKE_READY:
//
//                outtake.goToIntake();
//                currentState = TransferState.WAITING_FOR_OUTTAKE_DOWN;
//                transferTimer.reset();
//                break;
//
//            case WAITING_FOR_OUTTAKE_DOWN:
//
//                if(transferTimer.milliseconds() > time_outtake_down) {
//                    currentState = TransferState.OUTTAKE_IN_PLACE;
//                }
//                break;
//
//            case OUTTAKE_IN_PLACE:
//
//                outtake.claw.goToPlace();
//                currentState = TransferState.WAITING_FOR_CLAW;
//                transferTimer.reset();
//                break;
//
//            case WAITING_FOR_CLAW:
//
//                if(transferTimer.milliseconds() > time_for_claw) {
//                    currentState = TransferState.WAITING_FOR_OUTTAKE_UP;
//                    outtake.goToMoving();
//                    extendo.goToOuttakeEscape();
//                    transferTimer.reset();
//                }
//                break;
//
//            case WAITING_FOR_OUTTAKE_UP:
//
//                if(transferTimer.milliseconds() > time_outtake_up) {
//                    if(intake.pololuSensor.detect() == 0) {
//                        currentState = TransferState.NO_TRANSFER;
//                        intake.closeLatch();
//                        extendo.goToDrive();
//                    } else {
//                        //NU A REUSIT SA IA PIXELII
//                        currentState = TransferState.NO_TRANSFER;
//                    }
//
//                }
//                break;
//
//        }
//        previousState = currentState;
            case SLIDES_RETRACTING:
                extendo.goDown();
                intake.holdLatch();
                if (extendo.currentState == ExtendoControllerPID.States.RETRACTED &&
                        lift.currentState == LiftController.States.RETRACTED) {
                    currentState = WAITING_FOR_LATCH;
                    transferTimer.reset();
//                    intake.openLatch();
                }
                break;

            case WAITING_FOR_LATCH:
                if(extendo.position < 0){
                    intake.intake4Bar.goTo(Intake4Bar.POSE.pixel1);
                    intake.openLatch();
                }
                if(transferTimer.milliseconds() > time_for_latch) {
                    currentState = OUTTAKE_READY;
                }
                break;

            case OUTTAKE_READY:

                intake.turnOff();
                outtake.goToIntake();
                currentState = WAITING_FOR_OUTTAKE_DOWN;
                transferTimer.reset();
                break;

            case WAITING_FOR_OUTTAKE_DOWN:

                if(transferTimer.milliseconds() > time_outtake_down) {
                    currentState = OUTTAKE_IN_PLACE;
                }
                break;

            case OUTTAKE_IN_PLACE:

                outtake.claw.goToPlace();
                currentState = WAITING_FOR_CLAW;
                transferTimer.reset();
                break;

            case WAITING_FOR_CLAW:

                if(transferTimer.milliseconds() > time_for_claw) {
                    currentState = WAITING_FOR_OUTTAKE_UP;
                    extendo.goToOuttakeEscape();
                    outtake.goToMoving();
                    transferTimer.reset();
                }
                break;

            case WAITING_FOR_OUTTAKE_UP:

                if(transferTimer.milliseconds() > time_outtake_up) {
                    if(intake.pololuSensor.detect() == 0) {
                        currentState = NO_TRANSFER;
                        intake.closeLatch();
                        extendo.goToDrive();
                    } else {
                        //NU A REUSIT SA IA PIXELII
                        currentState = NO_TRANSFER;
                    }

                }
                break;

        }
        previousState = currentState;
    }
}
