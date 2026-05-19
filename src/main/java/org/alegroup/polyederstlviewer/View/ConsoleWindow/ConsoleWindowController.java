package org.alegroup.polyederstlviewer.View.ConsoleWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.Constants.ConsoleWindowConstants;

public class ConsoleWindowController implements ConsoleWindowConstants {

    @FXML
    public VBox console_text;
    public TextField consoleInput;
    public TextArea consoleOutput;
    public Label ghostLabel;

    public void initialize(){

        // This is listening for input ENTERED in Console and sending the message to output
        consoleInput.setOnAction((event) -> {

            String command = consoleInput.getText();

            // need to check if valid command against enum
            for(KnownCommands knownCommand : KnownCommands.values()){
                if(command.equals(knownCommand.getCommandString())){
                    // command is known and should be executed - no twin/double commands?
                    knownCommand.execute(command, consoleOutput);
                    break;
                }
            }

            // maybe extract logic for sending text to another method?
            consoleOutput.appendText(">> " + command + "\n");
            consoleInput.clear();
        });

        // This is listening for ANY input into the ConsoleInput and giving suggestions
        consoleInput.textProperty().addListener((observable, oldText, newText) -> {

            String suggestion = "";
            if(!newText.isEmpty()){
                for (KnownCommands knownCommand : KnownCommands.values()) {
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
