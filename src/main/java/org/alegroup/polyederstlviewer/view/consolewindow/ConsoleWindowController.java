package org.alegroup.polyederstlviewer.view.consolewindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.constants.AllCommands;
import org.alegroup.polyederstlviewer.control.CommandHandler;
import org.alegroup.polyederstlviewer.control.CommandWriter;
import org.alegroup.polyederstlviewer.control.commands.CommandExecuter;
import org.alegroup.polyederstlviewer.model.geometry.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

import java.util.Arrays;

public class ConsoleWindowController {

    @FXML
    public VBox console_text;
    public TextField consoleInput;
    public TextArea consoleOutput;
    public Label ghostLabel;
    public Label inputLabel;

    public void initialize(){

        // load available commands
        CommandWriter commandWriter = new CommandWriter();
        CommandHandler commandHandler = new CommandHandler();
        ConsoleObject console = new ConsoleObject(consoleOutput, consoleInput);

        commandWriter.writeCommand(new CommandBlueprint("clear", "clear", "main", "main"));
        commandWriter.writeCommand(new CommandBlueprint("color", "color", "main", "main"));

        consoleInput.setOnAction(e -> {

            String userInput = consoleInput.getText();
            consoleInput.clear();
            System.out.println("User entered something in the console: " + userInput);

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

                // now we have a valid command. Execute
                AllCommands executable = commandHandler.getExecutable(commandBlueprint.getMethodName());
                executable.execute(console, args);
            }
        });
    }
}
