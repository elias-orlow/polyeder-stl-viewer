package org.alegroup.polyederstlviewer.Constants;

import javafx.scene.control.TextArea;
import org.alegroup.polyederstlviewer.Control.Commands.ConsoleCommand;

import java.awt.*;

public interface ConsoleCommandEnum {

    public String getCommandString();

    ///  String consoleInput, TextArea consoleOutput   outsource to "Console" class? Containing input and way to write to TextArea? Plus maybe some basic functions
    public void execute(String consoleInput, TextArea consoleOutput);

    // get the subcommands, will return empty array if null
    public ConsoleCommandEnum[] getSubCommands();
}
