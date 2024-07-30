package org.firstinspires.ftc.teamcode.configs;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Intake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Storage;

public class IntakeTesterExtendoChassis extends LinearOpMode {
    Intake4Bar intake4Bar;
    IntakeController intakeController;
    Storage storage;
    ExtendoControllerPID extendo;
    MecanumDrive drive;

    @Override
    public void runOpMode() throws InterruptedException {
        intakeController = new IntakeController(hardwareMap);
        intake4Bar = new Intake4Bar(hardwareMap);
        storage = new Storage(hardwareMap);
        extendo = new ExtendoControllerPID(hardwareMap);
        drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.dpad_down) intake4Bar.goTo(Intake4Bar.POSE.pixel2);
            if (gamepad1.dpad_right) intake4Bar.goTo(Intake4Bar.POSE.pixel3);
            if (gamepad1.dpad_up) intake4Bar.goTo(Intake4Bar.POSE.pixel4);
            if (gamepad1.dpad_left) intake4Bar.goTo(Intake4Bar.POSE.pixel5);
            if (gamepad1.a) intake4Bar.goTo(Intake4Bar.POSE.pixel1);

            if (gamepad1.left_bumper) intakeController.turnOff();
            if (gamepad1.right_bumper) intakeController.turnOn();
            if (gamepad1.y) intakeController.reverse();

            if (gamepad1.b) storage.close();
            if (gamepad1.x) storage.open();

            extendo.setPower(gamepad1.right_trigger - gamepad1.left_trigger);
            drive.robotCentric(gamepad1);
        }

    }
}
