package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

public class ServerReturnCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        String context = ConsoleBufferContext.MAIN.context();
        console.loadContext(context);

        return true;
    }
}
