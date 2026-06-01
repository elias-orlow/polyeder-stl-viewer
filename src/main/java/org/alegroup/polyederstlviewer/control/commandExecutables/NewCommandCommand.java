package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.control.commands.CommandWriter;
import org.alegroup.polyederstlviewer.model.console.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

public class NewCommandCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        if(args.length != 4){
            console.makeOutputToCurrentContext("Invalid arguments given as new command. 'new command --COMMAND --METHOD_NAME --NEEDED_CONTEXT --NEXT_CONTEXT'");
            return false;
        }else{
            CommandBlueprint newCommand = new CommandBlueprint(args[0], args[1], args[2], args[3]);
            CommandWriter commandWriter = new CommandWriter();
            commandWriter.writeCommand(newCommand);
            return true;
        }
    }
}
