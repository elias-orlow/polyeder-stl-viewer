package org.alegroup.polyederstlviewer.model.server;

import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;

import java.util.HashMap;

public class ActiveServerContainer {

    private static ActiveServerContainer INSTANCE;

    private HashMap<Integer, STLServer> activeServers;

    private ActiveServerContainer(){
        this.activeServers = new HashMap<Integer, STLServer>();
    }

    public static ActiveServerContainer getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new ActiveServerContainer();
        }

        return INSTANCE;
    }

    /**
     *
     * @param portNumber
     * @return null if no server found for this port
     */
    public STLServer getServer(int portNumber){

        STLServer server;
        if((server = this.activeServers.get(portNumber)) != null){
            return server;
        }else{
            return null;
        }
    }

    public boolean addServer(STLServer server, int portNumber){

        if(this.activeServers.get(portNumber) == null){
            this.activeServers.put(portNumber, server);
            return true;
        }else{
            return false;
        }
    }

    public boolean removeServer(int portNumber){

        if(this.activeServers.get(portNumber) == null){
            return false;
        }else{
            this.activeServers.remove(portNumber);
            return true;
        }
    }
}
