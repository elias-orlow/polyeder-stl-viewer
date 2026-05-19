package org.alegroup.polyederstlviewer.constants;

import javafx.scene.control.TextArea;
import org.alegroup.polyederstlviewer.control.commands.ColorCommand;
import org.alegroup.polyederstlviewer.control.commands.ConsoleCommand;

import java.awt.*;

public enum ColorSubCommands implements ConsoleCommandEnum {

    RED("red", new ColorCommand(), null, Color.RED, false),
    BLUE("blue", new ColorCommand(), null, Color.BLUE, false),
    GREEN("green", new ColorCommand(), null, Color.GREEN, false),
    WHITE("white", new ColorCommand(), null, Color.WHITE, false);

    private final String commandString;
    private final ConsoleCommand command;
    private final ConsoleCommandEnum[] subCommands;
    private final Color color;
    private final Boolean changeContext;

    /**
     *
     * @param commandString the String the user has to type in the console
     * @param command the command to execute, can be null
     * @param subCommands The commands that can be appended to this command, can be null
     */
    ColorSubCommands(String commandString, ConsoleCommand command, ConsoleCommandEnum[] subCommands, Color color, boolean changeContext){
        this.commandString = commandString;
        this.command = command;
        this.subCommands = subCommands;
        this.color = color;
        this.changeContext = changeContext;
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

    @Override
    public Boolean getChangeContext() {
        return this.changeContext;
    }
}
