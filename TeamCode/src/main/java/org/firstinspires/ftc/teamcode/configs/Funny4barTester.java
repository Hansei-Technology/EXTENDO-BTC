package org.firstinspires.ftc.teamcode.configs;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.Photon;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teamCode.Classes.FunnyOuttake4bar;

@TeleOp(name="Funny4barTester", group = "Teste")
@Config
@Disabled
@Photon
public class Funny4barTester extends LinearOpMode {

    public FunnyOuttake4bar funny4bar;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        funny4bar = new FunnyOuttake4bar(hardwareMap);
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            if(gamepad1.a)
                funny4bar.CS = FunnyOuttake4bar.Status.READY;
           if(gamepad1.b)
                funny4bar.CS = FunnyOuttake4bar.Status.DROP;
            if(gamepad1.y)
                funny4bar.CS = FunnyOuttake4bar.Status.INTAKE;
            funny4bar.update();

//            telemetry.addData("ServoPos", funny4bar);
//            telemetry.addData()
//            telemetry.update();
        }

    }
}
