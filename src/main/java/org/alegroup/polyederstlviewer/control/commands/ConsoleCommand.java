package org.alegroup.polyederstlviewer.control.commands;

import javafx.scene.control.TextArea;

public interface ConsoleCommand {

    public <D> void execute(String consoleInput, TextArea consoleOutput, D data);
}
