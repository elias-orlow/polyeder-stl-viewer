package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Connects to an STL server using hostname and port number.
 *
 * @precondition console != null AND args != null
 * @postcondition Client is created and connected if arguments are valid
 */
public class ClientConnectCommand implements CommandExecuter
{

    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        if (args.length != 2)
        {
            console.makeOutputToCurrentContext(CommandConstants.CLIENT_CONNECT_INVALID_ARGUMENTS);
            return false;
        }

        int portNumber;
        String hostname;

        try
        {
            hostname = args[0];
            portNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException e)
        {
            console.makeOutputToCurrentContext(CommandConstants.CLIENT_CONNECT_INVALID_ARGUMENTS);
            return false;
        }

        String context =
                ConsoleBufferContext.CLIENT.context()
                        + CommandConstants.CLIENT_CONNECT_CONTEXT_SEPARATOR
                        + hostname
                        + CommandConstants.CLIENT_CONNECT_CONTEXT_SEPARATOR
                        + portNumber;

        console.loadContext(context);

        if (ActiveClientContainer.getInstance().getClient(context) == null)
        {

            STLClient stlClient = new STLClient(hostname, portNumber, console, context);
            Thread clientThread = new Thread(stlClient);
            clientThread.start();

            ActiveClientContainer.getInstance().addClient(stlClient, context);
        }

        return true;
    }
}