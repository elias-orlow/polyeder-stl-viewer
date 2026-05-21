package org.alegroup.polyederstlviewer.model.geometry;

public class CommandBlueprint {

    private String command;
    private String methodName;
    private String contextName;

    public CommandBlueprint(String command, String methodName, String contextName){
        this.command = command;
        this.methodName = methodName;
        this.contextName = contextName;
    }
}
