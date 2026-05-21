package org.alegroup.polyederstlviewer.constants;

import org.alegroup.polyederstlviewer.control.commands.ClearCommand;
import org.alegroup.polyederstlviewer.control.commands.ConsoleCommand;
import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

// Wie wäre es Command als eigene Klasse zu behandeln und Command Objekte zu erzeugen?

public enum BaseCommands implements ConsoleCommandEnum {

    //CLEAR("clear", new ClearCommand());
    COLOR("color", null, ColorSubCommands.values(), false),
    CLEAR("clear", new ClearCommand(), null, false),
    CLIENT("client", null, ClientSubCommands.values(), false);

    private final String commandString;
    private final ConsoleCommand command;
    private final ConsoleCommandEnum[] subCommands;
    private final Boolean changeContext;

    /**
     *
     * @param commandString the String the user has to type in the console
     * @param command the command to execute, can be null
     * @param subCommands The commands that can be appended to this command
     */
    BaseCommands(String commandString, ConsoleCommand command, ConsoleCommandEnum[] subCommands, boolean changeContext){
        this.commandString = commandString;
        this.command = command;
        this.subCommands = subCommands;
        this.changeContext = changeContext;
    }

    public String getCommandString(){
        return this.commandString;
    }

    ///  String consoleInput, TextArea consoleOutput   outsource to "Console" class? Containing input and way to write to TextArea? Plus maybe some basic functions
    public void execute(ConsoleObject console){
        this.command.execute(console, null);
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
