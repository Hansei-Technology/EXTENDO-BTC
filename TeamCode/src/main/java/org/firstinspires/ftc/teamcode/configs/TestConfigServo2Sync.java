package org.firstinspires.ftc.teamcode.configs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(group = "Teste")
@Config
public class TestConfigServo2Sync extends LinearOpMode {
    Servo servo1, servo2;
    public static double poz = 0;
    public static String name1 = "";
    public static String name2 = "";
    @Override
    public void runOpMode() throws InterruptedException {

        servo1 = hardwareMap.get(Servo.class, name1);
        servo2 = hardwareMap.get(Servo.class, name2);
        waitForStart();
        while(opModeIsActive())
        {
            servo1.setPosition(poz);
            servo2.setPosition(poz);
        }

    }
}
