package org.alegroup.polyederstlviewer.util;

import javafx.scene.shape.TriangleMesh;
import org.alegroup.polyederstlviewer.constants.TestRunnerConstants;
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
    public static void main (String[] args)
    {
        testAsciiSTLParsing();
    }

    public static void testAsciiSTLParsing ()
    {
        try
        {
            File tmp = File.createTempFile(
                    TestRunnerConstants.ASCII_TEMP_FILE_PREFIX,
                    TestRunnerConstants.STL_EXTENSION
            );

            try (FileWriter fw = new FileWriter(tmp))
            {
                // Normalen sind ausdrücklich richtig orientiert
                fw.write(TestRunnerConstants.ASCII_STL_LINE_SOLID);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_FACET_1);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_OUTER_LOOP);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_1);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_2);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_3);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_END_LOOP);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_END_FACET);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_FACET_2);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_OUTER_LOOP);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_1);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_4);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_3);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_END_LOOP);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_END_FACET);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_FACET_3);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_OUTER_LOOP);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_1);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_2);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_5);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_END_LOOP);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_END_FACET);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_FACET_4);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_OUTER_LOOP);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_3);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_4);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_VERTEX_2);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_END_LOOP);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_END_FACET);
                fw.write(TestRunnerConstants.ASCII_STL_LINE_END_SOLID);
            }

            System.out.println(
                    TestRunnerConstants.ASCII_TEMP_FILE_WRITTEN_PREFIX
                            + tmp.getAbsolutePath()
            );

            Polyhedron poly = STLParser.parse(tmp).getPolyhedron();

            System.out.println(
                    TestRunnerConstants.ASCII_TRIANGLES_PREFIX
                            + poly.triangleCount()
            );

            System.out.printf(
                    TestRunnerConstants.ASCII_SURFACE_AREA_FORMAT,
                    poly.surfaceArea()
            );

            System.out.printf(
                    TestRunnerConstants.ASCII_VOLUME_FORMAT,
                    poly.volume()
            );

            System.out.println(TestRunnerConstants.ASCII_TOP_TRIANGLES_HEADER);

            poly.trianglesSortedByAreaDesc().forEach(
                    t -> System.out.printf(
                            TestRunnerConstants.ASCII_TOP_TRIANGLE_AREA_FORMAT,
                            t.area()
                    )
            );

            TriangleMesh mesh = poly.toTriangleMesh();
            float[] points = mesh.getPoints().toArray(null);

            System.out.println(TestRunnerConstants.ASCII_MESH_POINTS_HEADER);

            for (
                    int i = TestRunnerConstants.INT_ZERO;
                    i < points.length;
                    i += TestRunnerConstants.ASCII_MESH_POINT_STEP
            )
            {
                float x = points[i];
                float y = points[i + TestRunnerConstants.INT_ONE];
                float z = points[i + TestRunnerConstants.INT_TWO];

                System.out.printf(
                        TestRunnerConstants.ASCII_MESH_POINT_FORMAT,
                        i / TestRunnerConstants.ASCII_MESH_POINT_STEP,
                        x,
                        y,
                        z
                );
            }
        } catch (IOException e)
        {
            System.err.println(
                    TestRunnerConstants.ASCII_TEST_FAILED_PREFIX
                            + e.getMessage()
            );
            e.printStackTrace(System.err);
        }
    }

    public static void testBinarySTLParsing ()
    {
        try
        {
            // 1) Temporäre Datei erzeugen
            File tmp = File.createTempFile(
                    TestRunnerConstants.BINARY_TEMP_FILE_PREFIX,
                    TestRunnerConstants.STL_EXTENSION
            );

            // 2) Binäre STL-Daten erzeugen (1 Dreieck)
            // Format:
            // 80 Byte Header
            // 4 Byte: Anzahl Dreiecke (UINT32)
            // Dreieck: 12 floats + 1 short = 50 Bytes

            ByteBuffer bb = ByteBuffer.allocate(
                    TestRunnerConstants.BINARY_STL_HEADER_BYTE_COUNT
                            + TestRunnerConstants.BINARY_STL_TRIANGLE_COUNT_BYTE_COUNT
                            + TestRunnerConstants.BINARY_STL_TRIANGLE_BYTE_COUNT
            );

            bb.order(ByteOrder.LITTLE_ENDIAN);

            // Header (80 Bytes, egal was drinsteht, nur kein "solid")
            for (
                    int i = TestRunnerConstants.INT_ZERO;
                    i < TestRunnerConstants.BINARY_STL_HEADER_BYTE_COUNT;
                    i++
            )
            {
                bb.put((byte) TestRunnerConstants.BINARY_HEADER_FILL_BYTE);
            }

            // Anzahl Dreiecke = 1
            bb.putInt(TestRunnerConstants.BINARY_STL_TRIANGLE_COUNT);

            // Normalenvektor
            bb.putFloat(TestRunnerConstants.FLOAT_ZERO);
            bb.putFloat(TestRunnerConstants.FLOAT_ZERO);
            bb.putFloat(TestRunnerConstants.FLOAT_ONE);

            // Vertex 1
            bb.putFloat(TestRunnerConstants.FLOAT_ZERO);
            bb.putFloat(TestRunnerConstants.FLOAT_ZERO);
            bb.putFloat(TestRunnerConstants.FLOAT_ZERO);

            // Vertex 2
            bb.putFloat(TestRunnerConstants.FLOAT_ONE);
            bb.putFloat(TestRunnerConstants.FLOAT_ZERO);
            bb.putFloat(TestRunnerConstants.FLOAT_ZERO);

            // Vertex 3
            bb.putFloat(TestRunnerConstants.FLOAT_ZERO);
            bb.putFloat(TestRunnerConstants.FLOAT_ONE);
            bb.putFloat(TestRunnerConstants.FLOAT_ZERO);

            // Attribute short (immer 0)
            bb.putShort((short) TestRunnerConstants.BINARY_ATTRIBUTE_BYTE_COUNT);

            // 3) Datei schreiben
            java.nio.file.Files.write(tmp.toPath(), bb.array());

            System.out.println(
                    TestRunnerConstants.BINARY_TEMP_FILE_WRITTEN_PREFIX
                            + tmp.getAbsolutePath()
            );

            // 4) Datei einlesen
            Polyhedron poly = STLParser.parse(tmp).getPolyhedron();

            // 5) Ergebnisse ausgeben
            System.out.println(TestRunnerConstants.BINARY_PARSE_RESULT_HEADER);

            System.out.println(
                    TestRunnerConstants.BINARY_TRIANGLE_COUNT_PREFIX
                            + poly.triangleCount()
            );

            for (Triangle t : poly.getTriangles())
            {
                System.out.println(t);
            }

            System.out.println(
                    TestRunnerConstants.BINARY_SURFACE_AREA_PREFIX
                            + poly.surfaceArea()
            );

            System.out.println(
                    TestRunnerConstants.BINARY_VOLUME_PREFIX
                            + poly.volume()
            );
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}