package org.alegroup.polyederstlviewer.constants;

public enum ConsoleBufferContext {

    MAIN("main"),
    SERVER("server"),
    CLIENT("client");

    private final String context;
    ConsoleBufferContext(String context){
        this.context = context;
    }

    public String context(){
        return this.context;
    }
}
