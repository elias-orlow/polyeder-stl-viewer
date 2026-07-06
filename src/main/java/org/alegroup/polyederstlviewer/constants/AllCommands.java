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
    CLEAR(CommandConstants.CMD_CLEAR, new ClearCommand()),
    COLOR(CommandConstants.CMD_COLOR, new ColorCommand()),
    NEW_COMMAND(CommandConstants.CMD_NEW_COMMAND, new NewCommandCommand()),
    READ_FILE(CommandConstants.CMD_READ_FILE, new ReadFileCommand()),

    // Server
    SERVER_START(CommandConstants.CMD_SERVER_START, new ServerStartCommand()),
    SERVER_IP(CommandConstants.CMD_SERVER_IP, new ServerIPCommand()),
    SERVER_RETURN(CommandConstants.CMD_SERVER_RETURN, new ServerReturnCommand()),
    SERVER_STOP(CommandConstants.CMD_SERVER_STOP, new ServerStopCommand()),

    // Client
    CLIENT_CONNECT(CommandConstants.CMD_CLIENT_CONNECT, new ClientConnectCommand()),
    CLIENT_DATA_SEND(CommandConstants.CMD_CLIENT_DATA_SEND, new ClientDataSendCommand()),
    CLIENT_STOP(CommandConstants.CMD_CLIENT_STOP, new ClientStopCommand()),
    CLIENT_TRANSLATE(CommandConstants.CMD_CLIENT_TRANSLATE, new ClientTranslateObjectCommand()),
    CLIENT_ROTATE(CommandConstants.CMD_CLIENT_ROTATE, new ClientRotateObjectCommand());

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