package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.model.commands.ConsoleObject;

public class ServerReturnCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        String context = ConsoleBufferContext.MAIN.getContext();
        console.loadContext(context);

        return true;
    }
}
