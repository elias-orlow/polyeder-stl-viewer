package org.alegroup.polyederstlviewer.model.geometry;


import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

// supposed to write text and read text from and to the console
public class ConsoleObject {

    private final TextArea consoleOutput;
    private final TextField consoleInput;

    // get random generated id to get console
    public ConsoleObject(TextArea consoleOutput, TextField consoleInput){

        this.consoleOutput = consoleOutput;
        this.consoleInput = consoleInput;
    }

    // a non-client sided output to the console. e.g. could be an error etc
    public void makeOutput(String outputText){

        this.consoleOutput.appendText(">> " + outputText + "\n");
    }

    // This would be the text the user input into the console
    public void writeUserInputToConsole(String userInput){

        this.consoleOutput.appendText("<< " + userInput + "\n");
    }

    public void clearConsole(){
        this.consoleOutput.clear();
    }

    public String getUserInput(boolean flush){

        String userInput = consoleInput.getText();

        if(flush){
            this.consoleInput.clear();
        }

        return userInput;
    }

    /* getter */
    public TextArea getOutputArea(){
        return this.consoleOutput;
    }
    public TextField getInputField(){
        return this.consoleInput;
    }
}
