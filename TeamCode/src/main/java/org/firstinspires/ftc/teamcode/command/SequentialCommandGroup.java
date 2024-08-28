package org.firstinspires.ftc.teamcode.command;

import java.lang.reflect.Array;
import java.util.*;

public class SequentialCommandGroup implements CommandGroup{
    private final ArrayList<Command> commands;
    private final boolean interruptable;
    private Set<String> requiredSubsystemsKeys;
    private Set<RunState> runStateList;
    private Command currentCommand;
    private int currentCommandIndex;


    public SequentialCommandGroup() {
        interruptable  = true;
        this.commands = new ArrayList<>();
        currentCommandIndex = -1;
    }

    private SequentialCommandGroup(ArrayList<Command> commands, Set<String> requiredSubsystemsKeys, Set<RunState> runStateList, boolean interruptable) {
        this.commands = commands;
        this.requiredSubsystemsKeys = requiredSubsystemsKeys;
        this.runStateList = runStateList;
        this.interruptable = interruptable;
        currentCommandIndex = -1;
    }


    @Override
    public void init() {
        if(commands.isEmpty())
            throw new IllegalStateException("No commands in command group");
        currentCommandIndex = 0;
        currentCommand = commands.get(currentCommandIndex);
        if(currentCommand.getRunstateList().contains(Scheduler.getInstance().getCurrentRunState())){
            currentCommand.init();
        }

    }

    @Override
    public final void execute() {
        if(currentCommand==null) return;
        if(currentCommand.isFinished() || !currentCommand.getRunstateList().contains(Scheduler.getInstance().getCurrentRunState())) {
            currentCommand.end(false);
            currentCommandIndex++;
            if(currentCommandIndex>=commands.size()) {
                currentCommand = null;
                return;
            }
            currentCommand = commands.get(currentCommandIndex);
            if(currentCommand.getRunstateList().contains(Scheduler.getInstance().getCurrentRunState())){
                currentCommand.init();
                currentCommand.execute();
            }
        }


    }

    @Override
    public void end(boolean interrupted) {
        currentCommandIndex=-1;
        if(currentCommand==null || !currentCommand.getRunstateList().contains(Scheduler.getInstance().getCurrentRunState()))
            return;
        currentCommand.end(interrupted);
    }

    @Override
    public boolean isFinished() {
          return currentCommandIndex>=commands.size() || currentCommand==null;
    }

    @Override
    public Set<RunState> getRunstateList() {
        return runStateList;
    }

    @Override
    public boolean isIsInterruptible() {
        return false;
    }

    @Override
    public Set<String> getRequiredSubsystemsKeys() {
        return requiredSubsystemsKeys;
    }

    public SequentialCommandGroup addCommands(Command... commands) {
        return addCommands(Arrays.asList(commands));
    }

    public SequentialCommandGroup addCommands(Collection<Command> commands) {
        if(currentCommandIndex!=-1)
            throw new IllegalStateException("Cannot add commands to command group after it has been initialized");
        ArrayList<Command> newCommandList = new ArrayList<>(commands);
        newCommandList.addAll(commands);
        Set<String> newRequiredSubsystemsKeys = new HashSet<>(this.getRequiredSubsystemsKeys());
        boolean newInerruptable = this.interruptable;
        HashSet<RunState> newRunStateList = new HashSet<>();
        for(Command command : newCommandList){
            newRequiredSubsystemsKeys.addAll(command.getRequiredSubsystemsKeys());
            newRunStateList.addAll(command.getRunstateList());
            newInerruptable &= command.isIsInterruptible();
        }
        return new SequentialCommandGroup(newCommandList, newRequiredSubsystemsKeys, newRunStateList, newInerruptable);
    }
}
