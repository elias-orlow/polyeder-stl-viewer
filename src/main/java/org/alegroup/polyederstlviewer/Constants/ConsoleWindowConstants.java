package org.alegroup.polyederstlviewer.Constants;

public interface ConsoleWindowConstants {

    enum KnownCommands{

        CLEAR("clear");

        private final String commandString;

        KnownCommands(String commandString){
            this.commandString = commandString;
        }

        public String getCommandString(){
            return this.commandString;
        }
    }
}
