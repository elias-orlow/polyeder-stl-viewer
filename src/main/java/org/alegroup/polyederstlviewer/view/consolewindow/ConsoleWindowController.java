package org.alegroup.polyederstlviewer.view.consolewindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.constants.AllCommands;
import org.alegroup.polyederstlviewer.control.commands.CommandHandler;
import org.alegroup.polyederstlviewer.control.commands.CommandWriter;
import org.alegroup.polyederstlviewer.model.geometry.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ConsoleWindowController {

    @FXML
    public VBox console_text;
    public TextField consoleInput;
    public TextArea consoleOutput;
    public Label ghostLabel;
    public Label inputLabel;

    private String currentContext = "main";

    public void initialize(){

        // load available commands
        CommandWriter commandWriter = new CommandWriter();
        CommandHandler commandHandler = new CommandHandler();
        ConsoleObject console = new ConsoleObject(consoleOutput, consoleInput);

        commandWriter.writeCommand(new CommandBlueprint("clear", "clear", "main", "main"));
        commandWriter.writeCommand(new CommandBlueprint("color", "color", "main", "main"));
        commandWriter.writeCommand(new CommandBlueprint("new command", "new command", "main", "main"));

        consoleInput.setOnAction(e -> {

            String userInput = consoleInput.getText();
            console.writeUserInputToConsole("STATE: " + currentContext + "| " + userInput);
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

        ArrayList<CommandBlueprint> possibleCommands = commandHandler.getSameContextCommands(this.currentContext);

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
            console.makeOutput("Invalid Command");
        }else{

            int argSize = cmd.length - 1;
            String[] args = new String[argSize];
            if(argSize > 0){
                args = Arrays.copyOfRange(cmd, 1, cmd.length);
            }


            CommandBlueprint commandBlueprint = commandHandler.validateCommand(cmd[0]);
            if(commandBlueprint == null){
                console.makeOutput("Invalid Command");
                return;
            }

            // now we have a valid command. Execute only if context matches
            if(this.currentContext.equals(commandBlueprint.getNeededContext())){
                AllCommands executable = commandHandler.getExecutable(commandBlueprint.getMethodName());
                if(executable == null){
                    console.makeOutput("no executable found for command: " + commandBlueprint.getCommand());
                    return;
                }
                boolean executed = executable.execute(console, args);

                if(executed){
                    currentContext = commandBlueprint.getNextContext();
                }else{
                    console.makeOutput("Command did not execute correctly");
                }

            }else{
                console.makeOutput("Invalid context for this command");
            }
        }
    }
}
