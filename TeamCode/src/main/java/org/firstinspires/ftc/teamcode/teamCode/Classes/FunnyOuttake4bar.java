package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utils.CoolServo;

@Config
public class FunnyOuttake4bar {
    Servo left, right;

    CoolServo funny_left, funny_right;

    public static double IntakePos = 0.88;
    public static double DropPos = 0.25;
    public static double ReadyPos = 0.78;
    public static double PreloadsPos = 0.3;  //4bar pe podea pt spike mark in auto
    public static double Cristina = 0.72;

    public static double profileMaxVelocity = 30, profileAcceleration = 30, profileDecceleration = 10;


    public enum Status {
        INTAKE,
        DROP,
        READY,
        AUTO_PLACE
    }

    public Status CS = Status.READY, PS=Status.READY;


    public FunnyOuttake4bar(HardwareMap map)
    {
        left = map.get(Servo.class, "s5e");
        right = map.get(Servo.class, "s4e");

        funny_left = new CoolServo(left, false, profileMaxVelocity, profileAcceleration, profileDecceleration, ReadyPos);
        funny_right = new CoolServo(left, false, profileMaxVelocity, profileAcceleration, profileDecceleration, ReadyPos);

        funny_left.forceUpdate();
        funny_right.forceUpdate();
    }

    public void setPos(double pos) {
        funny_left.setPosition(pos);
        funny_right.setPosition(pos);
    }

    public void update() {
            switch (CS) {
                case READY:
                    setPos(ReadyPos);
                case DROP:
                    setPos(DropPos);
                case INTAKE:
                    setPos(IntakePos);
            }
        funny_right.forceUpdate();
        funny_left.forceUpdate();
    }


}
