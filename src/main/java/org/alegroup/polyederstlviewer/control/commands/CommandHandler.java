package org.alegroup.polyederstlviewer.control.commands;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.constants.AllCommands;
import org.alegroup.polyederstlviewer.model.console.CommandBlueprint;
import org.alegroup.polyederstlviewer.model.console.CommandFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CommandHandler {

    final String source = "src/main/java/org/alegroup/polyederstlviewer/constants/commands.json";

    // check if command exists and return commandBlueprint
    // Könnte man mit getSameContextCommands kombinieren und hier nur die Liste der sameContextCommands durchgehen
    public CommandBlueprint validateCommand(String command){

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

            // if return null -> invalid command
            for (CommandBlueprint knownCommand : data.commands){
                if (knownCommand.getCommand().equals(command)){
                    return knownCommand;
                }
            }


        } catch (IOException e) {
            System.out.println("Something went wrong trying to load the json file " + e.toString());
        }

        // if return null -> invalid command
        return null;
    }

    // return String[0] is the command, the rest are args
    public String[] getCommandFromRaw(String rawInput){

        String[] input = rawInput.split("--");

        if(input.length == 0){
            return null;
        }

        for(int i = 0; i < input.length; i++){
            String cleanedInput = input[i].strip();
            cleanedInput = cleanedInput.replaceAll("-", "");
            input[i] = cleanedInput;
        }

        return input;
    }

    public AllCommands getExecutable(String methodName){

        for(AllCommands command : AllCommands.values()){
            if(methodName.equals(command.getMethodName())){
                return command;
            }
        }

        return null;
    }

    public ArrayList<CommandBlueprint> getSameContextCommands(String context){


        // check if valid
        Gson gson = new Gson();
        File file = new File(source);
        CommandFile data;
        ArrayList<CommandBlueprint> commandsOfSameContext = new ArrayList<CommandBlueprint>();

        try {
            FileReader reader = new FileReader(file);
            data = gson.fromJson(reader, CommandFile.class);
            reader.close();

            if(data == null){
                return commandsOfSameContext;
            }

            for (CommandBlueprint knownCommand : data.commands){

                if(knownCommand.getNeededContext().equals(context)){
                    commandsOfSameContext.add(knownCommand);
                }
            }

            return commandsOfSameContext;


        } catch (IOException e) {
            System.out.println("Something went wrong trying to load the json file " + e.toString());
        }

        return commandsOfSameContext;
    }
}
