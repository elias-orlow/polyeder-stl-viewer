package org.alegroup.polyederstlviewer.view.consolewindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.constants.AllCommands;
import org.alegroup.polyederstlviewer.control.commands.CommandHandler;
import org.alegroup.polyederstlviewer.control.commands.CommandWriter;
import org.alegroup.polyederstlviewer.model.commands.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.commands.ConsoleObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ConsoleWindowController {

    @FXML
    public VBox console_text;
    public TextField consoleInput;
    public TextArea consoleOutput;
    public Label ghostLabel;
    public Label inputLabel;

    private String currentBaseContext = "main";

    public void initialize(){

        // load available commands
        CommandWriter commandWriter = new CommandWriter();
        CommandHandler commandHandler = new CommandHandler();
        ConsoleObject console = new ConsoleObject(consoleOutput, consoleInput);

        // Basic command - command definition can be outsourced theoretically to make code cleaner
        commandWriter.writeCommand(new CommandBlueprint("clear", "clear", "main", "main"));
        commandWriter.writeCommand(new CommandBlueprint("color", "color", "main", "main"));
        commandWriter.writeCommand(new CommandBlueprint("new command", "new command", "main", "main"));
        // Server commands
        /*
        commandWriter.writeCommand(new CommandBlueprint("server start", "server start", "main", "server"));
        commandWriter.writeCommand(new CommandBlueprint("info", "server info", "server", "server"));
        commandWriter.writeCommand(new CommandBlueprint("return", "server return", "server", "main"));
        commandWriter.writeCommand(new CommandBlueprint("stop", "server stop", "server", "server"));
         */
        
        consoleInput.setOnAction(e -> {

            String userInput = consoleInput.getText();
            console.writeUserInputToConsole("STATE: " + currentBaseContext + "| " + userInput);
            consoleInput.clear();
            System.out.println("User entered something in the console: " + userInput);

            reactToUserInput(userInput, console, commandHandler);
        });

        consoleInput.textProperty().addListener((obs, oldText, newText) -> {

            showUserInputInTextField(newText);
            autoSuggestion(newText, commandHandler);
        });
    }

    private void showUserInputInTextField(String input){
        inputLabel.setText(input);
    }

    private void autoSuggestion(String input, CommandHandler commandHandler){

        ArrayList<CommandBlueprint> possibleCommands = commandHandler.getSameContextCommands(this.currentBaseContext);

        if(possibleCommands.isEmpty() || input.isEmpty()){
            this.ghostLabel.setText("");
            return;
        }

        for(CommandBlueprint command : possibleCommands){
            if(command.getCommand().startsWith(input)){
                this.ghostLabel.setText(command.getCommand());
                break;
            }
        }
    }

    private void reactToUserInput(String userInput, ConsoleObject console, CommandHandler commandHandler){

        String[] cmd = commandHandler.getCommandFromRaw(userInput);

        if(cmd == null){
            console.makeOutputToCurrentContext("Invalid Command");
        }else{

            int argSize = cmd.length - 1;
            String[] args = new String[argSize];
            if(argSize > 0){
                args = Arrays.copyOfRange(cmd, 1, cmd.length);
            }


            CommandBlueprint commandBlueprint = commandHandler.validateCommand(cmd[0]);
            if(commandBlueprint == null){
                console.makeOutputToCurrentContext("Invalid Command");
                return;
            }

            // now we have a valid command. Execute only if context matches
            if(this.currentBaseContext.equals(commandBlueprint.getNeededContext())){
                AllCommands executable = commandHandler.getExecutable(commandBlueprint.getMethodName());
                if(executable == null){
                    console.makeOutputToCurrentContext("no executable found for command: " + commandBlueprint.getCommand());
                    return;
                }
                boolean executed = executable.execute(console, args);

                if(executed){
                    currentBaseContext = commandBlueprint.getNextContext();
                }else{
                    console.makeOutputToCurrentContext("Command did not execute correctly");
                }

            }else{
                console.makeOutputToCurrentContext("Invalid context for this command");
            }
        }
    }
}
