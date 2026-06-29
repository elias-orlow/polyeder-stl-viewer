package org.alegroup.polyederstlviewer.control.commands;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.constants.AllCommands;
import org.alegroup.polyederstlviewer.constants.CommandHandlerConstants;
import org.alegroup.polyederstlviewer.model.console.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.console.CommandFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles command validation, parsing, and lookup operations.
 *
 * @precondition JSON command file must be readable if present.
 * @postcondition Commands can be validated and parsed.
 */
public class CommandHandler
{

    /**
     * Path to the JSON command file.
     */
    private final String source = CommandHandlerConstants.COMMAND_FILE_PATH;

    /**
     * Validates whether a command exists for the given context.
     *
     * @param command the command keyword
     * @param context the required context
     * @return the matching CommandBlueprint or null if invalid
     * @precondition command != null AND context != null
     * @postcondition Returns matching command or null
     */
    public CommandBlueprint validateCommand (String command, String context)
    {

        Gson gson = new Gson();
        File file = new File(source);
        CommandFile data;

        try
        {
            FileReader reader = new FileReader(file);
            data = gson.fromJson(reader, CommandFile.class);
            reader.close();

            if (data == null)
            {
                data = new CommandFile();
            }

            ArrayList<CommandBlueprint> possibleCommands =
                    getSameContextCommands(context);

            for (CommandBlueprint knownCommand : possibleCommands)
            {
                if (knownCommand.getCommand().equals(command))
                {
                    return knownCommand;
                }
            }

        } catch (IOException e)
        {
            System.out.println(
                    CommandHandlerConstants.JSON_LOAD_ERROR_PREFIX + e.toString()
            );
        }

        return null;
    }

    /**
     * Splits raw console input into command and arguments.
     *
     * @param rawInput the raw input string
     * @return array where index 0 is the command and remaining entries are arguments
     * @precondition rawInput != null
     * @postcondition Returns parsed input or null if empty
     */
    public String[] getCommandFromRaw (String rawInput)
    {

        String[] input = rawInput.split(CommandHandlerConstants.SPLIT_PATTERN);

        if (input.length == CommandHandlerConstants.FIRST_INDEX)
        {
            return null;
        }

        for (int i = 0; i < input.length; i++)
        {
            String cleaned = input[i].strip();
            cleaned = cleaned.replaceAll(CommandHandlerConstants.STRIP_PATTERN, "");
            input[i] = cleaned;
        }

        return input;
    }

    /**
     * Returns the executable enum for a given method name.
     *
     * @param methodName the method name
     * @return matching enum or null
     * @precondition methodName != null
     * @postcondition Returns enum or null
     */
    public AllCommands getExecutable (String methodName)
    {

        for (AllCommands command : AllCommands.values())
        {
            if (methodName.equals(command.getMethodName()))
            {
                return command;
            }
        }

        return null;
    }

    /**
     * Returns all commands that belong to the given context.
     *
     * @param context the context to filter by
     * @return list of matching commands
     * @precondition context != null
     * @postcondition Returns list of commands for the context
     */
    public ArrayList<CommandBlueprint> getSameContextCommands (String context)
    {

        Gson gson = new Gson();
        File file = new File(source);
        CommandFile data;
        ArrayList<CommandBlueprint> commandsOfSameContext = new ArrayList<>();

        try
        {
            FileReader reader = new FileReader(file);
            data = gson.fromJson(reader, CommandFile.class);
            reader.close();

            if (data == null)
            {
                return commandsOfSameContext;
            }

            for (CommandBlueprint knownCommand : data.getCommands())
            {
                if (knownCommand.getNeededContext().equals(context))
                {
                    commandsOfSameContext.add(knownCommand);
                }
            }

            return commandsOfSameContext;

        } catch (IOException e)
        {
            System.out.println(
                    CommandHandlerConstants.JSON_LOAD_ERROR_PREFIX + e.toString()
            );
        }

        return commandsOfSameContext;
    }
}