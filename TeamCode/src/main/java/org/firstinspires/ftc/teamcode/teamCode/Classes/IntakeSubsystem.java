package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class IntakeSubsystem {
    Intake4Bar intake4Bar;
    IntakeController intakeController;
    Storage storage;
    ElapsedTime timer;
    PololuSensor pololuSensor;

    public IntakeSubsystem(HardwareMap map) {
        intakeController = new IntakeController(map);
        intake4Bar = new Intake4Bar(map);
        storage = new Storage(map);
        pololuSensor = new PololuSensor(map);
        timer = new ElapsedTime();

        intake4Bar.goTo(Intake4Bar.POSE.pixel1);
        closeLatch();
    }

    public enum State {
        ON,
        OFF,
        GOT_PIXELS_WAITING,
        REVERSE,
        FULL
    }
    public State currentState = State.OFF, previousState = State.OFF;
    public static int time_to_settle = 100;
    public static int time_to_reverse = 300;

    public void takePixel(Intake4Bar.POSE poz) {
        intake4Bar.goTo(poz);
        intakeController.turnOn();
        currentState = State.ON;
    }

    public void stop() {
        intakeController.turnOff();
        currentState = State.OFF;
    }

    public void openLatch() {
        storage.open();
    }

    public void closeLatch() {
        storage.close();
    }


    public void update() {
        switch (currentState) {
            case ON:
                if(pololuSensor.detect() == 2) {
                    currentState = State.GOT_PIXELS_WAITING;
                }
                break;
            case GOT_PIXELS_WAITING:
                if (currentState != previousState) {
                    timer.reset();
                }
                if(timer.milliseconds() > time_to_settle) {
                    currentState = State.REVERSE;
                    intakeController.reverse();
                }
                break;
            case REVERSE:
                if (currentState != previousState) {
                    timer.reset();
                }
                if(timer.milliseconds() > time_to_reverse) {
                    currentState = State.FULL;
                    intakeController.reverse();
                }
        }
        previousState = currentState;
    }

}
