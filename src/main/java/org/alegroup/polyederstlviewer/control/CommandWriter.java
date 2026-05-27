package org.alegroup.polyederstlviewer.control;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.model.geometry.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.geometry.CommandFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommandWriter {

    final String source = "src/main/java/org/alegroup/polyederstlviewer/constants/commands.json";

    public void writeCommand(CommandBlueprint command){


        Gson gson = new Gson();
        File file = new File(source);
        CommandFile data;

        try {
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                data = gson.fromJson(reader, CommandFile.class);
                reader.close();

                if(data == null){
                    data = new CommandFile();
                }

                // search for duplicates and remove


                if (data.commands == null) {
                    data.commands = new ArrayList<>();
                }

            } else {
                data = new CommandFile();
            }


            // check duplicates
            boolean foundDuplicate = false;
            for(CommandBlueprint existingCommand : data.commands){
                if(existingCommand.equals(command)){
                    foundDuplicate = true;
                    break;
                }
            }

            // only add if not a duplicate
            if(!foundDuplicate){
                data.commands.add(command);
            }

            FileWriter writer = new FileWriter(file);
            gson.toJson(data, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("Something went wrong trying to create the file writer! " + e.toString());
        }

    }
}
