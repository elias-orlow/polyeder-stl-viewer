package org.alegroup.polyederstlviewer.control.commands;

import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

public class ClearCommand implements CommandExecuter{
    @Override
    public void execute(ConsoleObject console, String[] args) {
        console.clearConsole();
    }
}
