package org.alegroup.polyederstlviewer.util;

import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
        testAsciiSTLParsing();
    }

    public static void testAsciiSTLParsing ()
    {
        try
        {
            File tmp = File.createTempFile("tetra", ".stl");
            try (FileWriter fw = new FileWriter(tmp))
            {
                fw.write("solid tetra\n");
                fw.write("facet normal 7 4 6 \n");
                fw.write("outer loop\n");
                fw.write("vertex 2 1 3 \n");
                fw.write("vertex 4 0 1 \n");
                fw.write("vertex 3 3 0 \n");
                fw.write("endloop\n");
                fw.write("endfacet\n");
                fw.write("facet normal -6 6 -2 \n");
                fw.write("outer loop\n");
                fw.write("vertex 2 1 3 \n");
                fw.write("vertex 1 1 0 \n");
                fw.write("vertex 3 3 0 \n");
                fw.write("endloop\n");
                fw.write("endfacet\n");
                fw.write("facet normal -3 -8 1 \n");
                fw.write("outer loop\n");
                fw.write("vertex 2 1 3 \n");
                fw.write("vertex 4 0 1 \n");
                fw.write("vertex  1 1 0 \n");
                fw.write("endloop\n");
                fw.write("endfacet\n");
                fw.write("facet normal -4 -2 -8 \n");
                fw.write("outer loop\n");
                fw.write("vertex 3 3 0 \n");
                fw.write("vertex 1 1 0 \n");
                fw.write("vertex 4 0 1 \n");
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

    public static void testBinarySTLParsing ()
    {
        try
        {
            // 1) Temporäre Datei erzeugen
            File tmp = File.createTempFile("binary_test", ".stl");

            // 2) Binäre STL-Daten erzeugen (1 Dreieck)
            // Format:
            // 80 Byte Header
            // 4 Byte: Anzahl Dreiecke (UINT32)
            // Dreieck: 12 floats + 1 short = 50 Bytes

            ByteBuffer bb = ByteBuffer.allocate(80 + 4 + 50);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            // Header (80 Bytes, egal was drinsteht, nur kein "solid")
            for (int i = 0; i < 80; i++)
            {
                bb.put((byte) 0);
            }

            // Anzahl Dreiecke = 1
            bb.putInt(1);

            // Normalenvektor
            bb.putFloat(0.0f);
            bb.putFloat(0.0f);
            bb.putFloat(1.0f);

            // Vertex 1
            bb.putFloat(0.0f);
            bb.putFloat(0.0f);
            bb.putFloat(0.0f);

            // Vertex 2
            bb.putFloat(1.0f);
            bb.putFloat(0.0f);
            bb.putFloat(0.0f);

            // Vertex 3
            bb.putFloat(0.0f);
            bb.putFloat(1.0f);
            bb.putFloat(0.0f);

            // Attribute short (immer 0)
            bb.putShort((short) 0);

            // 3) Datei schreiben
            java.nio.file.Files.write(tmp.toPath(), bb.array());

            System.out.println("Binary STL test file written to: " + tmp.getAbsolutePath());

            // 4) Datei einlesen
            Polyhedron poly = STLParser.parse(tmp);

            // 5) Ergebnisse ausgeben
            System.out.println("=== BINARY STL PARSE RESULT ===");
            System.out.println("Triangle count: " + poly.triangleCount());

            for (Triangle t : poly.getTriangles())
            {
                System.out.println(t);
            }

            System.out.println("Surface area: " + poly.surfaceArea());
            System.out.println("Volume: " + poly.volume());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // todo: flächeninhalt wird noch nicht richtig berechnet
}
