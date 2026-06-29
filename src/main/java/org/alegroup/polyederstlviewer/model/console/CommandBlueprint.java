package org.alegroup.polyederstlviewer.model.console;

/**
 * Represents a console command blueprint, defining the command keyword,
 * the method to invoke, the required context for execution, and the
 * next context to switch to after execution.
 *
 * @precondition All constructor parameters must be non-null.
 * @postcondition A fully initialized CommandBlueprint instance is created.
 */
public class CommandBlueprint
{

    /**
     * The command keyword typed by the user.
     */
    private final String command;

    /**
     * The method name to invoke when this command is executed.
     */
    private final String methodName;

    /**
     * The context required for this command to be valid.
     */
    private final String neededContext;

    /**
     * The context to switch to after executing this command.
     */
    private final String nextContext;

    /**
     * Creates a new CommandBlueprint.
     *
     * @param command       the command keyword
     * @param methodName    the method name to invoke
     * @param neededContext the required context for execution
     * @param nextContext   the context to switch to after execution
     * @precondition command != null AND methodName != null AND neededContext != null AND nextContext != null
     * @postcondition A new CommandBlueprint instance is created
     */
    public CommandBlueprint (String command, String methodName, String neededContext, String nextContext)
    {
        this.command = command;
        this.methodName = methodName;
        this.neededContext = neededContext;
        this.nextContext = nextContext;
    }

    /**
     * Determines whether this blueprint is equal to another object.
     * Two blueprints are equal if all fields match exactly.
     *
     * @param obj the object to compare
     * @return true if equal, otherwise false
     * @precondition obj may be null
     * @postcondition A boolean indicating equality is returned
     */
    @Override
    public boolean equals (Object obj)
    {

        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof CommandBlueprint other))
        {
            return false;
        }

        return this.command.equals(other.command)
                && this.methodName.equals(other.methodName)
                && this.neededContext.equals(other.neededContext)
                && this.nextContext.equals(other.nextContext);
    }

    /**
     * Returns the command keyword.
     */
    public String getCommand ()
    {
        return command;
    }

    /**
     * Returns the method name to invoke.
     */
    public String getMethodName ()
    {
        return methodName;
    }

    /**
     * Returns the required context for execution.
     */
    public String getNeededContext ()
    {
        return neededContext;
    }

    /**
     * Returns the context to switch to after execution.
     */
    public String getNextContext ()
    {
        return nextContext;
    }
}