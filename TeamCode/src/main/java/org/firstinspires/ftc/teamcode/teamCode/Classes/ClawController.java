package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ClawController {
    Servo ClawLeft, ClawRight;

    public static double LeftOpen = 0.1;
    public static double LeftClose = 0.65;
    public static double RightOpen = 0.8;
    public static double RightClose = 0.22;
    public static double LeftArrange= 0.65;
    public ClawController(HardwareMap map)
    {
        ClawLeft = map.get(Servo.class, "s0e");
        ClawRight = map.get(Servo.class, "s2");
    }


    enum Status {
        INTAKE,
        DROP,
        ARRANGE,
        OPEN,
        CLOSE
    }
    public Status LeftStatus = Status.OPEN, RightStatus = Status.OPEN;

    public void openLeft() {
        LeftStatus = Status.OPEN;
        ClawLeft.setPosition(LeftOpen);
    }
    public void openRight() {
        RightStatus = Status.OPEN;
        ClawRight.setPosition(RightOpen);
    }

    public void closeLeft() {
        LeftStatus = Status.CLOSE;
        ClawLeft.setPosition(LeftClose);
    }
    public void closeRight() {
        RightStatus = Status.CLOSE;
        ClawRight.setPosition(RightClose);
    }

    public void toggleLeft() {
        if(LeftStatus == Status.OPEN) closeLeft();
        else openLeft();
    }

    public void toggleRight() {
        if(RightStatus == Status.OPEN) closeRight();
        else openRight();
    }

    public void goToIntake() {
        openLeft();
        openRight();
    }

    public void goToPlace() {
        closeLeft();
        closeRight();
    }

    public void goToArrange() {
        ClawLeft.setPosition(LeftArrange);
        openRight();
    }

    public boolean isEmpty() {
        return LeftStatus == Status.OPEN && RightStatus == Status.OPEN;
    }
}
