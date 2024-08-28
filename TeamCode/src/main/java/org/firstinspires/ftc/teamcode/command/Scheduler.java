package org.firstinspires.ftc.teamcode.command;

import lombok.Getter;
import lombok.Setter;

import java.util.*;


@Getter
@Setter
public class Scheduler {
    private static Scheduler scheduler;
    private final Set<Subsystem> subsystems;
    private final LinkedHashSet<Command> commandsToSchedule;
    private final LinkedHashSet<Command> commandsToRun;
    private final Set<String> currentlyRequiredSubsystems = new HashSet<>();
    private final Set<Command> commandsToCancel;
    private RunState currentRunState;

    private Scheduler() {
        subsystems = new HashSet<>();
        commandsToSchedule = new LinkedHashSet<>();
        commandsToCancel = new HashSet<>();
        commandsToRun = new LinkedHashSet<>();
        currentRunState = RunState.INIT;
    }

    public static Scheduler getInstance() {
        if (scheduler == null) {
            scheduler = new Scheduler();
        }
        return scheduler;
    }

    public static Scheduler freshInstance(){
        scheduler = new Scheduler();
        return scheduler;
    }

    public void registerSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }

    public void updatePeriodic(){
        for (Subsystem subsystem : subsystems) {
            subsystem.update();
        }
    }

    public boolean isScheduled(Command command){
        return commandsToSchedule.contains(command);
    }

    public void scheduleCommand(Command command){
        commandsToSchedule.add(command);
    }
    public void cancelCommand(Command command){
        commandsToCancel.add(command);
    }

    public void cancelCommand(Command command, boolean interrupted) {
        if(command==null) return;
        if(!isScheduled(command)) return;
        command.end(interrupted);
        commandsToRun.remove(command);
    }

    public void initializeCommand(Command command){
        commandsToRun.add(command);
        currentlyRequiredSubsystems.addAll(command.getRequiredSubsystemsKeys());
        command.init();
    }

    public void runCommands() {
        for(Command command : commandsToRun) {
            if (command.isFinished()) {
                command.end(false);
                commandsToRun.remove(command);
            } else if (!command.getRunstateList().contains(currentRunState)) {
                command.end(true);
                commandsToRun.remove(command);
            }
        }
        for(Command command : commandsToSchedule) {
            initializeCommand(command);
        }
        commandsToSchedule.clear();
        for(Subsystem subsystem: subsystems) {
            if(!currentlyRequiredSubsystems.contains(subsystem.getSubsystemKey())) {
                scheduleCommand(subsystem.getDefaultCommand());
            }
        }
        for(Command command : commandsToCancel)
            cancelCommand(command, true);
        commandsToCancel.clear();
        for(Command command: commandsToRun){
            command.execute();
        }
    }


}
