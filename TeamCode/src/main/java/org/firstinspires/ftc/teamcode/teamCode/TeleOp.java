package org.firstinspires.ftc.teamcode.teamCode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Utils.StickyGamepad;
import org.firstinspires.ftc.teamcode.teamCode.Classes.DroneController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Intake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeSubsystem;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
@Config
public class TeleOp extends LinearOpMode {
    LiftController lift;
    IntakeSubsystem intake;
    OuttakeSubsystem outtake;
    ExtendoControllerPID extendo;
    DroneController drone;

    MecanumDrive drive;
    StickyGamepad sg1, sg2;
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

    public static int time_for_latch = 1000;
    public static int time_outtake_down = 900;
    public static int time_for_claw = 250;
    public static int time_outtake_up = 350;

    public static int levelIncrement = 100;
    public boolean isArragingPixels = false;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        lift = new LiftController(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        extendo = new ExtendoControllerPID(hardwareMap);
        drone = new DroneController(hardwareMap);
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        sg1 = new StickyGamepad(gamepad1, this);
        sg2 = new StickyGamepad(gamepad2, this);
        transferTimer = new ElapsedTime();

        while(opModeInInit()){
            outtake.goToMoving();
        lift.goToPoz(-50);
        lift.ResetEncoders();
        //extendo.goDown();
        //lift.goDown();
        intake.closeLatch();
        intake.intake4Bar.goTo(Intake4Bar.POSE.moving);
        }
        

        waitForStart();

        while (opModeIsActive()) {

            //controller 1
            if (sg1.left_bumper) outtake.claw.toggleRight();
            if (sg1.right_bumper) outtake.claw.toggleLeft();

            if (gamepad1.x) {
                isArragingPixels = false;
                lift.goDown();
                outtake.goToMoving();
            }
            if (gamepad1.b)  {
                lift.goToMid();
                outtake.goToPlace();
            }

            if(gamepad1.y) {
                lift.goToHigh();
                outtake.goToPlace();
            }

            if(gamepad1.a) {
                outtake.goToFirstLines();
            }

            if(gamepad1.dpad_up) {
                lift.goToPoz(lift.position + levelIncrement);
            }

            if(gamepad1.dpad_down) {
                lift.goToPoz(lift.position - levelIncrement);
            }


            if (gamepad1.right_stick_y > 0.9) {
                outtake.rotation.goRight();
                gamepad1.right_stick_x = 0;
            }
            else if (gamepad1.right_stick_y < -0.9) {
                outtake.rotation.goLeft();
                gamepad1.right_stick_x = 0;
            }
            else if (!isArragingPixels) outtake.rotation.goToLevel();

            if(gamepad1.right_trigger - gamepad1.left_trigger >0.2 || gamepad1.right_trigger - gamepad1.left_trigger < -0.2)
                lift.setPower(gamepad1.right_trigger - gamepad1.left_trigger);
            else lift.setPower(0);
            if(gamepad1.dpad_right){
                isArragingPixels = true;
                outtake.goToArrange(lift.position);
            }

            if(gamepad1.dpad_left) {
                intake.intake4Bar.goTo(Intake4Bar.POSE.moving);
                extendo.goDown();
                lift.goDown();
            }

            //controller 2
            if (gamepad2.dpad_down) intake.takePixel(Intake4Bar.POSE.pixel1);
            extendo.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
            if (gamepad2.a) extendo.goToDrive();
            if (gamepad2.b) extendo.goToMid();
            if(gamepad2.y) extendo.goToMaxPosTeleop();
            if(gamepad2.x) drone.CS = DroneController.droneStatus.RELEASED;

            if(gamepad2.dpad_up) {
                currentState = TransferState.SLIDES_RETRACTING;
                lift.goDown();
                extendo.goDown();
                outtake.claw.goToIntake();
                outtake.goToMoving();
                transferTimer.reset();
                //intake.openLatch(); //ro2 au probleme cu asta(exista un state special)
                intake.turnOff();
            }

            if (gamepad2.dpad_right) intake.currentState = IntakeSubsystem.State.GOT_PIXELS_WAITING;

            if (gamepad2.dpad_left) intake.intakeController.turnOff();


            //pozitii stack
            if (Math.abs(gamepad2.left_stick_y) > Math.abs(gamepad2.left_stick_x))
            {
                if (gamepad2.left_stick_y < 0) //5 conuri in stack
                {
                    intake.takePixel(Intake4Bar.POSE.pixel5);
                }
                else //3 conuri in stack
                {
                    intake.takePixel(Intake4Bar.POSE.pixel3);
                }
            }
            else if (Math.abs(gamepad2.left_stick_x) > Math.abs(gamepad2.left_stick_y))
            {
                if (gamepad2.left_stick_x > 0) //4 conuri in stack
                {
                    intake.takePixel(Intake4Bar.POSE.pixel4);
                }
                else //2 conuri in stack
                {
                    intake.takePixel(Intake4Bar.POSE.pixel2);
                }
            }

            if(sg2.right_bumper) {
                sleep(50);
                intake.toogleLatch();
            }


            transferUpdate();
            drive.robotCentric(gamepad1);
            lift.update();
            intake.update(gamepad1,gamepad2);
            extendo.update();
            drone.update();
            sg1.update();
            sg2.update();

            telemetry.addData("extendo", extendo.currentState);
            telemetry.addData("lift", lift.currentState);
            telemetry.addData("transfer", currentState);
            telemetry.addData("intke", intake.currentState);
            telemetry.update();
        }
    }

    void transferUpdate() {
        switch (currentState) {
            case NO_TRANSFER:
                transferTimer.reset();
//                if(intake.isFull()) {
//                    currentState = TransferState.SLIDES_RETRACTING;
////                    intake.intakeController.turnOn();
//                    lift.goDown();
//                    extendo.goDown();
//                    outtake.claw.goToIntake();
//                    outtake.goToMoving();
//                    transferTimer.reset();
//                }
                break;

//            case NO_EXTENDO:
//
//                if(transferTimer.milliseconds() > time_for_latch) {
//                    currentState = TransferState.OUTTAKE_READY;
//                }
//                break;

            case SLIDES_RETRACTING:
                intake.holdLatch();
                if (extendo.currentState == ExtendoControllerPID.States.RETRACTED &&
                        lift.currentState == LiftController.States.RETRACTED) {
                    currentState = TransferState.WAITING_FOR_LATCH;
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
                    currentState = TransferState.OUTTAKE_READY;
                }
                break;

            case OUTTAKE_READY:

                intake.turnOff();
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
                    extendo.goToOuttakeEscape();
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
