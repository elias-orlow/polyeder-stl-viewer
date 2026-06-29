package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Base interface for all command executables.
 * Each command must implement the execute method.
 *
 * @precondition console != null AND args != null
 * @postcondition Command execution result is returned
 */
public interface CommandExecuter
{

    /**
     * Executes a command using the given console and arguments.
     *
     * @param console the console object used for output and context handling
     * @param args    the command arguments
     * @return true if executed successfully, false otherwise
     */
    boolean execute (ConsoleObject console, String[] args);
}