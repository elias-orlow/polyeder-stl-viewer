package org.alegroup.polyederstlviewer.model.geometry;

public class CommandBlueprint {

    private String command;
    private String methodName;
    private String executableFromContext;
    private String nextContext;

    public CommandBlueprint(String command, String methodName, String executableFromContext, String nextContext){
        this.command = command;
        this.methodName = methodName;
        this.executableFromContext = executableFromContext;
        this.nextContext = nextContext;
    }

    @Override
    public boolean equals(Object obj){
        if(this.getClass().equals(obj.getClass())){
            if(     ((CommandBlueprint) obj).command.equals(this.command) &&
                    ((CommandBlueprint) obj).methodName.equals(this.methodName) &&
                    ((CommandBlueprint) obj).executableFromContext.equals(this.executableFromContext) &&
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

    public String getExecutableFromContext() {
        return executableFromContext;
    }

    public String getNextContext() {
        return nextContext;
    }
}
