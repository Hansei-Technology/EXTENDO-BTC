package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class OuttakeJoint {
    Servo servo;

    public static double IntakePos = 0.94;
    public static double ReadyPos = 0.94;
    public static double DropPos = 0.5;
    public static double SpikePlace = 0.35;  //4bar pe podea pt spike mark in auto

    public OuttakeJoint(HardwareMap map)
    {
        servo = map.get(Servo.class, "s3e");
    }

    enum Status {
        INTAKE,
        DROP,
        READY
    }

    public void goToIntake()
    {
        servo.setPosition(IntakePos);
    }
    public void goToDrop()
    {
        servo.setPosition(DropPos);
    }
    public void goToReady()
    {
        servo.setPosition(ReadyPos);
    }
    public void goToPlaceAuto(){servo.setPosition(SpikePlace);}

}
