package org.alegroup.polyederstlviewer.constants;

import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

public interface ConsoleCommandEnum {

    public String getCommandString();

    ///  String consoleInput, TextArea consoleOutput   outsource to "Console" class? Containing input and way to write to TextArea? Plus maybe some basic functions
    public void execute(ConsoleObject console);

    // get the subcommands, will return empty array if null
    public ConsoleCommandEnum[] getSubCommands();

    public Boolean getChangeContext();
}
