package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Changes the console output color theme.
 *
 * @precondition console != null AND args != null
 * @postcondition Console output area style is updated if valid color is provided
 */
public class ColorCommand implements CommandExecuter
{

    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        if (args.length != GeneralConstants.INT_ONE)
        {
            console.makeOutputToCurrentContext(CommandConstants.COLOR_INVALID);
            return false;
        }

        String color = args[GeneralConstants.FIRST_INDEX];

        switch (color)
        {
            case CommandConstants.RED_COLOR_COMMAND:
                console.getOutputArea().setStyle(CommandConstants.COLOR_RED_STYLE);
                break;

            case CommandConstants.BLUE_COLOR_COMMAND:
                console.getOutputArea().setStyle(CommandConstants.COLOR_BLUE_STYLE);
                break;

            case CommandConstants.GREEN_COLOR_COMMAND:
                console.getOutputArea().setStyle(CommandConstants.COLOR_GREEN_STYLE);
                break;

            case CommandConstants.WHITE_COLOR_COMMAND:
                console.getOutputArea().setStyle(CommandConstants.COLOR_WHITE_STYLE);
                break;

            case CommandConstants.PURPLE_COLOR_COMMAND:
                console.getOutputArea().setStyle(CommandConstants.COLOR_PURPLE_STYLE);
                break;

            default:
                console.makeOutputToCurrentContext(CommandConstants.COLOR_INVALID);
                return false;
        }

        return true;
    }
}