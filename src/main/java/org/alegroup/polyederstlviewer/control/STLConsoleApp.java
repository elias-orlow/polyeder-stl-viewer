package org.alegroup.polyederstlviewer.control;

import org.alegroup.polyederstlviewer.constants.ConsoleConstants;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.util.STLParser;

import java.io.File;
import java.util.List;

/**
 * Console-based entry point for parsing and analyzing an STL file.
 *
 * @precondition args.length > 0 AND args[0] refers to an existing STL file.
 * @postcondition Analysis results are printed to the console.
 */
public class STLConsoleApp
{

    /**
     * Main entry point for the console STL viewer.
     *
     * @param args command-line arguments
     * @precondition args.length > 0 AND args[0] refers to an existing file
     * @postcondition STL file is parsed and analysis results are printed
     */
    public static void main (String[] args)
    {

        if (args.length == ConsoleConstants.ARG_COUNT_ZERO)
        {
            System.err.println(ConsoleConstants.USAGE_MESSAGE);
            System.exit(ConsoleConstants.EXIT_CODE_USAGE_ERROR);
        }

        File file = new File(args[ConsoleConstants.FIRST_INDEX]);

        if (!file.exists() || !file.isFile())
        {
            System.err.println(ConsoleConstants.FILE_NOT_FOUND_PREFIX + args[ConsoleConstants.FIRST_INDEX]);
            System.exit(ConsoleConstants.EXIT_CODE_FILE_NOT_FOUND);
        }

        try
        {
            System.out.println(ConsoleConstants.PARSING_PREFIX + file.getAbsolutePath());

            STLParseResult result = STLParser.parse(file);
            Polyhedron polyhedron = result.getPolyhedron();

            System.out.println(ConsoleConstants.TRIANGLE_COUNT_PREFIX + polyhedron.triangleCount());
            System.out.printf(ConsoleConstants.SURFACE_AREA_FORMAT, result.getAreaResult().getSurfaceArea());
            System.out.printf(ConsoleConstants.VOLUME_FORMAT, polyhedron.volume());

            List<Triangle> sorted = polyhedron.trianglesSortedByAreaDesc();

            System.out.println(ConsoleConstants.TOP_TRIANGLES_HEADER);

            for (int i = ConsoleConstants.FIRST_INDEX;
                 i < Math.min(ConsoleConstants.TOP_TRIANGLES_LIMIT, sorted.size());
                 i++)
            {

                Triangle t = sorted.get(i);
                System.out.printf(ConsoleConstants.TOP_TRIANGLE_ENTRY_FORMAT,
                        i + ConsoleConstants.INDEX_OFFSET,
                        t.area());
            }

        } catch (Exception e)
        {
            System.err.println(ConsoleConstants.UNEXPECTED_ERROR_PREFIX + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(ConsoleConstants.EXIT_CODE_UNEXPECTED_ERROR);
        }
    }
}