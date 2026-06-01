package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

public class ClientStopCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        String context = console.getCurrentContext();
        STLClient client = ActiveClientContainer.getInstance().getClient(context);

        if(client != null){
            client.stop();
            console.clearConsole();
            ActiveClientContainer.getInstance().removeClient(context);
            console.loadContext(ConsoleBufferContext.MAIN.context());
            return true;
        }else{
            return false;
        }
    }
}
