package org.alegroup.polyederstlviewer.model.server;

import java.util.HashMap;

/**
 * Singleton container responsible for managing active STLServer instances.
 * Servers are stored and retrieved based on their associated console context.
 * This class ensures that only one server per context is active at any time.
 */
public class ActiveServerContainer
{

    /**
     * Singleton instance of the ActiveServerContainer.
     */
    private static ActiveServerContainer INSTANCE;

    /**
     * Map storing active servers by their context identifier.
     */
    private HashMap<String, STLServer> activeServers;

    /**
     * Private constructor to enforce the singleton pattern.
     *
     * @precondition none
     * @postcondition A new container instance is created with an empty server map
     */
    private ActiveServerContainer ()
    {
        this.activeServers = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the ActiveServerContainer.
     *
     * @return the global ActiveServerContainer instance
     * @precondition none
     * @postcondition The same instance is returned on every call
     */
    public static ActiveServerContainer getInstance ()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ActiveServerContainer();
        }
        return INSTANCE;
    }

    /**
     * Retrieves the server associated with the given context.
     *
     * @param context the context identifier used to look up the server
     * @return the server for the given context, or null if none exists
     * @precondition context != null
     * @postcondition Returns either a valid server or null
     */
    public STLServer getServer (String context)
    {
        STLServer server = this.activeServers.get(context);
        return server != null ? server : null;
    }

    /**
     * Adds a server to the container under the specified context.
     * A server is only added if no server is currently registered for that context.
     *
     * @param server  the server instance to add
     * @param context the context identifier
     * @return true if the server was added, false if a server already exists for the context
     * @precondition server != null AND context != null
     * @postcondition The server is stored if the context was previously unused
     */
    public boolean addServer (STLServer server, String context)
    {
        if (this.activeServers.get(context) == null)
        {
            this.activeServers.put(context, server);
            return true;
        }
        return false;
    }

    /**
     * Removes the server associated with the given context.
     *
     * @param context the context identifier
     * @return true if a server was removed, false if no server existed for the context
     * @precondition context != null
     * @postcondition The server is removed if present
     */
    public boolean removeServer (String context)
    {
        if (this.activeServers.get(context) == null)
        {
            return false;
        }
        this.activeServers.remove(context);
        return true;
    }
}