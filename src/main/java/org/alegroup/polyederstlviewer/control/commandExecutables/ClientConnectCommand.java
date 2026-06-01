package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;
import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

import java.util.HashMap;

public class ClientConnectCommand implements CommandExecuter{

    private HashMap<String, Thread> threads;

    public ClientConnectCommand(){
        this.threads = new HashMap<String, Thread>();
    }

    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        if(args.length != 2){
            console.makeOutputToCurrentContext("Invalid arguments provided! 'client connect --HOST_NAME --PORT_NUMBER'");
            return false;
        }else{

            int portNumber;
            String hostname;

            try {
                portNumber = Integer.parseInt(args[1]);
                hostname = args[0];
            } catch (NumberFormatException e) {
                console.makeOutputToCurrentContext("Invalid arguments provided! 'client connect --HOST_NAME --PORT_NUMBER'");
                return false;
            }

            // host and port now at least of correct type
            // context is just base client context plus hostname and port
            String context = ConsoleBufferContext.CLIENT.context() + "-" + args[0] + "-" + args[1];
            console.loadContext(context);

            if(ActiveClientContainer.getInstance().getClient(context) == null){
                STLClient stlClient = new STLClient(hostname, portNumber, console, context);
                Thread clientThread = new Thread(stlClient);
                clientThread.start();

                ActiveClientContainer.getInstance().addClient(stlClient, context);

            }else{
                // exists, load only context?
            }

            return true;
        }
    }
}
