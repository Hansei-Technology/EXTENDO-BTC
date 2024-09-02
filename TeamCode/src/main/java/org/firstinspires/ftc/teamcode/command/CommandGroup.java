package org.firstinspires.ftc.teamcode.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public interface CommandGroup extends Command {
    default CommandGroup addCommands(Command... commands) {
        return addCommands(Arrays.asList(commands));
    }
    CommandGroup addCommands(Collection<Command> commands);
}
