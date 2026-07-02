package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;
import org.alegroup.polyederstlviewer.model.server.ActiveServerContainer;
import org.alegroup.polyederstlviewer.model.server.STLServer;

import java.util.HashMap;

/**
 * Starts a new STL server instance on the given port.
 *
 * @precondition console != null AND args != null
 * @postcondition Server is started if port is valid and no server exists for the context
 */
public class ServerStartCommand implements CommandExecuter
{

    /**
     * Tracks running server threads.
     */
    private final HashMap<Integer, Thread> threads;

    /**
     * Creates a new ServerStartCommand instance.
     *
     * @precondition none
     * @postcondition Thread map initialized
     */
    public ServerStartCommand ()
    {
        this.threads = new HashMap<>();
    }

    /**
     * Executes the server start command.
     *
     * @param console the console object
     * @param args    command arguments (must contain exactly one port number)
     * @return true if server started successfully, false otherwise
     * @precondition console != null AND args != null
     * @postcondition Server is started or error message is printed
     */
    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        if (args.length != GeneralConstants.INT_ONE)
        {
            console.makeOutputToCurrentContext(CommandConstants.INVALID_ARGUMENTS);
            return false;
        }

        try
        {
            int portNumber = Integer.parseInt(args[GeneralConstants.FIRST_INDEX]);

            String context =
                    ConsoleBufferContext.SERVER.context()
                            + CommandConstants.CONTEXT_SEPARATOR
                            + args[GeneralConstants.FIRST_INDEX];

            console.loadContext(context);

            if (ActiveServerContainer.getInstance().getServer(context) == null)
            {

                STLServer stlServer = new STLServer(portNumber, console, context);
                Thread serverThread = new Thread(stlServer);
                serverThread.start();

                ActiveServerContainer.getInstance().addServer(stlServer, context);

            }

            return true;

        } catch (NumberFormatException e)
        {
            console.makeOutputToCurrentContext(CommandConstants.INVALID_ARGUMENTS);
            return false;
        }
    }
}