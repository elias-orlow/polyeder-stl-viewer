package org.alegroup.polyederstlviewer.control.commands;

import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

public interface CommandExecuter {

    // true if executed normally, false if it didnt
    public boolean execute(ConsoleObject console, String[] args);
}
