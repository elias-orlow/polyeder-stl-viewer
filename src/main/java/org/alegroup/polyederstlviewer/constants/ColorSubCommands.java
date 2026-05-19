package org.alegroup.polyederstlviewer.constants;

import javafx.scene.control.TextArea;
import org.alegroup.polyederstlviewer.control.commands.ColorCommand;
import org.alegroup.polyederstlviewer.control.commands.ConsoleCommand;

import java.awt.*;

public enum ColorSubCommands implements ConsoleCommandEnum {

    RED("red", new ColorCommand(), null, Color.RED),
    BLUE("blue", new ColorCommand(), null, Color.BLUE),
    GREEN("green", new ColorCommand(), null, Color.GREEN),
    WHITE("white", new ColorCommand(), null, Color.WHITE);

    private final String commandString;
    private final ConsoleCommand command;
    private final ConsoleCommandEnum[] subCommands;
    private final Color color;

    /**
     *
     * @param commandString the String the user has to type in the console
     * @param command the command to execute, can be null
     * @param subCommands The commands that can be appended to this command, can be null
     */
    ColorSubCommands(String commandString, ConsoleCommand command, ConsoleCommandEnum[] subCommands, Color color){
        this.commandString = commandString;
        this.command = command;
        this.subCommands = subCommands;
        this.color = color;
    }

    public String getCommandString(){
        return this.commandString;
    }

    ///  String consoleInput, TextArea consoleOutput   outsource to "Console" class? Containing input and way to write to TextArea? Plus maybe some basic functions
    public void execute(String consoleInput, TextArea consoleOutput){
        // Platzhalter
        this.command.execute(consoleInput, consoleOutput, this.color);
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
