package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.control.commands.CommandWriter;
import org.alegroup.polyederstlviewer.model.console.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

/**
 * Creates a new command blueprint and writes it to the command JSON file.
 *
 * @precondition console != null AND args != null
 * @postcondition Command is written if arguments are valid
 */
public class NewCommandCommand implements CommandExecuter
{

    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        if (args.length != GeneralConstants.INT_FOUR)
        {
            console.makeOutputToCurrentContext(CommandConstants.NEWCOMMAND_INVALID_ARGUMENTS);
            return false;
        }

        CommandBlueprint newCommand =
                new CommandBlueprint(args[GeneralConstants.FIRST_INDEX], args[GeneralConstants.SECOND_INDEX],
                        args[GeneralConstants.THIRD_INDEX], args[GeneralConstants.FOURTH_INDEX]);

        CommandWriter commandWriter = new CommandWriter();
        commandWriter.writeCommand(newCommand);

        return true;
    }
}