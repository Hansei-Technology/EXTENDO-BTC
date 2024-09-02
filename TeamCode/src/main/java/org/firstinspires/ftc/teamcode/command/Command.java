package org.firstinspires.ftc.teamcode.command;

import java.util.Set;

public interface Command {

    void init();
    void execute();
    void end(boolean interrupted);
    boolean isFinished();
    Set<RunState> getRunstateList();
    boolean isIsInterruptible();

    Set<String> getRequiredSubsystemsKeys();
}
