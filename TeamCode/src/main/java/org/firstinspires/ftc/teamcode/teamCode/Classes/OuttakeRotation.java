package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class OuttakeRotation {
    public Servo servo;
    public static double Servo_Level = 0.585;
    public static double Servo_Left = 0.75;
    public static double Left_Vertical = 0.85;
    public static double Servo_Right = 0.4;
    public static double Right_Vertical = 0.3;

    public static double Servo_left_90_deg = 0.87;

    public OuttakeRotation(HardwareMap map)
    {
        servo = map.get(Servo.class, "s1e");
    }

    enum Status {
        LEVEL,
        LEFT,
        RIGHT
    }

    public void goToLevel()
    {
        servo.setPosition(Servo_Level);
    }
    public void goLeft()
    {
        servo.setPosition(Servo_Left);
    }
    public void goLeftVertical()
    {
        servo.setPosition(Left_Vertical);
    }

    public void go90Deg()
    {
        servo.setPosition(Servo_left_90_deg);
    }

    public void goRight()
    {
        servo.setPosition(Servo_Right);
    }
    public void goRightVertical()
    {
        servo.setPosition(Right_Vertical);
    }
}
