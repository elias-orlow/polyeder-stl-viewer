package org.alegroup.polyederstlviewer.constants;

/**
 * Defines the available console buffer contexts used throughout the application.
 * <p>
 * Each context represents a logical console state (main, server, client)
 * and provides a string identifier that is used for routing output,
 * switching views, and associating commands with the correct subsystem.
 */
public enum ConsoleBufferContext
{

    MAIN("main"),
    SERVER("server"),
    CLIENT("client");

    private final String context;

    ConsoleBufferContext (String context)
    {
        this.context = context;
    }

    public String context ()
    {
        return this.context;
    }
}