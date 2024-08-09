package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Intake4Bar {
    Servo servo;

    public static double poz1 = 0.39;
    public static double poz2 = 0.265;
    public static double poz3 = 0.23;
    public static double poz4 = 0.215;
    public static double poz5 = 0.2;
    public static double movingPos = 0.1;


    public Intake4Bar(HardwareMap map) {
        servo = map.get(Servo.class, "s1");
    }

    public enum POSE {
        pixel1,
        pixel2,
        pixel3,
        pixel4,
        pixel5,
        pixel0,
        moving
    }

    public POSE goDown1pixel(POSE pose) {
        switch (pose) {
            case pixel5:
                return POSE.pixel4;
            case pixel4:
                return POSE.pixel3;
            case pixel3:
                return POSE.pixel2;
            case pixel2:
                return POSE.pixel1;
            case pixel1:
                return POSE.pixel0;
            case pixel0:
                return POSE.pixel5;
        }
        return POSE.pixel5;
    }


    public void goTo(POSE poz) {
        switch (poz) {
            case pixel1: {
                servo.setPosition(poz1);
                break;
            }
            case pixel2: {
                servo.setPosition(poz2);
                break;
            }
            case pixel3: {
                servo.setPosition(poz3);
                break;
            }
            case pixel4: {
                servo.setPosition(poz4);
                break;
            }
            case pixel5: {
                servo.setPosition(poz5);
                break;
            }
            case moving: {
                servo.setPosition(movingPos);
                break;
            }

        }
    }
}
