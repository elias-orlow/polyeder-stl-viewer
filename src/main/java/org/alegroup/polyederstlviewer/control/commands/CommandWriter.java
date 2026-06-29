package org.alegroup.polyederstlviewer.control.commands;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.constants.CommandWriterConstants;
import org.alegroup.polyederstlviewer.model.console.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.console.CommandFile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes command blueprints to a JSON file, ensuring no duplicates.
 *
 * @precondition CommandBlueprint must be non-null.
 * @postcondition Command is written to JSON file unless already present.
 */
public class CommandWriter
{

    /**
     * Path to the JSON command file.
     */
    private final String source = CommandWriterConstants.COMMAND_FILE_PATH;

    /**
     * Writes a command blueprint to the JSON file.
     *
     * @param command the command blueprint to write
     * @precondition command != null
     * @postcondition Command is appended unless duplicate
     */
    public void writeCommand (CommandBlueprint command)
    {

        Gson gson = new Gson();
        File file = new File(source);
        CommandFile data;

        try
        {
            if (file.exists())
            {

                FileReader reader = new FileReader(file);
                data = gson.fromJson(reader, CommandFile.class);
                reader.close();

                if (data == null || data.getCommands() == null)
                {
                    data = new CommandFile();
                }

            }
            else
            {
                data = new CommandFile();
            }

            boolean duplicateFound = false;
            for (CommandBlueprint existing : data.getCommands())
            {
                if (existing.equals(command))
                {
                    duplicateFound = true;
                    break;
                }
            }

            if (!duplicateFound)
            {
                data.getCommands().add(command);
            }

            FileWriter writer = new FileWriter(file);
            gson.toJson(data, writer);
            writer.close();

        } catch (IOException e)
        {
            System.out.println(
                    CommandWriterConstants.ERROR_PREFIX + e.toString()
            );
        }
    }
}