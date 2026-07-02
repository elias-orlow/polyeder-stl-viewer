package org.alegroup.polyederstlviewer.control.commandExecutables;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.client.TranslateObjectJSON;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Sends a translation command (X,Y,Z) to the active STL client.
 *
 * @precondition console != null AND args != null
 * @postcondition Translation data is sent to the client if valid
 */
public class ClientTranslateObjectCommand implements CommandExecuter
{

    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        if (args.length != GeneralConstants.INT_THREE)
        {
            console.makeOutputToCurrentContext(CommandConstants.CLIENT_TRANSLATE_INVALID_ARGUMENTS);
            return false;
        }

        try
        {
            TranslateObjectJSON translateObject =
                    new TranslateObjectJSON(
                            Float.parseFloat(args[GeneralConstants.FIRST_INDEX]),
                            Float.parseFloat(args[GeneralConstants.SECOND_INDEX]),
                            Float.parseFloat(args[GeneralConstants.THIRD_INDEX])
                    );

            String context = console.getCurrentContext();
            STLClient client = ActiveClientContainer.getInstance().getClient(context);

            Gson gson = new Gson();
            String data = CommandConstants.CLIENT_TRANSLATE_PREFIX + gson.toJson(translateObject);

            client.inputCommand(data);

            console.makeOutputToCurrentContext(CommandConstants.CLIENT_TRANSLATE_SENT);
            return true;

        } catch (Exception e)
        {
            console.makeOutputToCurrentContext(CommandConstants.CLIENT_TRANSLATE_FLOAT_ERROR);
            System.out.println(e.toString());
            System.out.println(e.getStackTrace().toString());
            return false;
        }
    }
}