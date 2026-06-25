package org.alegroup.polyederstlviewer.control.commandExecutables;

import javafx.stage.FileChooser;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;
import org.alegroup.polyederstlviewer.util.STLParser;
import org.alegroup.polyederstlviewer.view.mainwindow.PolyInfoController;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ReadFileCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        if(args.length == 0){
            console.makeOutputToCurrentContext("Invalid argument. Provide path to .stl file --FILE_PATH");
            return false;
        }

        args[0] = args[0].replace("\"", "");
        File file = new File(args[0]);
        if(!file.isFile() || !file.exists()){
            console.makeOutputToCurrentContext("Invalid path provided or could not find file! Provide path to .stl file --FILE_PATH");
            return false;
        }

        try {
            STLParseResult poly = STLParser.parse(file);
            //PolyInfoController.update(poly);
            SceneModel.getInstance().renderPolyhedron(poly);
        }catch (Exception e){
            console.makeOutputToCurrentContext("Could not read the provided file! Make sure it is a .stl file.");
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println(e.toString());
            return false;
        }

        console.makeOutputToCurrentContext("Rendering file...");

        return true;
    }
}
