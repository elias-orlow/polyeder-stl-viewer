package org.alegroup.polyederstlviewer.control;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.model.geometry.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.geometry.CommandFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CommandLoader {

    final String source = "src/main/java/org/alegroup/polyederstlviewer/constants/commands.json";

    public CommandBlueprint getCommandBlueprintFromRaw(String rawInput){

        // check if valid
        Gson gson = new Gson();
        File file = new File(source);
        CommandFile data;

        try {
            FileReader reader = new FileReader(file);
            data = gson.fromJson(reader, CommandFile.class);
            reader.close();

            if(data == null){
                data = new CommandFile();
            }

            //String[] input = rawInput.split(" ");

            // if return null -> invalid command
            for (CommandBlueprint command : data.commands){
                if (command.getCommand().equals(rawInput)){
                    return command;
                }
            }


        } catch (IOException e) {
            System.out.println("Something went wrong trying to load the json file " + e.toString());
        }

        // if return null -> invalid command
        return null;
    }
}
