package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;
import org.alegroup.polyederstlviewer.util.STLParser;

import java.io.File;
import java.util.Arrays;

/**
 * Reads an STL file from a provided path and renders it.
 *
 * @precondition console != null AND args != null
 * @postcondition STL file is parsed and rendered if valid
 */
public class ReadFileCommand implements CommandExecuter
{

    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        if (args.length == 0)
        {
            console.makeOutputToCurrentContext(CommandConstants.READFILE_INVALID_ARGUMENT);
            return false;
        }

        args[0] = args[0].replace("\"", "");
        File file = new File(args[0]);

        if (!file.isFile() || !file.exists())
        {
            console.makeOutputToCurrentContext(CommandConstants.READFILE_INVALID_PATH);
            return false;
        }

        try
        {
            STLParseResult poly = STLParser.parse(file);
            SceneModel.getInstance().renderPolyhedron(poly);

        } catch (Exception e)
        {
            console.makeOutputToCurrentContext(CommandConstants.READFILE_PARSE_ERROR);
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println(e.toString());
            return false;
        }

        console.makeOutputToCurrentContext(CommandConstants.READFILE_RENDERING);
        return true;
    }
}