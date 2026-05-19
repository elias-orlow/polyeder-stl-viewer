package org.alegroup.polyederstlviewer.view.consolewindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.constants.BaseCommands;
import org.alegroup.polyederstlviewer.constants.ConsoleCommandEnum;

public class ConsoleWindowController {

    @FXML
    public VBox console_text;
    public TextField consoleInput;
    public TextArea consoleOutput;
    public Label ghostLabel;
    public Label inputLabel;

    public void initialize(){

        ConsoleCommandEnum[] commandContext = BaseCommands.values();
        ConsoleHandler consoleHandler = new ConsoleHandler(commandContext);

        // This is listening for input ENTERED in Console and sending the message to output
        consoleInput.setOnAction((event) -> {

            String userInput = consoleInput.getText();

            // maybe extract logic for sending text to another method?
            consoleOutput.appendText(">> " + userInput + "\n");
            consoleInput.clear();

            // baseCommands hier nur als Platzhalter. Nachher state dependent
            consoleHandler.executeCommand(userInput, consoleOutput);
        });

        // This is listening for ANY input into the ConsoleInput and giving suggestions - autocomplete
        consoleInput.textProperty().addListener((observable, oldText, newText) -> {

            // overlay so input and ghost input overlay perfectly
            inputLabel.setText(newText);

            // auto suggestion
            ConsoleCommandEnum[] subCommands = BaseCommands.values();
            String[] userCommand = newText.split(" ");
            String suggestion = "";
            int i = 0;

            searchDeepMatch:
            while (true){

                boolean possibleMatchFound = false;

                // check if input already equals to a command.

                iterateCommands:
                for (ConsoleCommandEnum command : subCommands) {

                    // either the userCommand already contains the whole command or just a part

                    if (userCommand[i].equals(command.getCommandString()) && !userCommand[i].isEmpty()) {

                        // it already starts with the whole String. check sub Commands
                        possibleMatchFound = true;

                        suggestion += command.getCommandString() + " ";

                        if(!command.getCommandString().isEmpty() && userCommand.length > i + 1){
                            i++;
                            subCommands = command.getSubCommands();
                        }else{
                            break searchDeepMatch;
                        }

                        break;

                    } else if (command.getCommandString().startsWith(userCommand[i]) && !userCommand[i].isEmpty()) {

                        // only contains a part
                        suggestion += command.getCommandString();
                        break searchDeepMatch;
                    }
                }

                if(!possibleMatchFound){
                    break searchDeepMatch;
                }
            }


            ghostLabel.setText(suggestion);
        });
    }
}
