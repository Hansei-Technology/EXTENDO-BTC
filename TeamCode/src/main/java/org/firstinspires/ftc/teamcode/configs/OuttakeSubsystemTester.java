package org.firstinspires.ftc.teamcode.configs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teamCode.Classes.OuttakeSubsystem;

@TeleOp(group = "Teste")
public class OuttakeSubsystemTester extends LinearOpMode {
    OuttakeSubsystem outtake;
    @Override
    public void runOpMode() throws InterruptedException {
        outtake = new OuttakeSubsystem(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            if(gamepad1.y) outtake.goToPlace();
            if(gamepad1.b) outtake.goToReady();
            if(gamepad1.a) outtake.goToIntake();

            if(gamepad1.left_bumper) outtake.claw.goToPlace();
            if(gamepad1.right_bumper) outtake.claw.goToIntake();

            if(gamepad1.dpad_up) outtake.rotation.goToLevel();
            if(gamepad1.dpad_left) outtake.rotation.goLeft();
            if(gamepad1.dpad_right) outtake.rotation.goRight();
        }
    }
}
