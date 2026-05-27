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

    @Override
    public boolean equals(Object obj){
        if(this.getClass().equals(obj.getClass())){
            if(     ((CommandBlueprint) obj).command.equals(this.command) &&
                    ((CommandBlueprint) obj).methodName.equals(this.methodName) &&
                    ((CommandBlueprint) obj).contextName.equals(this.contextName)){

                return true;
            }
        }

        return false;
    }

    /* getter */

    public String getCommand() {
        return command;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getContextName() {
        return contextName;
    }
}
