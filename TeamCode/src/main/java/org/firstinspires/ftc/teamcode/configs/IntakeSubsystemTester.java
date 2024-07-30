package org.firstinspires.ftc.teamcode.configs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.teamCode.Classes.Intake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeSubsystem;

public class IntakeSubsystemTester extends LinearOpMode {
    IntakeSubsystem intake;
    @Override
    public void runOpMode() throws InterruptedException {
        intake = new IntakeSubsystem(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_down) intake.takePixel(Intake4Bar.POSE.pixel2);
            if (gamepad1.dpad_right) intake.takePixel(Intake4Bar.POSE.pixel3);
            if (gamepad1.dpad_up) intake.takePixel(Intake4Bar.POSE.pixel4);
            if (gamepad1.dpad_left) intake.takePixel(Intake4Bar.POSE.pixel5);
            if (gamepad1.a) intake.takePixel(Intake4Bar.POSE.pixel1);

            if(gamepad1.y) intake.stop();

            intake.update();
        }
    }
}
