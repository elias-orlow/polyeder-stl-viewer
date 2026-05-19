package org.alegroup.polyederstlviewer.Control.Commands;

import javafx.scene.control.TextArea;

public class ClearCommand implements ConsoleCommand{

    @Override
    public void execute(String consoleInput, TextArea consoleOutput) {

        consoleOutput.setText(">> \n");
    }
}
