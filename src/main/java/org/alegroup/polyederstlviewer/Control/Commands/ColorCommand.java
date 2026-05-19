package org.alegroup.polyederstlviewer.Control.Commands;

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
            String hex = String.format("#%02X%02X%02X",
                    (int)(color.getRed() * 255),
                    (int)(color.getGreen() * 255),
                    (int)(color.getBlue() * 255));

            consoleOutput.setStyle("-fx-text-fill: #" + hex + ";");
        }else{
            // some exception handling bs
        }

    }
}
