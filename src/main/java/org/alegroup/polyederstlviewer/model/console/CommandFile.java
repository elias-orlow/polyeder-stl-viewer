package org.alegroup.polyederstlviewer.model.console;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of command blueprints loaded from a command file.
 * Each command blueprint defines a console command and its associated metadata.
 *
 * @precondition CommandBlueprint instances added must be valid and non-null.
 * @postcondition A CommandFile instance maintains an internal list of commands.
 */
public class CommandFile
{

    /**
     * The list of command blueprints contained in this file.
     */
    private final List<CommandBlueprint> commands;

    /**
     * Creates an empty CommandFile.
     *
     * @precondition none
     * @postcondition A new CommandFile instance is created with an empty command list
     */
    public CommandFile ()
    {
        this.commands = new ArrayList<>();
    }

    /**
     * Returns the list of command blueprints.
     *
     * @return the list of commands
     * @precondition none
     * @postcondition A non-null list is returned
     */
    public List<CommandBlueprint> getCommands ()
    {
        return commands;
    }

    /**
     * Adds a command blueprint to this file.
     *
     * @param command the command blueprint to add
     * @precondition command != null
     * @postcondition Command is added to the internal list
     */
    public void addCommand (CommandBlueprint command)
    {
        commands.add(command);
    }
}