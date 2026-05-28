package org.alegroup.polyederstlviewer.constants;

import org.alegroup.polyederstlviewer.control.commandExecutables.ClearCommand;
import org.alegroup.polyederstlviewer.control.commandExecutables.ColorCommand;
import org.alegroup.polyederstlviewer.control.commandExecutables.CommandExecuter;
import org.alegroup.polyederstlviewer.control.commandExecutables.NewCommandCommand;
import org.alegroup.polyederstlviewer.model.commands.ConsoleObject;

public enum AllCommands {

    CLEAR("clear", new ClearCommand()),
    COLOR("color", new ColorCommand()),
    NEW_COMMAND("new command", new NewCommandCommand());


    private final String methodName;
    private final CommandExecuter command;
    AllCommands(String methodName, CommandExecuter command){
        this.methodName = methodName;
        this.command = command;
    }

    public boolean execute(ConsoleObject console, String[] args){
        if(this.command == null){
            return false;
        }
        return this.command.execute(console, args);
    }

    /* getter */
    public String getMethodName() {
        return methodName;
    }
}
