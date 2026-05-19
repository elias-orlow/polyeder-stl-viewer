package org.alegroup.polyederstlviewer.View.ConsoleWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.Constants.KnownBaseCommands;
import org.alegroup.polyederstlviewer.Constants.SubCommand;

public class ConsoleWindowController {

    @FXML
    public VBox console_text;
    public TextField consoleInput;
    public TextArea consoleOutput;
    public Label ghostLabel;

    public void initialize(){

        // This is listening for input ENTERED in Console and sending the message to output
        consoleInput.setOnAction((event) -> {

            String command = consoleInput.getText();

            // maybe extract logic for sending text to another method?
            consoleOutput.appendText(">> " + command + "\n");
            consoleInput.clear();

            // need to check if valid command against enum
            // first check against BaseCommands
            for(KnownBaseCommands knownCommand : KnownBaseCommands.values()){

                // check if the command from the user contains a base command
                if(command.contains(knownCommand.getCommandString())){

                    // der bums muss loopen für unendlich viele subcommands
                    // it does so check against possible subCommands
                    if(knownCommand.getSubCommands().length > 0){
                        // subcommands exist check for valid subcommands
                        for(SubCommand subCommand : knownCommand.getSubCommands()){

                            if(command.contains(subCommand.getCommandString())){
                                subCommand.execute(command, consoleOutput);
                            }
                        }
                    }
                }

                if(command.equals(knownCommand.getCommandString())){
                    // command is known and should be executed - no twin/double commands?
                    knownCommand.execute(command, consoleOutput);
                    break;
                }
            }
        });

        // This is listening for ANY input into the ConsoleInput and giving suggestions
        consoleInput.textProperty().addListener((observable, oldText, newText) -> {

            String suggestion = "";
            if(!newText.isEmpty()){
                for (KnownBaseCommands knownCommand : KnownBaseCommands.values()) {
                    if (knownCommand.getCommandString().startsWith(newText)) {
                        suggestion = knownCommand.getCommandString();
                        break;
                    }
                }
            }

            ghostLabel.setText(suggestion);
        });
    }
}
