package org.firstinspires.ftc.teamcode.teamCode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.Outtake4Bar;

@TeleOp
public class TestHanga extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        LiftController liftController = new LiftController(hardwareMap);
        Outtake4Bar outtake4Bar = new Outtake4Bar(hardwareMap);
        outtake4Bar.goToDrop();
        liftController.goToPoz(0);
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            if(gamepad1.a) {
                liftController.goToPoz(1500);
            }
            if(gamepad1.b){
                liftController.goToPoz(0);
            }
            liftController.update();
        }
    }
}
