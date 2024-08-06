package org.firstinspires.ftc.teamcode.configs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;

@TeleOp
public class LiftControllerTester extends LinearOpMode {
    LiftController lift;
    @Override
    public void runOpMode() throws InterruptedException {
        lift = new LiftController(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) lift.goDown();
            if (gamepad1.b) lift.goToLow();
            if (gamepad1.y) lift.goToMid();
            if(gamepad1.dpad_down) lift.goDownTillMotorOverCurrent();

            lift.setPower(gamepad1.right_trigger - gamepad1.left_trigger);
            lift.update();
            telemetry.addData("lift", lift.currentState);
            telemetry.addData("poz", lift.position);
            telemetry.addData("pid", lift.pidON);
            telemetry.update();
        }
    }
}
