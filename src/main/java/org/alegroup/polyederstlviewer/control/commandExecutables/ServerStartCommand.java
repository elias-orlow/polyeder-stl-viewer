package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;
import org.alegroup.polyederstlviewer.model.server.ActiveServerContainer;
import org.alegroup.polyederstlviewer.model.server.STLServer;

import java.util.HashMap;

public class ServerStartCommand implements CommandExecuter{

    // global singleton static container containing all running server threads?
    private HashMap<Integer, Thread> threads;

    public ServerStartCommand(){
        this.threads = new HashMap<Integer, Thread>();
    }

    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        if(args.length != 1){
            console.makeOutputToCurrentContext("Invalid arguments. Must only provide a valid port number");
            return false;
        }else{

            int portNumber;
            try {
                portNumber = Integer.parseInt(args[0]);
                // context is simply a combination of the server context standard plus the port number
                String context = ConsoleBufferContext.SERVER.context() + "-" + args[0];
                console.loadContext(context);

                if(ActiveServerContainer.getInstance().getServer(context) == null){
                    STLServer stlServer = new STLServer(portNumber, console, context);
                    Thread serverThread = new Thread(stlServer);
                    serverThread.start();

                    ActiveServerContainer.getInstance().addServer(stlServer, context);
                }else {
                    // exists, only load context?
                    //console.makeOutputToCurrentContext("Server exists already");
                }

                return true;

            }catch (NumberFormatException e){
                console.makeOutputToCurrentContext("Invalid arguments. Must only provide a valid port number");
                return false;
            }
        }
    }
}
