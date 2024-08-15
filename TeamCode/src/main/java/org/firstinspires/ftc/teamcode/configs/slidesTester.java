package org.firstinspires.ftc.teamcode.configs;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.Photon;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;

@TeleOp(name = "Test glisiere")
@Config
@Photon
@Disabled
public class slidesTester extends LinearOpMode {
    public LiftController liftController;
    public float raw_input;
    public int ref = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        liftController = new LiftController(hardwareMap);
        liftController.pidON = false;
        waitForStart();
        while(opModeIsActive() && !isStopRequested())
        {
            raw_input = gamepad1.right_trigger - gamepad1.left_trigger;
            if(raw_input > 0.5)
                ref+=5;
            else if(raw_input < -0.5)
                ref -=5;

//            if(liftController.position < 10) {
//                ref=0;
//            }
//
//            if(liftController.position < ref+48 || liftController.position < ref-48)
//                liftController.setRawPower(0.4);
//            else if(liftController.position > ref + 48 || liftController.position > ref -48) {
//                liftController.setRawPower(-0.4);
//            }

//            liftController.runToPos(ref);


            liftController.update();
            telemetry.addData("ref", ref);
            telemetry.addData("raw", raw_input);
            telemetry.addData("pos", liftController.position);
            telemetry.addData("error", Math.abs(ref-liftController.position));
            telemetry.update();
        }
    }
}
