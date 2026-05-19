package org.alegroup.polyederstlviewer.Constants;

import javafx.scene.control.TextArea;
import org.alegroup.polyederstlviewer.Control.Commands.ClearCommand;
import org.alegroup.polyederstlviewer.Control.Commands.ConsoleCommand;

public interface ConsoleWindowConstants {

    enum KnownCommands{

        CLEAR("clear", new ClearCommand());

        private final String commandString;
        private final ConsoleCommand command;

        KnownCommands(String commandString, ConsoleCommand command){
            this.commandString = commandString;
            this.command = command;
        }

        public String getCommandString(){
            return this.commandString;
        }

        ///  String consoleInput, TextArea consoleOutput   outsource to "Console" class? Containing input and way to write to TextArea? Plus maybe some basic functions
        public void execute(String consoleInput, TextArea consoleOutput){
            this.command.execute(consoleInput, consoleOutput);
        }
    }
}
