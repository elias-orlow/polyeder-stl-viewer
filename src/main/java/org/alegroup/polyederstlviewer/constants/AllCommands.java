package org.alegroup.polyederstlviewer.constants;

import org.alegroup.polyederstlviewer.control.commands.ClearCommand;
import org.alegroup.polyederstlviewer.control.commands.CommandExecuter;
import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

public enum AllCommands {

    CLEAR("clear", new ClearCommand());


    private final String methodName;
    private final CommandExecuter command;
    AllCommands(String methodName, CommandExecuter command){
        this.methodName = methodName;
        this.command = command;
    }

    public void execute(ConsoleObject console, String[] args){
        this.command.execute(console, args);
    }

    /* getter */
    public String getMethodName() {
        return methodName;
    }
}
