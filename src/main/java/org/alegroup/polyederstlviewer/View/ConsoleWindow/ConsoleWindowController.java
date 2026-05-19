package org.alegroup.polyederstlviewer.View.ConsoleWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.Constants.BaseCommands;
import org.alegroup.polyederstlviewer.Constants.ConsoleCommandEnum;

public class ConsoleWindowController {

    @FXML
    public VBox console_text;
    public TextField consoleInput;
    public TextArea consoleOutput;
    public Label ghostLabel;
    public Label inputLabel;

    public void initialize(){

        // This is listening for input ENTERED in Console and sending the message to output
        consoleInput.setOnAction((event) -> {

            String userInput = consoleInput.getText();

            // maybe extract logic for sending text to another method?
            consoleOutput.appendText(">> " + userInput + "\n");
            consoleInput.clear();


            // search for deepest subcommand and execute
            ConsoleCommandEnum[] subCommands = BaseCommands.values();
            String userCommand = userInput;
            boolean foundValidCommand = false;

            searchForValidCommand:
            while (true){

                // subCommands is now baseCommands = starting point
                // check if userInput starts with valid command
                boolean startValid = false;

                iterateCommands:
                for (ConsoleCommandEnum command : subCommands){

                    if(userCommand.startsWith(command.getCommandString())){

                        // We found a valid command, remove String and find subcommands or execute
                        startValid = true;

                        userCommand = userCommand.substring(command.getCommandString().length());
                        userCommand = userCommand.strip();

                        if(command.getSubCommands().length > 0){
                            // contains sub commands so go deeper in loop
                            subCommands = command.getSubCommands();
                            break iterateCommands;
                        }else{
                            // no further subcommands, execute if valid
                            if(userCommand.isEmpty()){
                                command.execute(userInput, consoleOutput);
                                foundValidCommand = true;
                            }

                            break searchForValidCommand;
                        }
                    }
                }

                if(!startValid){
                    break searchForValidCommand;
                }
            }

            if(!foundValidCommand){
                consoleOutput.appendText(">> " + "Invalid Command!" + "\n");
            }
        });

        // This is listening for ANY input into the ConsoleInput and giving suggestions - autocomplete
        consoleInput.textProperty().addListener((observable, oldText, newText) -> {

            // overlay so input and ghost input overlay perfectly
            inputLabel.setText(newText);

            // auto suggestion
            ConsoleCommandEnum[] subCommands = BaseCommands.values();
            String[] userCommand = newText.split(" ");
            String suggestion = "";
            boolean possibleMatchFound = false;
            int i = 0;

            searchDeepMatch:
            while (true){

                // check if input already equals to a command.

                iterateCommands:
                for (ConsoleCommandEnum command : subCommands) {

                    // either the userCommand already contains the whole command or just a part

                    if (userCommand[i].equals(command.getCommandString()) && !userCommand[i].isEmpty()) {

                        // it already starts with the whole String. check sub Commands

                        suggestion += command.getCommandString() + " ";

                        if(!command.getCommandString().isEmpty() && userCommand.length > i + 1){
                            i++;
                            subCommands = command.getSubCommands();
                        }else{
                            break searchDeepMatch;
                        }

                        break;

                    } else if (command.getCommandString().contains(userCommand[i]) && !userCommand[i].isEmpty()) {

                        // only contains a part
                        suggestion += command.getCommandString();
                        break searchDeepMatch;
                    } else {
                        break searchDeepMatch;
                    }
                }


            }


            ghostLabel.setText(suggestion);
        });
    }
}
