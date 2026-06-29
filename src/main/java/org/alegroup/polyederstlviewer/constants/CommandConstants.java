package org.alegroup.polyederstlviewer.constants;

/**
 * Centralized constants for all command executables.
 * All literal strings used by command classes must be placed here.
 */
public interface CommandConstants
{

    // --- Shared command error messages ---
    String INVALID_ARGUMENTS =
            "Invalid arguments. Must only provide a valid port number";

    String JSON_LOAD_ERROR_PREFIX =
            "Something went wrong trying to load the json file ";

    // --- Shared parsing constants ---
    String COMMAND_SPLIT_PATTERN = "--";
    String COMMAND_STRIP_PATTERN = "-";

    // --- Shared context formatting ---
    String CONTEXT_SEPARATOR = "-";

    // --- Shared numeric constants ---
    int FIRST_INDEX = 0;

    // --- ServerReturnCommand ---
    String MAIN_CONTEXT = ConsoleBufferContext.MAIN.context();

    // --- ServerIPCommand ---
    String IP_HEADER_PREFIX = "IP-Addresses for ";
    String IP_ENTRY_PREFIX = "---> ";
    String EMPTY_LINE = "";
    String NETWORK_ERROR = "Could not fetch network interfaces!";

    // --- ReadFileCommand ---
    String READFILE_INVALID_ARGUMENT =
            "Invalid argument. Provide path to .stl file --FILE_PATH";

    String READFILE_INVALID_PATH =
            "Invalid path provided or could not find file! Provide path to .stl file --FILE_PATH";

    String READFILE_PARSE_ERROR =
            "Could not read the provided file! Make sure it is a .stl file.";

    String READFILE_RENDERING =
            "Rendering file...";

    // --- NewCommandCommand ---
    String NEWCOMMAND_INVALID_ARGUMENTS =
            "Invalid arguments given as new command. 'new command --COMMAND --METHOD_NAME --NEEDED_CONTEXT --NEXT_CONTEXT'";

    // --- ColorCommand ---
    String COLOR_INVALID =
            "Invalid color command!";

    String COLOR_RED_STYLE =
            "-fx-control-inner-background: black; -fx-text-fill: red; "
                    + "-fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; "
                    + "-fx-highlight-text-fill: white; -fx-focus-color: transparent; "
                    + "-fx-faint-focus-color: transparent; -fx-font-size: 14px; ";

    String COLOR_BLUE_STYLE =
            "-fx-control-inner-background: black; -fx-text-fill: blue; "
                    + "-fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; "
                    + "-fx-highlight-text-fill: white; -fx-focus-color: transparent; "
                    + "-fx-faint-focus-color: transparent; -fx-font-size: 14px; ";

    String COLOR_GREEN_STYLE =
            "-fx-control-inner-background: black; -fx-text-fill: green; "
                    + "-fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; "
                    + "-fx-highlight-text-fill: white; -fx-focus-color: transparent; "
                    + "-fx-faint-focus-color: transparent; -fx-font-size: 14px; ";

    String COLOR_WHITE_STYLE =
            "-fx-control-inner-background: black; -fx-text-fill: white; "
                    + "-fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; "
                    + "-fx-highlight-text-fill: white; -fx-focus-color: transparent; "
                    + "-fx-faint-focus-color: transparent; -fx-font-size: 14px; ";

    String COLOR_PURPLE_STYLE =
            "-fx-control-inner-background: black; -fx-text-fill: purple; "
                    + "-fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; "
                    + "-fx-highlight-text-fill: white; -fx-focus-color: transparent; "
                    + "-fx-faint-focus-color: transparent; -fx-font-size: 14px; ";

    // --- ClientTranslateObjectCommand ---
    String CLIENT_TRANSLATE_INVALID_ARGUMENTS =
            "Invalid arguments provided. translate --X --Y --Z";

    String CLIENT_TRANSLATE_FLOAT_ERROR =
            "Arguments provided must be of type float. translate --X --Y --Z";

    String CLIENT_TRANSLATE_SENT =
            "Sent translate data to server!";

    String CLIENT_TRANSLATE_PREFIX =
            "tr";

    // --- ClientRotateObjectCommand ---
    String CLIENT_ROTATE_INVALID_ARGUMENTS =
            "Invalid arguments provided. rotate --X --Y --Z";

    String CLIENT_ROTATE_FLOAT_ERROR =
            "Arguments provided must be of type float. rotate --X --Y --Z";

    String CLIENT_ROTATE_SENT =
            "Sent rotate data to server!";

    String CLIENT_ROTATE_PREFIX =
            "ro";

    // --- ClientDataSendCommand ---
    String CLIENT_DATA_INVALID_ARGUMENTS =
            "Invalid arguments: 'data send --DATA'";

    String CLIENT_DATA_PREFIX =
            "se";

    String CLIENT_DATA_SENT =
            "Sent data to server!";

    // --- ClientConnectCommand ---
    String CLIENT_CONNECT_INVALID_ARGUMENTS =
            "Invalid arguments provided! 'client connect --HOST_NAME --PORT_NUMBER'";

    String CLIENT_CONNECT_CONTEXT_SEPARATOR =
            "-";

}