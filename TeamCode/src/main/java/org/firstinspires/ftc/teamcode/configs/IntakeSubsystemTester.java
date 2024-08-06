package org.firstinspires.ftc.teamcode.configs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teamCode.Classes.Intake4Bar;
import org.firstinspires.ftc.teamcode.teamCode.Classes.IntakeSubsystem;

@TeleOp(group = "Teste")
public class IntakeSubsystemTester extends LinearOpMode {
    IntakeSubsystem intake;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intake = new IntakeSubsystem(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_down) intake.takePixel(Intake4Bar.POSE.pixel2);
            if (gamepad1.dpad_right) intake.takePixel(Intake4Bar.POSE.pixel3);
            if (gamepad1.dpad_up) intake.takePixel(Intake4Bar.POSE.pixel4);
            if (gamepad1.dpad_left) intake.takePixel(Intake4Bar.POSE.pixel5);
            if (gamepad1.a) intake.takePixel(Intake4Bar.POSE.pixel1);

            if(gamepad1.y) intake.stop();

            if(gamepad1.right_bumper) intake.openLatch();
            if(gamepad1.left_bumper) intake.closeLatch();

            telemetry.addData("timer", intake.timer.milliseconds());
            telemetry.addData("state", intake.currentState);
            telemetry.update();
            intake.update();
        }
    }
}
