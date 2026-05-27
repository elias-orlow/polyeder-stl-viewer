package org.alegroup.polyederstlviewer.control.commands;

import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

public interface CommandExecuter {

    public void execute(ConsoleObject console, String[] args);
}
