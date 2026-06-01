package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

public class ClientDataSendCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        if(args.length != 1){
            console.makeOutputToCurrentContext("Invalid arguments: 'data send --DATA'");
            return false;
        }else{

            String context = console.getCurrentContext();
            STLClient client = ActiveClientContainer.getInstance().getClient(context);

            client.inputCommand(args[0]);
            console.makeOutputToCurrentContext("Sent data to server!");
            return true;
        }
    }
}
