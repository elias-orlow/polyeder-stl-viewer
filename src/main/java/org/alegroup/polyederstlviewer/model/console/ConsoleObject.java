package org.alegroup.polyederstlviewer.model.console;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.alegroup.polyederstlviewer.constants.ConsoleBufferContext;

import java.util.HashMap;

/**
 * Represents a console abstraction that manages input and output text
 * across multiple logical contexts. Each context maintains its own
 * text buffer, allowing different subsystems to operate independently
 * within the same console window.
 *
 * @precondition consoleOutput != null AND consoleInput != null
 * @postcondition A ConsoleObject instance is created with an initialized context buffer
 */
public class ConsoleObject
{

    /**
     * The text area used for console output.
     */
    private final TextArea consoleOutput;

    /**
     * The text field used for console input.
     */
    private final TextField consoleInput;

    /**
     * Stores console text per context.
     */
    private final HashMap<String, String> contextBasedBuffer;

    /**
     * The currently active console context.
     */
    private String currentContext;

    /**
     * Creates a new ConsoleObject with the given output and input controls.
     *
     * @param consoleOutput the TextArea used for console output
     * @param consoleInput  the TextField used for console input
     * @precondition consoleOutput != null AND consoleInput != null
     * @postcondition ConsoleObject is initialized with MAIN context
     */
    public ConsoleObject (TextArea consoleOutput, TextField consoleInput)
    {
        this.consoleOutput = consoleOutput;
        this.consoleInput = consoleInput;

        this.currentContext = ConsoleBufferContext.MAIN.context();
        this.contextBasedBuffer = new HashMap<>();
    }

    /**
     * Writes non-user output to the current context and reloads it.
     *
     * @param outputText the text to append
     * @precondition outputText != null
     * @postcondition Text is appended to the current context buffer and displayed
     */
    public void makeOutputToCurrentContext (String outputText)
    {

        String append = ">> " + outputText + "\n";
        String old = contextBasedBuffer.getOrDefault(currentContext, "");

        contextBasedBuffer.put(currentContext, old + append);
        loadContext(currentContext);
    }

    /**
     * Writes output to a specified context without switching to it,
     * unless the specified context is the current one.
     *
     * @param outputText the text to append
     * @param toContext  the target context
     * @precondition outputText != null AND toContext != null
     * @postcondition Text is appended to the specified context buffer
     */
    public void makeOutputToSpecifiedContext (String outputText, String toContext)
    {

        String append = ">> " + outputText + "\n";
        String old = contextBasedBuffer.getOrDefault(toContext, "");

        contextBasedBuffer.put(toContext, old + append);

        if (toContext.equals(currentContext))
        {
            loadContext(currentContext);
        }
    }

    /**
     * Writes user input to the current context and reloads it.
     *
     * @param userInput the user input text
     * @precondition userInput != null
     * @postcondition User input is appended to the current context buffer and displayed
     */
    public void writeUserInputToConsole (String userInput)
    {

        String append = "<< " + userInput + "\n";
        String old = contextBasedBuffer.getOrDefault(currentContext, "");

        contextBasedBuffer.put(currentContext, old + append);
        loadContext(currentContext);
    }

    /**
     * Clears the current context buffer and reloads it.
     *
     * @precondition none
     * @postcondition Current context buffer becomes empty
     */
    public void clearConsole ()
    {
        contextBasedBuffer.put(currentContext, "");
        loadContext(currentContext);
    }

    /**
     * Retrieves user input from the console input field,
     * but only if the caller provides the correct context.
     *
     * @param context the context requesting the input
     * @return the user input if context matches, otherwise an empty string
     * @precondition context != null
     * @postcondition Returns input only if context matches currentContext
     */
    public String getUserInput (String context)
    {

        String userInput = consoleInput.getText();

        if (currentContext.equals(context))
        {
            return userInput;
        }
        return "";
    }

    /**
     * Loads the specified context into the console output field.
     *
     * @param context the context to load
     * @precondition context != null
     * @postcondition Console output displays the buffer of the specified context
     */
    public void loadContext (String context)
    {

        String loadedText = contextBasedBuffer.getOrDefault(context, "");
        consoleOutput.setText(loadedText);
        currentContext = context;
    }

    /**
     * Returns the console output area.
     */
    public TextArea getOutputArea ()
    {
        return consoleOutput;
    }

    /**
     * Returns the console input field.
     */
    public TextField getInputField ()
    {
        return consoleInput;
    }

    /**
     * Returns the currently active context.
     */
    public String getCurrentContext ()
    {
        return currentContext;
    }
}