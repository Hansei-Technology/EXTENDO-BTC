package org.firstinspires.ftc.teamcode.configs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teamCode.AutoController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Intake4Bar;

@TeleOp
public class AutoControllerTester extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        AutoController robot = new AutoController(hardwareMap);
        robot.outtake.goToMoving();
        waitForStart();
        robot.start();

        while (opModeIsActive() && !isStopRequested()) {
            if(gamepad1.a) robot.startTransfer();
            if(gamepad1.dpad_up) robot.intake.takePixel(Intake4Bar.POSE.pixel1);

            if(gamepad1.left_bumper) robot.intake.closeLatch();
            if(gamepad1.right_bumper) robot.intake.openLatch();
        } robot.interrupt();
    }
}
