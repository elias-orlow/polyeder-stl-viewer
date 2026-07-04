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
    /**
     * Clears the current console output.
     *
     * @param console the console whose output is to be cleared
     * @param args    the command arguments (ignored)
     * @return {@code true} after the console has been cleared
     * @precondition console != null AND args != null
     * @postcondition The console output is cleared and {@code true} is returned
     */
    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {
        console.clearConsole();
        return true;
    }
}