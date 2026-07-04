package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Stops the active STL client associated with the current console context.
 *
 * @precondition console != null
 * @postcondition Client is stopped and context is reset to MAIN if client exists
 */
public class ClientStopCommand implements CommandExecuter
{
    /**
     * Stops the active STL client associated with the current console context.
     *
     * @param console the console used to get and reset the current context
     * @param args    the command arguments
     * @return {@code true} if the client is stopped, otherwise {@code false}
     * @precondition console != null AND args != null
     * @postcondition Client is stopped and context is reset to MAIN if client exists
     */
    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        String context = console.getCurrentContext();
        STLClient client = ActiveClientContainer.getInstance().getClient(context);

        if (client != null)
        {
            client.stop();
            console.clearConsole();
            ActiveClientContainer.getInstance().removeClient(context);
            console.loadContext(ConsoleBufferContext.MAIN.context());
            return true;
        }

        return false;
    }
}