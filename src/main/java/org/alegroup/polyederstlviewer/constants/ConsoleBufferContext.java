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

    MAIN(CommandConstants.CONTEXT_MAIN),
    SERVER(CommandConstants.CONTEXT_SERVER),
    CLIENT(CommandConstants.CONTEXT_CLIENT);

    /**
     * The textual identifier associated with this console buffer context.
     */
    private final String context;

    /**
     * Creates a new console buffer context with the given textual identifier.
     *
     * @param context the string identifier for this context
     * @precondition context != null
     * @postcondition The enum instance stores the provided context string
     */
    ConsoleBufferContext (String context)
    {
        this.context = context;
    }

    /**
     * Returns the textual identifier of this console buffer context.
     *
     * @return the context string
     * @precondition none
     * @postcondition A non-null string representing the context is returned
     */
    public String context ()
    {
        return this.context;
    }
}