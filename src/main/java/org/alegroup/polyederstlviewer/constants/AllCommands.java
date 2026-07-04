package org.alegroup.polyederstlviewer.constants;

import org.alegroup.polyederstlviewer.control.commandExecutables.*;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Central registry of all available console commands.
 * <p>
 * Each enum entry maps a textual command identifier to its corresponding
 * CommandExecuter implementation. This provides a unified lookup mechanism
 * for command execution and ensures that all commands are centrally defined,
 * discoverable, and consistently referenced throughout the application.
 */
public enum AllCommands
{

    // Basic
    CLEAR("clear", new ClearCommand()),
    COLOR("color", new ColorCommand()),
    NEW_COMMAND("new command", new NewCommandCommand()),
    READ_FILE("read file", new ReadFileCommand()),

    // Server
    SERVER_START("server start", new ServerStartCommand()),
    SERVER_IP("server ip", new ServerIPCommand()),
    SERVER_RETURN("server return", new ServerReturnCommand()),
    SERVER_STOP("server stop", new ServerStopCommand()),

    // Client
    CLIENT_CONNECT("client connect", new ClientConnectCommand()),
    CLIENT_DATA_SEND("data send", new ClientDataSendCommand()),
    CLIENT_STOP("client stop", new ClientStopCommand()),
    CLIENT_TRANSLATE("object translate", new ClientTranslateObjectCommand()),
    CLIENT_ROTATE("object rotate", new ClientRotateObjectCommand());

    private final String methodName;
    private final CommandExecuter command;


    /**
     * Creates a new command registry entry.
     *
     * @param methodName the textual command identifier
     * @param command    the command implementation
     * @precondition methodName != null
     * @postcondition A new command entry with the given method name and command implementation is created
     */
    AllCommands (String methodName, CommandExecuter command)
    {
        this.methodName = methodName;
        this.command = command;
    }

    /**
     * Executes the command with the given console and arguments.
     * <p>
     * If the command implementation is {@code null}, the execution is treated
     * as successful.
     *
     * @param console the console object used for command output and interaction
     * @param args    the command arguments
     * @return {@code true} if the command was executed successfully, otherwise {@code false}
     * @precondition console != null AND args != null
     * @postcondition The command is executed or treated as successful if no command implementation exists
     */
    public boolean execute (ConsoleObject console, String[] args)
    {
        // command execution is always seen as a "success" when respective method is 'null'
        if (this.command == null)
        {
            return true;
        }
        return this.command.execute(console, args);
    }

    /**
     * Returns the textual command identifier.
     *
     * @return the method name of the command
     * @precondition none
     * @postcondition A non-null command method name is returned
     */
    public String getMethodName ()
    {
        return methodName;
    }
}