package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Storage {
    Servo servo;
    public static double openPos = 0.2;
    public static double closedPos = 0.6;
    public Storage(HardwareMap map) {
        servo = map.get(Servo.class, "s0");
    }

    public enum State {
        EMPTY,
        PIXEL,
        FULL
    }
    public State currentState = State.EMPTY;

    public void open() {
        servo.setPosition(openPos);
    }

    public void close() {
        servo.setPosition(closedPos);
    }
}
