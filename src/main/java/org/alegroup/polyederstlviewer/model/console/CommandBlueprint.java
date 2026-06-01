package org.alegroup.polyederstlviewer.model.console;

public class CommandBlueprint {

    private String command;
    private String methodName;
    private String neededContext;
    private String nextContext;

    public CommandBlueprint(String command, String methodName, String neededContext, String nextContext){
        this.command = command;
        this.methodName = methodName;
        this.neededContext = neededContext;
        this.nextContext = nextContext;
    }

    @Override
    public boolean equals(Object obj){
        if(this.getClass().equals(obj.getClass())){
            if(     ((CommandBlueprint) obj).command.equals(this.command) &&
                    ((CommandBlueprint) obj).methodName.equals(this.methodName) &&
                    ((CommandBlueprint) obj).neededContext.equals(this.neededContext) &&
                    ((CommandBlueprint) obj).nextContext.equals(this.nextContext)){

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

    public String getNeededContext() {
        return neededContext;
    }

    public String getNextContext() {
        return nextContext;
    }
}
