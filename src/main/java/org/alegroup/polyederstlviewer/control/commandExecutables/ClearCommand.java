package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

public class ClearCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {
        console.clearConsole();
        return true;
    }
}
