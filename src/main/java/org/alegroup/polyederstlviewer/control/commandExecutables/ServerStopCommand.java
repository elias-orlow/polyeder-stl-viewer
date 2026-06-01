package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;
import org.alegroup.polyederstlviewer.model.server.ActiveServerContainer;
import org.alegroup.polyederstlviewer.model.server.STLServer;

public class ServerStopCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {


        String context = console.getCurrentContext();
        STLServer stlServer = ActiveServerContainer.getInstance().getServer(context);

        if(stlServer != null){
            stlServer.stop();
            console.clearConsole();
            ActiveServerContainer.getInstance().removeServer(context);
            console.loadContext(ConsoleBufferContext.MAIN.context());
            return true;
        }else{
            return false;
        }
    }
}
