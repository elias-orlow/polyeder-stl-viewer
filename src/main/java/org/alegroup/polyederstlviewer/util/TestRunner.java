package org.alegroup.polyederstlviewer.util;

import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Simple test runner that writes a small ASCII STL (tetrahedron) to a temp file
 * and runs the parser and analysis. This is a convenience class for local testing.
 *
 * @precondition JVM has permission to write to temp directory.
 * @postcondition Writes a temp STL file and prints expected outputs.
 */
public class TestRunner
{
    /**
     * Run the test. Creates a tetrahedron ASCII STL and parses it.
     *
     * @precondition None.
     * @postcondition Prints parsing results to console.
     */
    public static void main(String[] args)
    {
        try
        {
            File tmp = File.createTempFile("tetra", ".stl");
            try (FileWriter fw = new FileWriter(tmp))
            {
                fw.write("solid tetra\n");
                fw.write("facet normal 0 0 0\n");
                fw.write("outer loop\n");
                fw.write("vertex 1 1 1\n");
                fw.write("vertex -1 -1 1\n");
                fw.write("vertex -1 1 -1\n");
                fw.write("endloop\n");
                fw.write("endfacet\n");
                fw.write("facet normal 0 0 0\n");
                fw.write("outer loop\n");
                fw.write("vertex 1 1 1\n");
                fw.write("vertex -1 -1 1\n");
                fw.write("vertex 1 -1 -1\n");
                fw.write("endloop\n");
                fw.write("endfacet\n");
                fw.write("facet normal 0 0 0\n");
                fw.write("outer loop\n");
                fw.write("vertex -1 1 -1\n");
                fw.write("vertex -1 -1 1\n");
                fw.write("vertex 1 -1 -1\n");
                fw.write("endloop\n");
                fw.write("endfacet\n");
                fw.write("facet normal 0 0 0\n");
                fw.write("outer loop\n");
                fw.write("vertex 1 1 1\n");
                fw.write("vertex 1 -1 -1\n");
                fw.write("vertex -1 1 -1\n");
                fw.write("endloop\n");
                fw.write("endfacet\n");
                fw.write("endsolid tetra\n");
            }

            System.out.println("Temporary test STL written to: " + tmp.getAbsolutePath());
            Polyhedron poly = STLParser.parse(tmp);
            System.out.println("Triangles: " + poly.triangleCount());
            System.out.printf("Surface area: %.6f\n", poly.surfaceArea());
            System.out.printf("Volume: %.6f\n", poly.volume());
            System.out.println("Top triangles:");
            poly.trianglesSortedByAreaDesc().forEach(t -> System.out.printf(" area=%.6f\n", t.area()));
        }
        catch (IOException | STLFormatException e)
        {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    // todo: der normalenvektor wird noch nicht richtig eingelesen
}
