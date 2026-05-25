package org.alegroup.polyederstlviewer.control;

import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.util.STLFormatException;
import org.alegroup.polyederstlviewer.util.STLParser;

import java.io.File;
import java.util.List;

/**
 * Console application entry point for reading an STL file and printing analysis.
 *
 * Usage:
 *   java -jar polyviewer.jar path/to/model.stl
 *
 * @precondition args[0] is path to an existing STL file.
 * @postcondition Program prints triangle count, surface area, volume and top triangles.
 */
public class STLConsoleApp
{
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.err.println("Usage: java -jar polyviewer.jar <path-to-stl-file>");
            System.exit(1);
        }

        File f = new File(args[0]);
        if (!f.exists() || !f.isFile())
        {
            System.err.println("File not found: " + args[0]);
            System.exit(2);
        }

        try
        {
            System.out.println("Parsing STL file: " + f.getAbsolutePath());
            Polyhedron poly = STLParser.parse(f);

            System.out.println("Triangles: " + poly.triangleCount());
            System.out.printf("Surface area: %.6f\n", poly.surfaceArea());
            System.out.printf("Volume: %.6f\n", poly.volume());

            List<Triangle> sorted = poly.trianglesSortedByAreaDesc();
            System.out.println("Top 10 largest triangles by area:");
            for (int i = 0; i < Math.min(10, sorted.size()); i++)
            {
                Triangle t = sorted.get(i);
                System.out.printf("%2d: area=%.6f\n", i + 1, t.area());
            }
        }
        catch (STLFormatException e)
        {
            System.err.println("STL format error: " + e.getMessage());
            System.exit(3);
        }
        catch (Exception e)
        {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(4);
        }
    }
}
