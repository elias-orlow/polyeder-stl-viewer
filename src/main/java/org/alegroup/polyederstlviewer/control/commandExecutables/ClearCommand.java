package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Clears the current console output.
 *
 * @precondition console != null
 * @postcondition Console output is cleared
 */
public class ClearCommand implements CommandExecuter
{

    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {
        console.clearConsole();
        return true;
    }
}