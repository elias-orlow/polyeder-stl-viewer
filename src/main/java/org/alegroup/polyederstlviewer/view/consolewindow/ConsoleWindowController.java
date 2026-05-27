package org.alegroup.polyederstlviewer.view.consolewindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.control.CommandWriter;
import org.alegroup.polyederstlviewer.model.geometry.CommandBlueprint;

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

        commandWriter.writeCommand(new CommandBlueprint("clear", "clear", "main"));
        commandWriter.writeCommand(new CommandBlueprint("color", "color", "main"));
    }
}
