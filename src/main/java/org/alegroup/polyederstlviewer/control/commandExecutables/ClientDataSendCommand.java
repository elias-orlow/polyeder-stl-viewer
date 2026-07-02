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