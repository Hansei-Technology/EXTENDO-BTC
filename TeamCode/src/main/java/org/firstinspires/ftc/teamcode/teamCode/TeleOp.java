package org.firstinspires.ftc.teamcode.teamCode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Utils.StickyGamepad;
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

    public static int time_for_latch = 1000;
    public static int time_outtake_down = 300;
    public static int time_for_claw = 250;
    public static int time_outtake_up = 300;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        lift = new LiftController(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        extendo = new ExtendoControllerPID(hardwareMap);
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        sg1 = new StickyGamepad(gamepad1, this);
        transferTimer = new ElapsedTime();

        outtake.goToMoving();
        //extendo.goDown();
        //lift.goDown();
        intake.closeLatch();

        waitForStart();

        while (opModeIsActive()) {

            //controller 1
            if (sg1.left_bumper) outtake.claw.toggleLeft();
            if (sg1.right_bumper) outtake.claw.toggleRight();

            if (gamepad1.a) {
                lift.goDown();
                outtake.goToMoving();
            }
            if (gamepad1.b)  {
                lift.goToLow();
                outtake.goToPlace();
            }

            if (gamepad1.dpad_right) outtake.rotation.goRight();
            else if (gamepad1.dpad_left) outtake.rotation.goLeft();
            else outtake.rotation.goToLevel();

            lift.setPower(gamepad1.right_trigger - gamepad1.left_trigger);

            if(gamepad1.dpad_up) outtake.goToArrange();

            //controller 2
            if (gamepad2.dpad_down) intake.takePixel(Intake4Bar.POSE.pixel1);
            extendo.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
            if (gamepad2.a) extendo.goToDrive();
            if (gamepad2.y) extendo.goToMid();

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


            transferUpdate();
            drive.robotCentric(gamepad1);
            lift.update();
            intake.update();
            extendo.update();
            sg1.update();

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

                if(intake.isFull()) {
                    currentState = TransferState.SLIDES_RETRACTING;
//                    intake.intakeController.turnOn();
                    lift.goDown();
                    extendo.goDown();
                    outtake.claw.goToIntake();
                    outtake.goToMoving();
                    transferTimer.reset();
                }
                break;

//            case NO_EXTENDO:
//
//                if(transferTimer.milliseconds() > time_for_latch) {
//                    currentState = TransferState.OUTTAKE_READY;
//                }
//                break;

            case SLIDES_RETRACTING:

                if (extendo.currentState == ExtendoControllerPID.States.RETRACTED &&
                        lift.currentState == LiftController.States.RETRACTED) {
                    currentState = TransferState.WAITING_FOR_LATCH;
                    transferTimer.reset();
                    intake.openLatch();
                }
                break;
                
            case WAITING_FOR_LATCH:
                
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
