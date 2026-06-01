package org.alegroup.polyederstlviewer.model.server;

import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;

import java.util.HashMap;

public class ActiveServerContainer {

    private static ActiveServerContainer INSTANCE;

    private HashMap<String, STLServer> activeServers;

    private ActiveServerContainer(){
        this.activeServers = new HashMap<String, STLServer>();
    }

    public static ActiveServerContainer getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new ActiveServerContainer();
        }

        return INSTANCE;
    }

    /**
     *
     * @param context
     * @return null if no server found for this context
     */
    public STLServer getServer(String context){

        STLServer server;
        if((server = this.activeServers.get(context)) != null){
            return server;
        }else{
            return null;
        }
    }

    public boolean addServer(STLServer server, String context){

        if(this.activeServers.get(context) == null){
            this.activeServers.put(context, server);
            return true;
        }else{
            return false;
        }
    }

    public boolean removeServer(String context){

        if(this.activeServers.get(context) == null){
            return false;
        }else{
            this.activeServers.remove(context);
            return true;
        }
    }
}
