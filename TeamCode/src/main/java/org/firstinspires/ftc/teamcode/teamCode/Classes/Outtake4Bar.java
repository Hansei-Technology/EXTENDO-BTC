package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Outtake4Bar {
    Servo left, right;

    public static double IntakePos = 0.88;
    public static double DropPos = 0.25;
    public static double ReadyPos = 0.78;
    public static double PreloadsPos = 0.3;  //4bar pe podea pt spike mark in auto
    public static double Cristina = 0.72;

    public Outtake4Bar(HardwareMap map)
    {
        left = map.get(Servo.class, "s5e");
        right = map.get(Servo.class, "s4e");
    }

    enum Status {
        INTAKE,
        DROP,
        AUTO_PLACE
    }

    public void setPosition(double pos) {
        left.setPosition(pos);
        right.setPosition(pos);
    }

    public void goToIntake() {
        setPosition(IntakePos);
    }

    public void goToReady() {
        setPosition(ReadyPos);
    }

    public void goToDrop() {
        setPosition(DropPos);
    }

    public void goToPreloads(){setPosition(PreloadsPos);}

    public void goingToPizdaMati() {
        setPosition(Cristina);
    }
}
