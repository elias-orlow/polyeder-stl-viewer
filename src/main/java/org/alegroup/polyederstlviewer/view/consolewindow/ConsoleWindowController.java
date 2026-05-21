package org.alegroup.polyederstlviewer.view.consolewindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ConsoleWindowController {

    @FXML
    public VBox console_text;
    public TextField consoleInput;
    public TextArea consoleOutput;
    public Label ghostLabel;
    public Label inputLabel;

    public void initialize(){

        // load available commands
    }
}
