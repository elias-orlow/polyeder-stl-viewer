package org.alegroup.polyederstlviewer.Constants;

import javafx.scene.control.TextArea;
import org.alegroup.polyederstlviewer.Control.Commands.ColorCommand;
import org.alegroup.polyederstlviewer.Control.Commands.ConsoleCommand;

import java.awt.*;

public enum ColorSubCommands implements ConsoleCommandEnum {

    RED("red", new ColorCommand(), null);

    private final String commandString;
    private final ConsoleCommand command;
    private final ConsoleCommandEnum[] subCommands;

    /**
     *
     * @param commandString the String the user has to type in the console
     * @param command the command to execute, can be null
     * @param subCommands The commands that can be appended to this command, can be null
     */
    ColorSubCommands(String commandString, ConsoleCommand command, ConsoleCommandEnum[] subCommands){
        this.commandString = commandString;
        this.command = command;
        this.subCommands = subCommands;
    }

    public String getCommandString(){
        return this.commandString;
    }

    ///  String consoleInput, TextArea consoleOutput   outsource to "Console" class? Containing input and way to write to TextArea? Plus maybe some basic functions
    public void execute(String consoleInput, TextArea consoleOutput){
        // Platzhalter
        this.command.execute(consoleInput, consoleOutput, Color.red);
    }

    // get the subcommands, will return empty array if null
    public ConsoleCommandEnum[] getSubCommands(){
        if(this.subCommands != null){
            return this.subCommands;
        }else{
            return new ConsoleCommandEnum[0];
        }
    }
}
