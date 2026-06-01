package org.alegroup.polyederstlviewer.model.client;

import java.util.HashMap;

/* SINGLETON */
public class ActiveClientContainer {

    private static ActiveClientContainer INSTANCE;

    private HashMap<String, STLClient> activeClients;

    private ActiveClientContainer(){
        this.activeClients = new HashMap<String, STLClient>();
    }

    public static ActiveClientContainer getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new ActiveClientContainer();
        }

        return INSTANCE;
    }

    /**
     *
     * @param context
     * @return null if no client found for this context
     */
    public STLClient getClient(String context){

        STLClient client;
        if((client = this.activeClients.get(context)) != null){
            return client;
        }else{
            return null;
        }
    }

    public boolean addClient(STLClient client, String context){

        if(this.activeClients.get(context) == null){
            this.activeClients.put(context, client);
            return true;
        }else{
            return false;
        }
    }

    public boolean removeClient(String context){

        if(this.activeClients.get(context) == null){
            return false;
        }else{
            this.activeClients.remove(context);
            return true;
        }
    }
}
