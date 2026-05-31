package org.alegroup.polyederstlviewer.model.commands;


import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;

import java.util.HashMap;

// supposed to write text and read text from and to the console
public class ConsoleObject {

    private final TextArea consoleOutput;
    private final TextField consoleInput;

    private HashMap<String, String> contextBasedBuffer;
    private String currentContext;

    // get random generated id to get console
    public ConsoleObject(TextArea consoleOutput, TextField consoleInput){

        this.consoleOutput = consoleOutput;
        this.consoleInput = consoleInput;

        // default to main
        this.currentContext = ConsoleBufferContext.MAIN.getContext();
        this.contextBasedBuffer = new HashMap<String, String>();
    }

    // a non-client sided output to the console. e.g. could be an error etc
    // returns the current context if wanted, also loads the current context
    // Since commands can only be executed from their respective contexts within ConsoleWindowController we expect to have this has the currentContext
    public void makeOutputToCurrentContext(String outputText){

        String append = ">> " + outputText + "\n";

        // retrieve old value, empty if null
        String old = (this.contextBasedBuffer.get(this.currentContext) == null) ? "" : this.contextBasedBuffer.get(this.currentContext);
        this.contextBasedBuffer.put(this.currentContext, old + append);

        loadContext(this.currentContext);

    }

    // does not load the context but lets you still write to the selected context
    public void makeOutputToSpecifiedContext(String outputText, String toContext){
        String append = ">> " + outputText + "\n";

        // retrieve old value, empty if null
        String old = (this.contextBasedBuffer.get(toContext) == null) ? "" : this.contextBasedBuffer.get(toContext);
        this.contextBasedBuffer.put(toContext, old + append);
    }

    // This would be the text the user input into the console, always uses the current context and loads it
    public void writeUserInputToConsole(String userInput){

        String append = "<< " + userInput + "\n";

        // retrieve old value, empty if null
        String old = (this.contextBasedBuffer.get(this.currentContext) == null) ? "" : this.contextBasedBuffer.get(this.currentContext);
        this.contextBasedBuffer.put(this.currentContext, old + append);

        loadContext(this.currentContext);
    }

    /**
     * Uses the current context and reloads it
     */
    public void clearConsole(){

        this.contextBasedBuffer.put(this.currentContext, "");
        loadContext(this.currentContext);
    }

    public String getUserInput(boolean flush){

        String userInput = consoleInput.getText();

        if(flush){
            this.consoleInput.clear();
        }

        return userInput;
    }

    /**
     * load the specified context to the console Output Field
     * @param context
     */
    public void loadContext(String context){

        String loadedText = (this.contextBasedBuffer.get(context) == null) ? "" : this.contextBasedBuffer.get(context);
        this.consoleOutput.setText(loadedText);
        this.currentContext = context;
    }

    /* getter */
    public TextArea getOutputArea(){
        return this.consoleOutput;
    }
    public TextField getInputField(){
        return this.consoleInput;
    }
}
