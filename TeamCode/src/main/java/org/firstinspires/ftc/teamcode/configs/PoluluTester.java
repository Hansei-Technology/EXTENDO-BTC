package org.firstinspires.ftc.teamcode.configs;

import android.graphics.LinearGradient;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teamCode.Classes.PololuSensor;

@TeleOp(group = "Teste")
public class PoluluTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        PololuSensor senzor = new PololuSensor(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("pixeli", senzor.detect());
            telemetry.update();
        }

    }
}
