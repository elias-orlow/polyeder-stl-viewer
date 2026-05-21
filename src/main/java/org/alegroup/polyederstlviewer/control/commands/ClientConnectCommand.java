package org.alegroup.polyederstlviewer.control.commands;

import javafx.scene.control.TextArea;
import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

public class ClientConnectCommand implements ConsoleCommand{
    @Override
    public <D> void execute(ConsoleObject console, D data) {
        console.makeOutput("Trying to connect...");

        //consoleOutput.textProperty().addListener();
    }
}
