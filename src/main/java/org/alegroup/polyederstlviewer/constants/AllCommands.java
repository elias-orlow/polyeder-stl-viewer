package org.alegroup.polyederstlviewer.constants;

import org.alegroup.polyederstlviewer.control.commandExecutables.*;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

public enum AllCommands {

    // Basic
    CLEAR("clear", new ClearCommand()),
    COLOR("color", new ColorCommand()),
    NEW_COMMAND("new command", new NewCommandCommand()),

    // Server
    SERVER_START("server start", new ServerStartCommand()),
    SERVER_RETURN("server return", new ServerReturnCommand()),

    // Client
    CLIENT_CONNECT("client connect", new ClientConnectCommand());


    private final String methodName;
    private final CommandExecuter command;
    AllCommands(String methodName, CommandExecuter command){
        this.methodName = methodName;
        this.command = command;
    }

    public boolean execute(ConsoleObject console, String[] args){
        // command execution is always seen as a "success" when respective method is 'null'
        if(this.command == null){
            return true;
        }

        return this.command.execute(console, args);
    }

    /* getter */
    public String getMethodName() {
        return methodName;
    }
}
