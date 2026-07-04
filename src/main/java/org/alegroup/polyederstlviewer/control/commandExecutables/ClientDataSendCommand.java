package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Sends arbitrary data to the active STL client.
 *
 * @precondition console != null AND args != null
 * @postcondition Data is sent to the client if valid
 */
public class ClientDataSendCommand implements CommandExecuter
{
    /**
     * Sends arbitrary data to the active STL client.
     *
     * @param console the console used to get the current context and output messages
     * @param args    the command arguments containing the data to send
     * @return {@code true} if the data is sent, otherwise {@code false}
     * @precondition console != null AND args != null
     * @postcondition Data is sent to the active client if arguments are valid
     */
    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        if (args.length != GeneralConstants.INT_ONE)
        {
            console.makeOutputToCurrentContext(CommandConstants.CLIENT_DATA_INVALID_ARGUMENTS);
            return false;
        }

        String context = console.getCurrentContext();
        STLClient client = ActiveClientContainer.getInstance().getClient(context);

        client.inputCommand(CommandConstants.CLIENT_DATA_PREFIX + args[GeneralConstants.FIRST_INDEX]);
        console.makeOutputToCurrentContext(CommandConstants.CLIENT_DATA_SENT);

        return true;
    }
}