package org.firstinspires.ftc.teamcode.configs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teamCode.Classes.LiftController;

@TeleOp(group = "Teste")
@Config
public class LiftTester extends LinearOpMode {
    public static int poz = 0;
    LiftController lift;
    @Override
    public void runOpMode() throws InterruptedException {

        lift = new LiftController(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        waitForStart();

        while(opModeIsActive()) {
            lift.goToPoz(poz);
            lift.update();
            telemetry.addData("poz:", lift.position);
            telemetry.update();
        }


    }
}
