package org.alegroup.polyederstlviewer.control.commands;

import javafx.scene.control.TextArea;

public class ClearCommand implements ConsoleCommand{

    @Override
    public <D> void execute(String consoleInput, TextArea consoleOutput, D data) {

        consoleOutput.setText("");
    }
}
