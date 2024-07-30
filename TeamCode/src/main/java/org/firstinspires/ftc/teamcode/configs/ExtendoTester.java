package org.firstinspires.ftc.teamcode.configs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;

public class ExtendoTester extends LinearOpMode {
    ExtendoControllerPID extendo;
    @Override
    public void runOpMode() throws InterruptedException {
        extendo = new ExtendoControllerPID(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            extendo.setPower(gamepad1.right_trigger - gamepad1.left_trigger);
            if (gamepad1.a) extendo.goDown();
            if (gamepad1.y) extendo.goDownTillMotorOverCurrent();
            extendo.update();
        }
    }
}
