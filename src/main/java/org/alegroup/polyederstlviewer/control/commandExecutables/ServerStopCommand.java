package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;
import org.alegroup.polyederstlviewer.model.server.ActiveServerContainer;
import org.alegroup.polyederstlviewer.model.server.STLServer;

/**
 * Executes the server stop command for the current console context.
 *
 * @precondition console != null AND args != null
 * @postcondition Server is stopped if present, console context is reset
 */
public class ServerStopCommand implements CommandExecuter
{

    /**
     * Stops the server associated with the current console context.
     *
     * @param console the console object
     * @param args    command arguments (unused)
     * @return true if server was stopped, false otherwise
     * @precondition console != null
     * @postcondition Server is stopped and console context reset if server existed
     */
    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        String context = console.getCurrentContext();
        STLServer stlServer = ActiveServerContainer.getInstance().getServer(context);

        if (stlServer != null)
        {
            stlServer.stop();
            console.clearConsole();
            ActiveServerContainer.getInstance().removeServer(context);
            console.loadContext(ConsoleBufferContext.MAIN.context());
            return true;
        }

        return false;
    }
}