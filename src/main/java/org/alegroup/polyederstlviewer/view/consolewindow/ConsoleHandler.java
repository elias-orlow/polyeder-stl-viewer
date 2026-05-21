package org.alegroup.polyederstlviewer.view.consolewindow;

import org.alegroup.polyederstlviewer.constants.ConsoleCommandEnum;
import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

// make static method to globally get List of consoles if necessary?
public class ConsoleHandler {

    ConsoleCommandEnum[] currentCommandContext;

    // should also take console object?
    public ConsoleHandler(ConsoleCommandEnum[] currentCommandContext){
        this.currentCommandContext = currentCommandContext;
    }



    // Hier nur console übergeben
    public void executeCommand(ConsoleObject console){

        // search for deepest subcommand and execute
        ConsoleCommandEnum[] subCommands = this.currentCommandContext;
        String userCommand = console.getUserInput(false);
        boolean foundValidCommand = false;

        searchForValidCommand:
        while (true){

            // subCommands is now baseCommands = starting point
            // check if userInput starts with valid command
            boolean startValid = false;

            iterateCommands:
            for (ConsoleCommandEnum command : subCommands){

                if(userCommand.startsWith(command.getCommandString())){

                    // We found a valid command, remove String and find subcommands or execute
                    startValid = true;

                    userCommand = userCommand.substring(command.getCommandString().length());
                    userCommand = userCommand.strip();

                    if(command.getSubCommands().length > 0){
                        // contains sub commands so go deeper in loop
                        subCommands = command.getSubCommands();
                        break iterateCommands;
                    }else{
                        // no further subcommands, execute if valid
                        if(userCommand.isEmpty()){
                            command.execute(console);
                            foundValidCommand = true;

                            // check for context switch
                            if(command.getChangeContext()){
                                this.currentCommandContext = subCommands;
                            }
                        }

                        break searchForValidCommand;
                    }
                }
            }

            if(!startValid){
                break searchForValidCommand;
            }
        }

        if(!foundValidCommand){
            console.makeOutput("Invalid Command!");
        }

        // flush
        console.getUserInput(true);
    }
}
