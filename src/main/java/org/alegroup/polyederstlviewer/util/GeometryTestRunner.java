package org.alegroup.polyederstlviewer.util;

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
    private static final float EPSILON = 0.0001f;

    private static int passedTests = 0;
    private static int failedTests = 0;

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
        System.out.println("=== Geometry Model Tests ===");

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
        Vertex firstVertex = new Vertex(1.0f, 2.0f, 3.0f);
        Vertex secondVertex = new Vertex(1.0f, 2.0f, 3.0f);
        Vertex thirdVertex = new Vertex(3.0f, 2.0f, 1.0f);

        assertTrue(
                "Vertices with equal coordinates are equal",
                firstVertex.equals(secondVertex)
        );

        assertTrue(
                "Equal vertices have equal hash codes",
                firstVertex.hashCode() == secondVertex.hashCode()
        );

        assertFalse(
                "Vertices with different coordinates are not equal",
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
        Vertex a = new Vertex(0.0f, 0.0f, 0.0f);
        Vertex b = new Vertex(1.0f, 0.0f, 0.0f);

        Edge firstEdge = new Edge(a, b);
        Edge secondEdge = new Edge(new Vertex(0.0f, 0.0f, 0.0f), new Vertex(1.0f, 0.0f, 0.0f));
        Edge oppositeEdge = new Edge(b, a);

        assertTrue(
                "Edges with same start and end are equal",
                firstEdge.equals(secondEdge)
        );

        assertFalse(
                "Edges with opposite direction are not equal",
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
        Vector3D firstVector = new Vector3D(1.0f, 0.0f, 0.0f);
        Vector3D secondVector = new Vector3D(0.0f, 1.0f, 0.0f);

        Vector3D cross = firstVector.cross(secondVector);
        float dot = firstVector.dot(secondVector);

        assertFloatEquals(
                "Cross product x-component",
                0.0f,
                cross.getX()
        );

        assertFloatEquals(
                "Cross product y-component",
                0.0f,
                cross.getY()
        );

        assertFloatEquals(
                "Cross product z-component",
                1.0f,
                cross.getZ()
        );

        assertFloatEquals(
                "Dot product of orthogonal vectors is zero",
                0.0f,
                dot
        );

        assertFloatEquals(
                "Magnitude of unit vector is one",
                1.0f,
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
        Vertex a = new Vertex(0.0f, 0.0f, 0.0f);
        Vertex b = new Vertex(1.0f, 0.0f, 0.0f);
        Vertex c = new Vertex(1.0f, 1.0f, 0.0f);

        List<Edge> edges = List.of(
                new Edge(a, b),
                new Edge(b, c)
        );

        PolygonalChain polygonalChain = new PolygonalChain(edges);

        assertTrue(
                "Valid polygonal chain is created",
                polygonalChain.getEdges().size() == 2
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
        Vertex a = new Vertex(0.0f, 0.0f, 0.0f);
        Vertex b = new Vertex(1.0f, 0.0f, 0.0f);
        Vertex c = new Vertex(1.0f, 1.0f, 0.0f);
        Vertex d = new Vertex(2.0f, 2.0f, 0.0f);

        List<Edge> edges = List.of(
                new Edge(a, b),
                new Edge(c, d)
        );

        assertThrows(
                "Invalid polygonal chain is rejected",
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
                "Valid closed polygon is created",
                polygon.getEdges().size() == 3
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
        Vertex a = new Vertex(0.0f, 0.0f, 0.0f);
        Vertex b = new Vertex(1.0f, 0.0f, 0.0f);
        Vertex c = new Vertex(1.0f, 1.0f, 0.0f);
        Vertex d = new Vertex(2.0f, 1.0f, 0.0f);

        List<Edge> edges = List.of(
                new Edge(a, b),
                new Edge(b, c),
                new Edge(c, d)
        );

        assertThrows(
                "Open polygon is rejected",
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
                new Vertex(0.0f, 0.0f, 0.0f),
                new Vertex(1.0f, 0.0f, 0.0f),
                new Vertex(0.0f, 1.0f, 0.0f),
                new Vector3D(0.0f, 0.0f, 1.0f)
        );

        assertFloatEquals(
                "Right triangle area is 0.5",
                0.5f,
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
                "Degenerated triangle is rejected",
                () -> createTriangle(
                        new Vertex(0.0f, 0.0f, 0.0f),
                        new Vertex(1.0f, 0.0f, 0.0f),
                        new Vertex(2.0f, 0.0f, 0.0f),
                        new Vector3D(0.0f, 0.0f, 1.0f)
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
                "Connected mesh is created",
                mesh.getTriangleCount() == 4
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
                new Vertex(0.0f, 0.0f, 0.0f),
                new Vertex(1.0f, 0.0f, 0.0f),
                new Vertex(0.0f, 1.0f, 0.0f),
                new Vector3D(0.0f, 0.0f, 1.0f)
        );

        Triangle secondTriangle = createTriangle(
                new Vertex(10.0f, 10.0f, 10.0f),
                new Vertex(11.0f, 10.0f, 10.0f),
                new Vertex(10.0f, 11.0f, 10.0f),
                new Vector3D(0.0f, 0.0f, 1.0f)
        );

        assertThrows(
                "Disconnected mesh is rejected",
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
                "Closed tetrahedron polyhedron is created",
                polyhedron.triangleCount() == 4
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
        List<Triangle> openTriangles = createTetrahedronTriangles().subList(0, 3);

        assertThrows(
                "Open polyhedron is rejected",
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
                "Surface area of tetrahedron is positive",
                polyhedron.surfaceArea() > 0.0f
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
        Vertex a = new Vertex(0.0f, 0.0f, 0.0f);
        Vertex b = new Vertex(1.0f, 0.0f, 0.0f);
        Vertex c = new Vertex(0.0f, 1.0f, 0.0f);

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
        Vertex a = new Vertex(1.0f, 1.0f, 1.0f);
        Vertex b = new Vertex(-1.0f, -1.0f, 1.0f);
        Vertex c = new Vertex(-1.0f, 1.0f, -1.0f);
        Vertex d = new Vertex(1.0f, -1.0f, -1.0f);

        Triangle abc = createTriangle(a, c, b, new Vector3D(1.0f, 1.0f, 1.0f));
        Triangle abd = createTriangle(a, b, d, new Vector3D(1.0f, -1.0f, 1.0f));
        Triangle acd = createTriangle(a, d, c, new Vector3D(1.0f, 1.0f, -1.0f));
        Triangle bcd = createTriangle(b, c, d, new Vector3D(-1.0f, -1.0f, -1.0f));

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
        assertTrue(testName, Math.abs(expected - actual) <= EPSILON);
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
        System.out.println("[PASS] " + testName);
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
        System.out.println("[FAIL] " + testName);
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
        System.out.println("=== Test Summary ===");
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
    }
}