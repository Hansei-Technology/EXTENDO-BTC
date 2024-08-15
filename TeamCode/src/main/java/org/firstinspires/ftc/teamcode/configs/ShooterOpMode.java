package org.firstinspires.ftc.teamcode.configs;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@Disabled
@TeleOp
public class ShooterOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Shooter shooter = new Shooter(hardwareMap);

        waitForStart();

        Actions.runBlocking(shooter.spinUp());
    }
}
