package org.alegroup.polyederstlviewer.view.consolewindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.constants.AllCommands;
import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.control.commands.CommandHandler;
import org.alegroup.polyederstlviewer.control.commands.CommandWriter;
import org.alegroup.polyederstlviewer.model.console.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

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
    private String currentBaseContext = "main";

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

        commandWriter.writeCommand(new CommandBlueprint("clear", "clear",
                ConsoleBufferContext.MAIN.context(), ConsoleBufferContext.MAIN.context()));

        commandWriter.writeCommand(new CommandBlueprint("color", "color",
                ConsoleBufferContext.MAIN.context(), ConsoleBufferContext.MAIN.context()));

        commandWriter.writeCommand(new CommandBlueprint("new command", "new command",
                ConsoleBufferContext.MAIN.context(), ConsoleBufferContext.MAIN.context()));

        commandWriter.writeCommand(new CommandBlueprint("server start", "server start",
                ConsoleBufferContext.MAIN.context(), ConsoleBufferContext.SERVER.context()));

        commandWriter.writeCommand(new CommandBlueprint("ip", "server ip",
                ConsoleBufferContext.SERVER.context(), ConsoleBufferContext.SERVER.context()));

        commandWriter.writeCommand(new CommandBlueprint("return", "server return",
                ConsoleBufferContext.SERVER.context(), ConsoleBufferContext.MAIN.context()));

        commandWriter.writeCommand(new CommandBlueprint("stop", "server stop",
                ConsoleBufferContext.SERVER.context(), ConsoleBufferContext.MAIN.context()));

        commandWriter.writeCommand(new CommandBlueprint("client connect", "client connect",
                ConsoleBufferContext.MAIN.context(), ConsoleBufferContext.CLIENT.context()));

        commandWriter.writeCommand(new CommandBlueprint("data send", "data send",
                ConsoleBufferContext.CLIENT.context(), ConsoleBufferContext.CLIENT.context()));

        commandWriter.writeCommand(new CommandBlueprint("translate", "object translate",
                ConsoleBufferContext.CLIENT.context(), ConsoleBufferContext.CLIENT.context()));

        commandWriter.writeCommand(new CommandBlueprint("rotate", "object rotate",
                ConsoleBufferContext.CLIENT.context(), ConsoleBufferContext.CLIENT.context()));

        commandWriter.writeCommand(new CommandBlueprint("stop", "client stop",
                ConsoleBufferContext.CLIENT.context(), ConsoleBufferContext.MAIN.context()));

        commandWriter.writeCommand(new CommandBlueprint("read file", "read file",
                ConsoleBufferContext.MAIN.context(), ConsoleBufferContext.MAIN.context()));

        consoleInput.setOnAction(e -> {
            String userInput = consoleInput.getText();
            console.writeUserInputToConsole("STATE: " + currentBaseContext + "| " + userInput);
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
            this.ghostLabel.setText("");
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
                this.ghostLabel.setText("");
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
            console.makeOutputToCurrentContext("Invalid Command");
            return;
        }

        int argSize = cmd.length - 1;
        String[] args = new String[argSize];
        if (argSize > 0)
        {
            args = Arrays.copyOfRange(cmd, 1, cmd.length);
        }

        CommandBlueprint commandBlueprint =
                commandHandler.validateCommand(cmd[0], this.currentBaseContext);

        if (commandBlueprint == null)
        {
            console.makeOutputToCurrentContext("Invalid Command");
            return;
        }

        if (this.currentBaseContext.equals(commandBlueprint.getNeededContext()))
        {

            AllCommands executable = commandHandler.getExecutable(commandBlueprint.getMethodName());
            if (executable == null)
            {
                console.makeOutputToCurrentContext(
                        "no executable found for command: " + commandBlueprint.getCommand());
                return;
            }

            boolean executed = executable.execute(console, args);

            if (executed)
            {
                currentBaseContext = commandBlueprint.getNextContext();
            }
            else
            {
                console.makeOutputToCurrentContext("Command did not execute correctly");
            }

        }
        else
        {
            console.makeOutputToCurrentContext(
                    "Invalid context for this command! Current context: "
                            + this.currentBaseContext
                            + ", needed context: "
                            + commandBlueprint.getNeededContext());
        }
    }
}