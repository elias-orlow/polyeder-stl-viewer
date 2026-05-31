package org.alegroup.polyederstlviewer.constants;

public enum ConsoleBufferContext {

    MAIN("main"),
    SERVER("server");

    private final String context;
    ConsoleBufferContext(String context){
        this.context = context;
    }

    public String getContext(){
        return this.context;
    }
}
