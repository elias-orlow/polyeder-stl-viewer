package org.alegroup.polyederstlviewer.constants;

/**
 * Constants used by the ConsoleWindowController.
 */
public interface ConsoleWindowControllerConstants
{

    /**
     * Initial console base context.
     */
    String DEFAULT_BASE_CONTEXT = ConsoleBufferContext.MAIN.context();

    /**
     * Main console context.
     */
    String MAIN_CONTEXT = ConsoleBufferContext.MAIN.context();

    /**
     * Server console context.
     */
    String SERVER_CONTEXT = ConsoleBufferContext.SERVER.context();

    /**
     * Client console context.
     */
    String CLIENT_CONTEXT = ConsoleBufferContext.CLIENT.context();

    /**
     * Empty text used to clear labels.
     */
    String EMPTY_TEXT = "";

    /**
     * Prefix used when writing the current state to the console.
     */
    String USER_INPUT_STATE_PREFIX = "STATE: ";

    /**
     * Separator between context and user input.
     */
    String USER_INPUT_CONTEXT_SEPARATOR = "| ";

    /**
     * Message shown when a command is invalid.
     */
    String INVALID_COMMAND_MESSAGE = "Invalid Command";

    /**
     * Message prefix shown when no executable was found.
     */
    String NO_EXECUTABLE_FOUND_MESSAGE_PREFIX = "no executable found for command: ";

    /**
     * Message shown when command execution fails.
     */
    String COMMAND_EXECUTION_FAILED_MESSAGE = "Command did not execute correctly";

    /**
     * Message prefix shown when the command context is invalid.
     */
    String INVALID_CONTEXT_MESSAGE_PREFIX = "Invalid context for this command! Current context: ";

    /**
     * Message part before the needed context.
     */
    String NEEDED_CONTEXT_MESSAGE_PART = ", needed context: ";

    /**
     * Index of the command name in a parsed command array.
     */
    int COMMAND_NAME_INDEX = 0;

    /**
     * Index of the first argument in a parsed command array.
     */
    int FIRST_ARGUMENT_INDEX = 1;

    /**
     * Offset used to calculate the number of arguments.
     */
    int ARGUMENT_COUNT_OFFSET = 1;

    /**
     * Value representing no arguments.
     */
    int NO_ARGUMENTS = 0;

    String COMMAND_CLEAR = "clear";
    String METHOD_CLEAR = COMMAND_CLEAR;

    String COMMAND_COLOR = "color";
    String METHOD_COLOR = COMMAND_COLOR;

    String COMMAND_NEW_COMMAND = "new command";
    String METHOD_NEW_COMMAND = COMMAND_NEW_COMMAND;

    String COMMAND_SERVER_START = "server start";
    String METHOD_SERVER_START = COMMAND_SERVER_START;

    String COMMAND_IP = "ip";
    String METHOD_SERVER_IP = "server ip";

    String COMMAND_RETURN = "return";
    String METHOD_SERVER_RETURN = "server return";

    String COMMAND_STOP = "stop";
    String METHOD_SERVER_STOP = "server stop";
    String METHOD_CLIENT_STOP = "client stop";

    String COMMAND_CLIENT_CONNECT = "client connect";
    String METHOD_CLIENT_CONNECT = COMMAND_CLIENT_CONNECT;

    String COMMAND_DATA_SEND = "data send";
    String METHOD_DATA_SEND = COMMAND_DATA_SEND;

    String COMMAND_TRANSLATE = "translate";
    String METHOD_OBJECT_TRANSLATE = "object translate";

    String COMMAND_ROTATE = "rotate";
    String METHOD_OBJECT_ROTATE = "object rotate";

    String COMMAND_READ_FILE = "read file";
    String METHOD_READ_FILE = COMMAND_READ_FILE;
}
