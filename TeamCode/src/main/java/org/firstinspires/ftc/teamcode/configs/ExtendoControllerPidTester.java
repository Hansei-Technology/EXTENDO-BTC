package org.firstinspires.ftc.teamcode.configs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;

@TeleOp
public class ExtendoControllerPidTester extends LinearOpMode {
    ExtendoControllerPID extendo;
    @Override
    public void runOpMode() throws InterruptedException {
        extendo = new ExtendoControllerPID(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            if(gamepad1.a) extendo.goDown();
            if(gamepad1.b) extendo.goToMid();
            if(gamepad1.y) extendo.goToPoz(1200);

            extendo.update();
        }

    }
}
