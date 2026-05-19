package org.alegroup.polyederstlviewer.control.commands;

import javafx.scene.control.TextArea;
import java.awt.*;

public class ColorCommand implements ConsoleCommand{

    // We have to expect data to be of a certain type. Maybe make this an array later to provide infinite data
    // Though this data type would be specific to the command and maybe we need error handling
    @Override
    public <D> void execute(String consoleInput, TextArea consoleOutput, D data) {

        if(data.getClass().equals(Color.class)){

            Color color = (Color) data;

            // make hex from Color
            String hex = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());

            String style = " -fx-control-inner-background: black; -fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 14px;";

            consoleOutput.setStyle(style + "-fx-text-fill: " + hex + ";");
        }else{
            // some exception handling bs
        }

    }
}
