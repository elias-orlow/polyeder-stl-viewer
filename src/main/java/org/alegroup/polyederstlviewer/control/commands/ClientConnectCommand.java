package org.alegroup.polyederstlviewer.control.commands;

import javafx.scene.control.TextArea;

public class ClientConnectCommand implements ConsoleCommand{
    @Override
    public <D> void execute(String consoleInput, TextArea consoleOutput, D data) {
        consoleOutput.appendText(">> Trying to connect... \n");

        //consoleOutput.textProperty().addListener();
    }
}
