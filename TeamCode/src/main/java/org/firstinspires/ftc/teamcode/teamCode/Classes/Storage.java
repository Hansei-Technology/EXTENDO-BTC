package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Storage {
    Servo servo;
    public static double openPos = 0.22;
    public static double closedPos = 0.6;

    public static double holdPos = 0.65;
    public Storage(HardwareMap map) {
        servo = map.get(Servo.class, "s0");
    }

    public enum State {
        EMPTY,
        PIXEL,
        FULL
    }

    public boolean isOpen;
    public State currentState = State.EMPTY;

    public void open() {
        isOpen = true;
        servo.setPosition(openPos);
    }

    public void close() {
        isOpen = false;
        servo.setPosition(closedPos);
    }
    public void hold() {
        isOpen = false;
        servo.setPosition(holdPos);
    }

    public void toogleLatch(){
        if(isOpen)
            close();
        else open();
    }
}
