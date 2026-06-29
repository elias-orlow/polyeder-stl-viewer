package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Returns the console to the MAIN context.
 *
 * @precondition console != null
 * @postcondition Console context is switched to MAIN
 */
public class ServerReturnCommand implements CommandExecuter
{

    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        console.loadContext(CommandConstants.MAIN_CONTEXT);
        return true;
    }
}