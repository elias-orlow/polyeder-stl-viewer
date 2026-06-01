package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

public interface CommandExecuter {

    // true if executed normally, false if it didnt
    public boolean execute(ConsoleObject console, String[] args);
}
