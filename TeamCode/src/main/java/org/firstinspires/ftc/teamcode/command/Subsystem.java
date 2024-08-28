package org.firstinspires.ftc.teamcode.command;

public interface Subsystem {
    void init();
    void update();
    void stop();
    void updatePeriodic();
    String getSubsystemKey();

    Command getDefaultCommand();
}
