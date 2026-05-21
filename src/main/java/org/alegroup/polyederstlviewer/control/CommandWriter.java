package org.alegroup.polyederstlviewer.control;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.model.geometry.CommandBlueprint;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class CommandFile {
    List<CommandBlueprint> commands = new ArrayList<>();
}

public class CommandWriter {

    final static String source = "/org/alegroup/polyederstlviewer/constants/commands.json";

    public static void writeCommand(CommandBlueprint command){

        /*
        Gson gson = new Gson();

        try{
            FileWriter writer = new FileWriter(source);
            gson.toJson(command, writer);
            writer.close();

        } catch (IOException e) {
            System.out.println("Something went wrong trying to create the file writer! " + e.toString());
        }

         */

        Gson gson = new Gson();
        File file = new File(source);

        CommandFile data;

        try {
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                data = gson.fromJson(reader, CommandFile.class);
                reader.close();

                if (data.commands == null) {
                    data.commands = new ArrayList<>();
                }

            } else {
                data = new CommandFile();
            }

            data.commands.add("newCommand");

// 3. komplett zurückschreiben (overwrite ist OK!)
            FileWriter writer = new FileWriter(file);
            gson.toJson(data, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("Something went wrong trying to create the file writer! " + e.toString());
        }

    }
}
