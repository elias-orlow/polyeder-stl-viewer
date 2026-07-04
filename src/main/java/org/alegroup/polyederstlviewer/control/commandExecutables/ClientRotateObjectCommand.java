package org.alegroup.polyederstlviewer.control.commandExecutables;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
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
    /**
     * Sends a rotation command (X, Y, Z) to the active STL client.
     *
     * @param console the console used to get the current context and output messages
     * @param args    the command arguments containing the X, Y, and Z rotation values
     * @return {@code true} if the rotation data is sent, otherwise {@code false}
     * @precondition console != null AND args != null
     * @postcondition Rotation data is sent to the active client if arguments are valid
     */
    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        if (args.length != GeneralConstants.INT_THREE)
        {
            console.makeOutputToCurrentContext(CommandConstants.CLIENT_ROTATE_INVALID_ARGUMENTS);
            return false;
        }

        try
        {
            RotateObjectJSON rotateObject =
                    new RotateObjectJSON(
                            Float.parseFloat(args[GeneralConstants.FIRST_INDEX]),
                            Float.parseFloat(args[GeneralConstants.SECOND_INDEX]),
                            Float.parseFloat(args[GeneralConstants.THIRD_INDEX])
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