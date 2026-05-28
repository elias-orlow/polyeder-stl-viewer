package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

public class ColorCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        if(args.length == 1){

            System.out.println(args[0]);

            switch (args[0]){
                case "red": console.getOutputArea().setStyle("-fx-control-inner-background: black; -fx-text-fill: red; -fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; -fx-highlight-text-fill: white; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 14px; "); break;
                case "blue": console.getOutputArea().setStyle("-fx-control-inner-background: black; -fx-text-fill: blue; -fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; -fx-highlight-text-fill: white; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 14px; "); break;
                case "green": console.getOutputArea().setStyle("-fx-control-inner-background: black; -fx-text-fill: green; -fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; -fx-highlight-text-fill: white; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 14px; "); break;
                case "white": console.getOutputArea().setStyle("-fx-control-inner-background: black; -fx-text-fill: white; -fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; -fx-highlight-text-fill: white; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 14px; "); break;
                case "purple": console.getOutputArea().setStyle("-fx-control-inner-background: black; -fx-text-fill: purple; -fx-font-family: 'Consolas'; -fx-highlight-fill: #444444; -fx-highlight-text-fill: white; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 14px; "); break;
                default: console.makeOutput("Invalid color command!"); return false;
            }

            return true;

        }else{
            console.makeOutput("Invalid color command!");
            return false;
        }
    }
}
