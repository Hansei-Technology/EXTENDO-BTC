package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class IntakeSubsystem {
    public Intake4Bar intake4Bar;
    public IntakeController intakeController;
    public Storage storage;
    public ElapsedTime timer;
    public PololuSensor pololuSensor;

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
        FULL,
        AUTO_ON,
        AUTO_OFF,
        AUTO_GOT_PIXELS_WAITING,
        AUTO_REVERSE,
        AUTO_FULL

    }
    public State currentState = State.OFF, previousState = State.OFF;
    public static int time_to_settle = 1000;
    public static int time_to_reverse = 300;

    public boolean isFull() {
        return currentState == State.FULL;
    }

    public void takePixel(Intake4Bar.POSE poz) {
        intake4Bar.goTo(poz);
        intakeController.turnOn();
        currentState = State.ON;
    }

    public void takePixel(int pixelNumber) {
        switch (pixelNumber) {
            case 5:
                intake4Bar.goTo(Intake4Bar.POSE.pixel5);
                break;
            case 4:
                intake4Bar.goTo(Intake4Bar.POSE.pixel4);
                break;
            case 3:
                intake4Bar.goTo(Intake4Bar.POSE.pixel3);
                break;
            case 2:
                intake4Bar.goTo(Intake4Bar.POSE.pixel2);
                break;
            case 1:
                intake4Bar.goTo(Intake4Bar.POSE.pixel1);
                break;
        }
        intakeController.turnOn();
        currentState = State.ON;
    }


    public void takePixelAuto(Intake4Bar.POSE poz) {
        intake4Bar.goTo(poz);
        intakeController.turnOn();
        currentState = State.AUTO_ON;
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
    public void holdLatch() {
        storage.hold();
    }


    public void update() {
        switch (currentState) {
            case ON:
                closeLatch();
                if(pololuSensor.detect() == 2) {
                    currentState = State.GOT_PIXELS_WAITING;
                    timer.reset();
                }
                break;
            case GOT_PIXELS_WAITING:
                if(timer.milliseconds() > time_to_settle) {
                    currentState = State.REVERSE;
                    intakeController.reverse();
                    //openLatch();
                    timer.reset();
                }
                break;
            case REVERSE:
                if(timer.milliseconds() > time_to_reverse) {
                    currentState = State.FULL;
                    intakeController.turnOff();
                }
                break;

            case FULL:
                if (pololuSensor.detect() != 2) currentState = State.OFF;
                break;
        }
        previousState = currentState;
    }


    public void updateAuto() {
        switch (currentState) {
            case AUTO_ON:
                closeLatch();
                if(pololuSensor.detect() == 2) {
                    currentState = State.GOT_PIXELS_WAITING;
                    timer.reset();
                }
                break;
            case AUTO_GOT_PIXELS_WAITING:
                if(timer.milliseconds() > time_to_settle) {
                    currentState = State.FULL;
                    intakeController.reversePreload();
                    //openLatch();
                    timer.reset();
                }
                break;

            case AUTO_FULL:
                if (pololuSensor.detect() != 2) currentState = State.OFF;
                break;
        }
        previousState = currentState;
    }
    public void turnOff() {
        currentState = State.AUTO_FULL;
        openLatch();
        intakeController.turnOff();
    }
}
