package org.alegroup.polyederstlviewer.control.commandExecutables;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.RotateObjectJSON;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Sends a rotation command (X,Y,Z) to the active STL client.
 *
 * @precondition console != null AND args != null
 * @postcondition Rotation data is sent to the client if valid
 */
public class ClientRotateObjectCommand implements CommandExecuter
{

    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        if (args.length != 3)
        {
            console.makeOutputToCurrentContext(CommandConstants.CLIENT_ROTATE_INVALID_ARGUMENTS);
            return false;
        }

        try
        {
            RotateObjectJSON rotateObject =
                    new RotateObjectJSON(
                            Float.parseFloat(args[0]),
                            Float.parseFloat(args[1]),
                            Float.parseFloat(args[2])
                    );

            String context = console.getCurrentContext();
            STLClient client = ActiveClientContainer.getInstance().getClient(context);

            Gson gson = new Gson();
            String data = CommandConstants.CLIENT_ROTATE_PREFIX + gson.toJson(rotateObject);

            client.inputCommand(data);

            console.makeOutputToCurrentContext(CommandConstants.CLIENT_ROTATE_SENT);
            return true;

        } catch (Exception e)
        {
            console.makeOutputToCurrentContext(CommandConstants.CLIENT_ROTATE_FLOAT_ERROR);
            System.out.println(e.toString());
            System.out.println(e.getStackTrace().toString());
            return false;
        }
    }
}