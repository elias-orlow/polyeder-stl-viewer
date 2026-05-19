package org.alegroup.polyederstlviewer.Control.Commands;

import javafx.scene.control.TextArea;

public interface ConsoleCommand {

    public <D> void execute(String consoleInput, TextArea consoleOutput, D data);
}
