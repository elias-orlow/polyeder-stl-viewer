package org.alegroup.polyederstlviewer.view.consolewindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.constants.AllCommands;
import org.alegroup.polyederstlviewer.control.commands.CommandHandler;
import org.alegroup.polyederstlviewer.control.commands.CommandWriter;
import org.alegroup.polyederstlviewer.model.console.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;
import static org.alegroup.polyederstlviewer.constants.ConsoleWindowControllerConstants.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Controller responsible for managing the interactive console window.
 * It handles user input, command validation, auto-suggestions, context switching,
 * and output rendering. The console supports multiple command contexts such as
 * MAIN, SERVER, and CLIENT, and executes commands defined in the command system.
 */
public class ConsoleWindowController
{

    /**
     * Container holding console text elements.
     */
    @FXML
    public VBox console_text;

    /**
     * Text field where the user enters commands.
     */
    public TextField consoleInput;

    /**
     * Text area displaying console output.
     */
    public TextArea consoleOutput;

    /**
     * Label used for showing auto-suggestions based on user input.
     */
    public Label ghostLabel;

    /**
     * Label mirroring the current user input for visual feedback.
     */
    public Label inputLabel;

    /**
     * Current active command context (MAIN, SERVER, CLIENT).
     */
    private String currentBaseContext = DEFAULT_BASE_CONTEXT;

    /**
     * Initializes the console window by loading available commands, setting up
     * the console object, and registering listeners for user input and auto-suggestions.
     *
     * @precondition consoleInput != null AND consoleOutput != null
     * @postcondition Console is fully initialized and ready to process user input
     */
    public void initialize ()
    {

        CommandWriter commandWriter = new CommandWriter();
        CommandHandler commandHandler = new CommandHandler();
        ConsoleObject console = new ConsoleObject(consoleOutput, consoleInput);

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_CLEAR, METHOD_CLEAR,
                MAIN_CONTEXT, MAIN_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_COLOR, METHOD_COLOR,
                MAIN_CONTEXT, MAIN_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_NEW_COMMAND, METHOD_NEW_COMMAND,
                MAIN_CONTEXT, MAIN_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_SERVER_START, METHOD_SERVER_START,
                MAIN_CONTEXT, SERVER_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_IP, METHOD_SERVER_IP,
                SERVER_CONTEXT, SERVER_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_RETURN, METHOD_SERVER_RETURN,
                SERVER_CONTEXT, MAIN_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_STOP, METHOD_SERVER_STOP,
                SERVER_CONTEXT, MAIN_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_CLIENT_CONNECT, METHOD_CLIENT_CONNECT,
                MAIN_CONTEXT, CLIENT_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_DATA_SEND, METHOD_DATA_SEND,
                CLIENT_CONTEXT, CLIENT_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_TRANSLATE, METHOD_OBJECT_TRANSLATE,
                CLIENT_CONTEXT, CLIENT_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_ROTATE, METHOD_OBJECT_ROTATE,
                CLIENT_CONTEXT, CLIENT_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_STOP, METHOD_CLIENT_STOP,
                CLIENT_CONTEXT, MAIN_CONTEXT));

        commandWriter.writeCommand(new CommandBlueprint(COMMAND_READ_FILE, METHOD_READ_FILE,
                MAIN_CONTEXT, MAIN_CONTEXT));

        consoleInput.setOnAction(e -> {
            String userInput = consoleInput.getText();
            console.writeUserInputToConsole(USER_INPUT_STATE_PREFIX + currentBaseContext
                    + USER_INPUT_CONTEXT_SEPARATOR + userInput);
            reactToUserInput(userInput, console, commandHandler);
            consoleInput.clear();
        });

        consoleInput.textProperty().addListener((obs, oldText, newText) -> {
            showUserInputInTextField(newText);
            autoSuggestion(newText, commandHandler);
        });
    }

    /**
     * Displays the current user input in the mirrored label.
     *
     * @param input the text entered by the user
     * @precondition input != null
     * @postcondition inputLabel displays the provided text
     */
    private void showUserInputInTextField (String input)
    {
        inputLabel.setText(input);
    }

    /**
     * Generates auto-suggestions based on the current input and active context.
     *
     * @param input          the text entered by the user
     * @param commandHandler handler used to retrieve available commands
     * @precondition commandHandler != null
     * @postcondition ghostLabel displays a matching command or is cleared
     */
    private void autoSuggestion (String input, CommandHandler commandHandler)
    {

        ArrayList<CommandBlueprint> possibleCommands =
                commandHandler.getSameContextCommands(this.currentBaseContext);

        if (possibleCommands.isEmpty() || input.isEmpty())
        {
            this.ghostLabel.setText(EMPTY_TEXT);
            return;
        }

        for (CommandBlueprint command : possibleCommands)
        {
            if (command.getCommand().startsWith(input))
            {
                this.ghostLabel.setText(command.getCommand());
                break;
            }
            else
            {
                this.ghostLabel.setText(EMPTY_TEXT);
            }
        }
    }

    /**
     * Processes the user input, validates the command, checks context compatibility,
     * and executes the corresponding command if valid.
     *
     * @param userInput      the raw input entered by the user
     * @param console        the console object used for output
     * @param commandHandler handler used to validate and execute commands
     * @precondition console != null AND commandHandler != null
     * @postcondition Command is executed or an error message is displayed
     */
    private void reactToUserInput (String userInput, ConsoleObject console, CommandHandler commandHandler)
    {

        String[] cmd = commandHandler.getCommandFromRaw(userInput);

        if (cmd == null)
        {
            console.makeOutputToCurrentContext(INVALID_COMMAND_MESSAGE);
            return;
        }

        int argSize = cmd.length - ARGUMENT_COUNT_OFFSET;
        String[] args = new String[argSize];
        if (argSize > NO_ARGUMENTS)
        {
            args = Arrays.copyOfRange(cmd, FIRST_ARGUMENT_INDEX, cmd.length);
        }

        CommandBlueprint commandBlueprint =
                commandHandler.validateCommand(cmd[COMMAND_NAME_INDEX], this.currentBaseContext);

        if (commandBlueprint == null)
        {
            console.makeOutputToCurrentContext(INVALID_COMMAND_MESSAGE);
            return;
        }

        if (this.currentBaseContext.equals(commandBlueprint.getNeededContext()))
        {

            AllCommands executable = commandHandler.getExecutable(commandBlueprint.getMethodName());
            if (executable == null)
            {
                console.makeOutputToCurrentContext(
                        NO_EXECUTABLE_FOUND_MESSAGE_PREFIX + commandBlueprint.getCommand());
                return;
            }

            boolean executed = executable.execute(console, args);

            if (executed)
            {
                currentBaseContext = commandBlueprint.getNextContext();
            }
            else
            {
                console.makeOutputToCurrentContext(COMMAND_EXECUTION_FAILED_MESSAGE);
            }

        }
        else
        {
            console.makeOutputToCurrentContext(
                    INVALID_CONTEXT_MESSAGE_PREFIX
                            + this.currentBaseContext
                            + NEEDED_CONTEXT_MESSAGE_PART
                            + commandBlueprint.getNeededContext());
        }
    }
}
