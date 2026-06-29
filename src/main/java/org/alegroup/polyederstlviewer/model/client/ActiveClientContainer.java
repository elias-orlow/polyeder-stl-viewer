package org.alegroup.polyederstlviewer.model.client;

import java.util.HashMap;

/**
 * Singleton container that manages active STLClient instances.
 * Each client is associated with a specific console context.
 *
 * @precondition Context keys must be unique and non-null.
 * @postcondition ActiveClientContainer provides global access to active clients.
 */
public class ActiveClientContainer
{

    /**
     * Singleton instance.
     */
    private static ActiveClientContainer INSTANCE;

    /**
     * Map of context identifiers to active STLClient instances.
     */
    private final HashMap<String, STLClient> activeClients;

    /**
     * Private constructor for singleton initialization.
     *
     * @precondition none
     * @postcondition Internal client map is initialized
     */
    private ActiveClientContainer ()
    {
        this.activeClients = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the ActiveClientContainer.
     *
     * @return the global ActiveClientContainer instance
     * @precondition none
     * @postcondition A non-null singleton instance is returned
     */
    public static ActiveClientContainer getInstance ()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ActiveClientContainer();
        }
        return INSTANCE;
    }

    /**
     * Retrieves the client associated with the given context.
     *
     * @param context the context identifier
     * @return the STLClient associated with the context, or null if none exists
     * @precondition context != null
     * @postcondition Returns client or null without modifying internal state
     */
    public STLClient getClient (String context)
    {
        return activeClients.getOrDefault(context, null);
    }

    /**
     * Adds a client to the container under the given context.
     *
     * @param client  the client to add
     * @param context the context identifier
     * @return true if the client was added, false if a client already exists for the context
     * @precondition client != null AND context != null
     * @postcondition Client is added only if context was unused
     */
    public boolean addClient (STLClient client, String context)
    {
        if (activeClients.get(context) == null)
        {
            activeClients.put(context, client);
            return true;
        }
        return false;
    }

    /**
     * Removes the client associated with the given context.
     *
     * @param context the context identifier
     * @return true if a client was removed, false if no client existed for the context
     * @precondition context != null
     * @postcondition Client is removed if present
     */
    public boolean removeClient (String context)
    {
        if (activeClients.get(context) == null)
        {
            return false;
        }
        activeClients.remove(context);
        return true;
    }
}