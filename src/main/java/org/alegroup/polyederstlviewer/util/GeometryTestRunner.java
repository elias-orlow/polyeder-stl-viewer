package org.alegroup.polyederstlviewer.util;

import org.alegroup.polyederstlviewer.constants.TestRunnerConstants;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Mesh;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Polygon;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.PolygonalChain;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vector3D;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.List;

/**
 * Manual test runner for the geometry model classes.
 *
 * @precondition Model classes are implemented and available.
 * @postcondition Test results are printed to the console.
 */
public class GeometryTestRunner
{
    private static int passedTests = TestRunnerConstants.INT_ZERO;
    private static int failedTests = TestRunnerConstants.INT_ZERO;

    /**
     * Runs all manual model tests.
     *
     * @param args command line arguments, not used
     *
     * @precondition None.
     * @postcondition Test results are printed to the console.
     */
    public static void main(String[] args)
    {
        System.out.println(TestRunnerConstants.GEOMETRY_TESTS_HEADER);

        testVertexEquality();
        testEdgeEquality();
        testVectorOperations();
        testValidPolygonalChain();
        testInvalidPolygonalChain();
        testValidPolygon();
        testInvalidOpenPolygon();
        testValidTriangle();
        testInvalidDegeneratedTriangle();
        testValidMesh();
        testInvalidDisconnectedMesh();
        testValidPolyhedron();
        testInvalidOpenPolyhedron();
        testPolyhedronSurfaceArea();

        printSummary();
    }

    /**
     * Tests equality and hash code behavior of vertices.
     *
     * @precondition Vertex class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testVertexEquality()
    {
        Vertex firstVertex = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_TWO,
                TestRunnerConstants.FLOAT_THREE
        );

        Vertex secondVertex = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_TWO,
                TestRunnerConstants.FLOAT_THREE
        );

        Vertex thirdVertex = new Vertex(
                TestRunnerConstants.FLOAT_THREE,
                TestRunnerConstants.FLOAT_TWO,
                TestRunnerConstants.FLOAT_ONE
        );

        assertTrue(
                TestRunnerConstants.TEST_VERTICES_EQUAL,
                firstVertex.equals(secondVertex)
        );

        assertTrue(
                TestRunnerConstants.TEST_VERTICES_HASH_EQUAL,
                firstVertex.hashCode() == secondVertex.hashCode()
        );

        assertFalse(
                TestRunnerConstants.TEST_VERTICES_DIFFERENT,
                firstVertex.equals(thirdVertex)
        );
    }

    /**
     * Tests equality and hash code behavior of edges.
     *
     * @precondition Edge class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testEdgeEquality()
    {
        Vertex a = new Vertex(
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex b = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Edge firstEdge = new Edge(a, b);

        Edge secondEdge = new Edge(
                new Vertex(
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO
                ),
                new Vertex(
                        TestRunnerConstants.FLOAT_ONE,
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO
                )
        );

        Edge oppositeEdge = new Edge(b, a);

        assertTrue(
                TestRunnerConstants.TEST_EDGES_EQUAL,
                firstEdge.equals(secondEdge)
        );

        assertFalse(
                TestRunnerConstants.TEST_EDGES_OPPOSITE,
                firstEdge.equals(oppositeEdge)
        );
    }

    /**
     * Tests basic vector operations.
     *
     * @precondition Vector3D class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testVectorOperations()
    {
        Vector3D firstVector = new Vector3D(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vector3D secondVector = new Vector3D(
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vector3D cross = firstVector.cross(secondVector);
        float dot = firstVector.dot(secondVector);

        assertFloatEquals(
                TestRunnerConstants.TEST_CROSS_PRODUCT_X,
                TestRunnerConstants.FLOAT_ZERO,
                cross.getX()
        );

        assertFloatEquals(
                TestRunnerConstants.TEST_CROSS_PRODUCT_Y,
                TestRunnerConstants.FLOAT_ZERO,
                cross.getY()
        );

        assertFloatEquals(
                TestRunnerConstants.TEST_CROSS_PRODUCT_Z,
                TestRunnerConstants.FLOAT_ONE,
                cross.getZ()
        );

        assertFloatEquals(
                TestRunnerConstants.TEST_DOT_PRODUCT_ZERO,
                TestRunnerConstants.FLOAT_ZERO,
                dot
        );

        assertFloatEquals(
                TestRunnerConstants.TEST_MAGNITUDE_UNIT_VECTOR,
                TestRunnerConstants.FLOAT_ONE,
                firstVector.magnitude()
        );
    }

    /**
     * Tests creation of a valid polygonal chain.
     *
     * @precondition PolygonalChain class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testValidPolygonalChain()
    {
        Vertex a = new Vertex(
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex b = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex c = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO
        );

        List<Edge> edges = List.of(
                new Edge(a, b),
                new Edge(b, c)
        );

        PolygonalChain polygonalChain = new PolygonalChain(edges);

        assertTrue(
                TestRunnerConstants.TEST_VALID_POLYGONAL_CHAIN,
                polygonalChain.getEdges().size() == TestRunnerConstants.INT_TWO
        );
    }

    /**
     * Tests rejection of an invalid polygonal chain.
     *
     * @precondition PolygonalChain class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testInvalidPolygonalChain()
    {
        Vertex a = new Vertex(
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex b = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex c = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex d = new Vertex(
                TestRunnerConstants.FLOAT_TWO,
                TestRunnerConstants.FLOAT_TWO,
                TestRunnerConstants.FLOAT_ZERO
        );

        List<Edge> edges = List.of(
                new Edge(a, b),
                new Edge(c, d)
        );

        assertThrows(
                TestRunnerConstants.TEST_INVALID_POLYGONAL_CHAIN,
                () -> new PolygonalChain(edges)
        );
    }

    /**
     * Tests creation of a valid closed polygon.
     *
     * @precondition Polygon class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testValidPolygon()
    {
        List<Edge> edges = createTriangleEdges();

        Polygon polygon = new Polygon(edges);

        assertTrue(
                TestRunnerConstants.TEST_VALID_POLYGON,
                polygon.getEdges().size() == TestRunnerConstants.INT_THREE
        );
    }

    /**
     * Tests rejection of an open polygon.
     *
     * @precondition Polygon class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testInvalidOpenPolygon()
    {
        Vertex a = new Vertex(
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex b = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex c = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex d = new Vertex(
                TestRunnerConstants.FLOAT_TWO,
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO
        );

        List<Edge> edges = List.of(
                new Edge(a, b),
                new Edge(b, c),
                new Edge(c, d)
        );

        assertThrows(
                TestRunnerConstants.TEST_OPEN_POLYGON,
                () -> new Polygon(edges)
        );
    }

    /**
     * Tests creation of a valid triangle.
     *
     * @precondition Triangle class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testValidTriangle()
    {
        Triangle triangle = createTriangle(
                new Vertex(
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO
                ),
                new Vertex(
                        TestRunnerConstants.FLOAT_ONE,
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO
                ),
                new Vertex(
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ONE,
                        TestRunnerConstants.FLOAT_ZERO
                ),
                new Vector3D(
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ONE
                )
        );

        assertFloatEquals(
                TestRunnerConstants.TEST_RIGHT_TRIANGLE_AREA,
                TestRunnerConstants.FLOAT_HALF,
                triangle.area()
        );
    }

    /**
     * Tests rejection of a degenerated triangle.
     *
     * @precondition Triangle class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testInvalidDegeneratedTriangle()
    {
        assertThrows(
                TestRunnerConstants.TEST_DEGENERATED_TRIANGLE,
                () -> createTriangle(
                        new Vertex(
                                TestRunnerConstants.FLOAT_ZERO,
                                TestRunnerConstants.FLOAT_ZERO,
                                TestRunnerConstants.FLOAT_ZERO
                        ),
                        new Vertex(
                                TestRunnerConstants.FLOAT_ONE,
                                TestRunnerConstants.FLOAT_ZERO,
                                TestRunnerConstants.FLOAT_ZERO
                        ),
                        new Vertex(
                                TestRunnerConstants.FLOAT_TWO,
                                TestRunnerConstants.FLOAT_ZERO,
                                TestRunnerConstants.FLOAT_ZERO
                        ),
                        new Vector3D(
                                TestRunnerConstants.FLOAT_ZERO,
                                TestRunnerConstants.FLOAT_ZERO,
                                TestRunnerConstants.FLOAT_ONE
                        )
                )
        );
    }

    /**
     * Tests creation of a connected mesh.
     *
     * @precondition Mesh class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testValidMesh()
    {
        List<Triangle> triangles = createTetrahedronTriangles();

        Mesh mesh = new Mesh(triangles);

        assertTrue(
                TestRunnerConstants.TEST_CONNECTED_MESH,
                mesh.getTriangleCount() == TestRunnerConstants.INT_FOUR
        );
    }

    /**
     * Tests rejection of a disconnected mesh.
     *
     * @precondition Mesh class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testInvalidDisconnectedMesh()
    {
        Triangle firstTriangle = createTriangle(
                new Vertex(
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO
                ),
                new Vertex(
                        TestRunnerConstants.FLOAT_ONE,
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO
                ),
                new Vertex(
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ONE,
                        TestRunnerConstants.FLOAT_ZERO
                ),
                new Vector3D(
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ONE
                )
        );

        Triangle secondTriangle = createTriangle(
                new Vertex(
                        TestRunnerConstants.FLOAT_TEN,
                        TestRunnerConstants.FLOAT_TEN,
                        TestRunnerConstants.FLOAT_TEN
                ),
                new Vertex(
                        TestRunnerConstants.FLOAT_ELEVEN,
                        TestRunnerConstants.FLOAT_TEN,
                        TestRunnerConstants.FLOAT_TEN
                ),
                new Vertex(
                        TestRunnerConstants.FLOAT_TEN,
                        TestRunnerConstants.FLOAT_ELEVEN,
                        TestRunnerConstants.FLOAT_TEN
                ),
                new Vector3D(
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ZERO,
                        TestRunnerConstants.FLOAT_ONE
                )
        );

        assertThrows(
                TestRunnerConstants.TEST_DISCONNECTED_MESH,
                () -> new Mesh(List.of(firstTriangle, secondTriangle))
        );
    }

    /**
     * Tests creation of a valid closed polyhedron.
     *
     * @precondition Polyhedron class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testValidPolyhedron()
    {
        List<Triangle> triangles = createTetrahedronTriangles();

        Polyhedron polyhedron = new Polyhedron(triangles);

        assertTrue(
                TestRunnerConstants.TEST_CLOSED_TETRAHEDRON,
                polyhedron.triangleCount() == TestRunnerConstants.INT_FOUR
        );
    }

    /**
     * Tests rejection of an open polyhedron.
     *
     * @precondition Polyhedron class is implemented.
     * @postcondition Test result is printed.
     */
    private static void testInvalidOpenPolyhedron()
    {
        List<Triangle> openTriangles = createTetrahedronTriangles().subList(
                TestRunnerConstants.INT_ZERO,
                TestRunnerConstants.INT_THREE
        );

        assertThrows(
                TestRunnerConstants.TEST_OPEN_POLYHEDRON,
                () -> new Polyhedron(openTriangles)
        );
    }

    /**
     * Tests surface area calculation of a tetrahedron.
     *
     * @precondition Polyhedron and Triangle area calculation are implemented.
     * @postcondition Test result is printed.
     */
    private static void testPolyhedronSurfaceArea()
    {
        Polyhedron polyhedron = new Polyhedron(createTetrahedronTriangles());

        assertTrue(
                TestRunnerConstants.TEST_SURFACE_AREA_POSITIVE,
                polyhedron.surfaceArea() > TestRunnerConstants.FLOAT_ZERO
        );
    }

    /**
     * Creates a simple triangle edge list.
     *
     * @return connected and closed triangle edges
     *
     * @precondition None.
     * @postcondition A valid edge list is returned.
     */
    private static List<Edge> createTriangleEdges()
    {
        Vertex a = new Vertex(
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex b = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ZERO
        );

        Vertex c = new Vertex(
                TestRunnerConstants.FLOAT_ZERO,
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ZERO
        );

        return List.of(
                new Edge(a, b),
                new Edge(b, c),
                new Edge(c, a)
        );
    }

    /**
     * Creates a triangle from three vertices and a normal vector.
     *
     * @param a the first vertex
     * @param b the second vertex
     * @param c the third vertex
     * @param normalVector the normal vector
     * @return a triangle with connected edges
     *
     * @precondition Vertices and normal vector are not null.
     * @postcondition A triangle is returned.
     */
    private static Triangle createTriangle(Vertex a, Vertex b, Vertex c, Vector3D normalVector)
    {
        List<Edge> edges = List.of(
                new Edge(a, b),
                new Edge(b, c),
                new Edge(c, a)
        );

        return new Triangle(edges, normalVector);
    }

    /**
     * Creates four triangles forming a closed tetrahedron.
     *
     * @return a list of four connected triangles
     *
     * @precondition None.
     * @postcondition A closed tetrahedron triangle list is returned.
     */
    private static List<Triangle> createTetrahedronTriangles()
    {
        Vertex a = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_ONE
        );

        Vertex b = new Vertex(
                TestRunnerConstants.FLOAT_MINUS_ONE,
                TestRunnerConstants.FLOAT_MINUS_ONE,
                TestRunnerConstants.FLOAT_ONE
        );

        Vertex c = new Vertex(
                TestRunnerConstants.FLOAT_MINUS_ONE,
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_MINUS_ONE
        );

        Vertex d = new Vertex(
                TestRunnerConstants.FLOAT_ONE,
                TestRunnerConstants.FLOAT_MINUS_ONE,
                TestRunnerConstants.FLOAT_MINUS_ONE
        );

        Triangle abc = createTriangle(
                a,
                c,
                b,
                new Vector3D(
                        TestRunnerConstants.FLOAT_ONE,
                        TestRunnerConstants.FLOAT_ONE,
                        TestRunnerConstants.FLOAT_ONE
                )
        );

        Triangle abd = createTriangle(
                a,
                b,
                d,
                new Vector3D(
                        TestRunnerConstants.FLOAT_ONE,
                        TestRunnerConstants.FLOAT_MINUS_ONE,
                        TestRunnerConstants.FLOAT_ONE
                )
        );

        Triangle acd = createTriangle(
                a,
                d,
                c,
                new Vector3D(
                        TestRunnerConstants.FLOAT_ONE,
                        TestRunnerConstants.FLOAT_ONE,
                        TestRunnerConstants.FLOAT_MINUS_ONE
                )
        );

        Triangle bcd = createTriangle(
                b,
                c,
                d,
                new Vector3D(
                        TestRunnerConstants.FLOAT_MINUS_ONE,
                        TestRunnerConstants.FLOAT_MINUS_ONE,
                        TestRunnerConstants.FLOAT_MINUS_ONE
                )
        );

        return List.of(abc, abd, acd, bcd);
    }

    /**
     * Checks whether a condition is true.
     *
     * @param testName the test description
     * @param condition the condition to check
     *
     * @precondition testName is not null.
     * @postcondition Test result is printed.
     */
    private static void assertTrue(String testName, boolean condition)
    {
        if (condition)
        {
            pass(testName);
        }
        else
        {
            fail(testName);
        }
    }

    /**
     * Checks whether a condition is false.
     *
     * @param testName the test description
     * @param condition the condition to check
     *
     * @precondition testName is not null.
     * @postcondition Test result is printed.
     */
    private static void assertFalse(String testName, boolean condition)
    {
        assertTrue(testName, !condition);
    }

    /**
     * Checks whether two float values are approximately equal.
     *
     * @param testName the test description
     * @param expected the expected value
     * @param actual the actual value
     *
     * @precondition testName is not null.
     * @postcondition Test result is printed.
     */
    private static void assertFloatEquals(String testName, float expected, float actual)
    {
        assertTrue(
                testName,
                Math.abs(expected - actual) <= TestRunnerConstants.EPSILON
        );
    }

    /**
     * Checks whether a piece of code throws a runtime exception.
     *
     * @param testName the test description
     * @param executable the code to execute
     *
     * @precondition executable is not null.
     * @postcondition Test result is printed.
     */
    private static void assertThrows(String testName, Runnable executable)
    {
        try
        {
            executable.run();
            fail(testName);
        }
        catch (RuntimeException exception)
        {
            pass(testName);
        }
    }

    /**
     * Prints a passed test result.
     *
     * @param testName the test description
     *
     * @precondition testName is not null.
     * @postcondition Passed counter is increased.
     */
    private static void pass(String testName)
    {
        passedTests++;
        System.out.println(TestRunnerConstants.GEOMETRY_PASS_PREFIX + testName);
    }

    /**
     * Prints a failed test result.
     *
     * @param testName the test description
     *
     * @precondition testName is not null.
     * @postcondition Failed counter is increased.
     */
    private static void fail(String testName)
    {
        failedTests++;
        System.out.println(TestRunnerConstants.GEOMETRY_FAIL_PREFIX + testName);
    }

    /**
     * Prints the final test summary.
     *
     * @precondition None.
     * @postcondition Summary is printed.
     */
    private static void printSummary()
    {
        System.out.println();
        System.out.println(TestRunnerConstants.GEOMETRY_TEST_SUMMARY_HEADER);
        System.out.println(TestRunnerConstants.GEOMETRY_TEST_PASSED_PREFIX + passedTests);
        System.out.println(TestRunnerConstants.GEOMETRY_TEST_FAILED_PREFIX + failedTests);
    }
}