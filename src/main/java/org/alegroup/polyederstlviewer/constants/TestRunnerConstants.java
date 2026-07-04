package org.alegroup.polyederstlviewer.constants;

/**
 * Provides centralized constants for manual STL parser tests and geometry model tests.
 * <p>
 * This interface is used to avoid hard-coded literals inside the TestRunner
 * and GeometryTestRunner classes.
 *
 * @precondition none
 * @postcondition All test runner constants are centrally available
 */
public interface TestRunnerConstants
{
    int INT_ZERO = 0;
    int INT_ONE = 1;
    int INT_TWO = 2;
    int INT_THREE = 3;
    int INT_FOUR = 4;
    int INT_TEN = 10;
    int INT_ELEVEN = 11;
    int ASCII_MESH_POINT_STEP = 3;

    int BINARY_STL_HEADER_BYTE_COUNT = 80;
    int BINARY_STL_TRIANGLE_COUNT_BYTE_COUNT = 4;
    int BINARY_STL_TRIANGLE_BYTE_COUNT = 50;
    int BINARY_STL_TRIANGLE_COUNT = 1;
    int BINARY_HEADER_FILL_BYTE = 0;
    int BINARY_ATTRIBUTE_BYTE_COUNT = 0;

    float FLOAT_ZERO = 0.0f;
    float FLOAT_HALF = 0.5f;
    float FLOAT_ONE = 1.0f;
    float FLOAT_TWO = 2.0f;
    float FLOAT_THREE = 3.0f;
    float FLOAT_FOUR = 4.0f;
    float FLOAT_FIVE = 5.0f;
    float FLOAT_SEVEN = 7.0f;
    float FLOAT_TEN = 10.0f;
    float FLOAT_ELEVEN = 11.0f;

    float FLOAT_MINUS_ONE = -1.0f;
    float FLOAT_MINUS_TWO = -2.0f;
    float FLOAT_MINUS_THREE = -3.0f;
    float FLOAT_MINUS_SIX = -6.0f;
    float FLOAT_MINUS_EIGHT = -8.0f;

    float EPSILON = 0.0001f;

    String ASCII_TEMP_FILE_PREFIX = "tetra";
    String BINARY_TEMP_FILE_PREFIX = "binary_test";
    String STL_EXTENSION = ".stl";

    String ASCII_STL_LINE_SOLID = "solid tetra\n";
    String ASCII_STL_LINE_FACET_1 = "facet normal 7 4 5 \n";
    String ASCII_STL_LINE_FACET_2 = "facet normal -6 6 -2 \n";
    String ASCII_STL_LINE_FACET_3 = "facet normal -3 -8 1 \n";
    String ASCII_STL_LINE_FACET_4 = "facet normal 2 -2 -8 \n";
    String ASCII_STL_LINE_OUTER_LOOP = "outer loop\n";
    String ASCII_STL_LINE_VERTEX_1 = "vertex 2 1 3 \n";
    String ASCII_STL_LINE_VERTEX_2 = "vertex 4 0 1 \n";
    String ASCII_STL_LINE_VERTEX_3 = "vertex 3 3 0 \n";
    String ASCII_STL_LINE_VERTEX_4 = "vertex 1 1 0 \n";
    String ASCII_STL_LINE_VERTEX_5 = "vertex  1 1 0 \n";
    String ASCII_STL_LINE_END_LOOP = "endloop\n";
    String ASCII_STL_LINE_END_FACET = "endfacet\n";
    String ASCII_STL_LINE_END_SOLID = "endsolid tetra\n";

    String ASCII_TEMP_FILE_WRITTEN_PREFIX = "Temporary test STL written to: ";
    String ASCII_TRIANGLES_PREFIX = "Triangles: ";
    String ASCII_SURFACE_AREA_FORMAT = "Surface area: %.6f\n";
    String ASCII_VOLUME_FORMAT = "Volume: %.6f\n";
    String ASCII_TOP_TRIANGLES_HEADER = "Top triangles:";
    String ASCII_TOP_TRIANGLE_AREA_FORMAT = " area=%.6f\n";
    String ASCII_MESH_POINTS_HEADER = "=== Mesh Points ===";
    String ASCII_MESH_POINT_FORMAT = "Point %d: (%.3f, %.3f, %.3f)%n";
    String ASCII_TEST_FAILED_PREFIX = "Test failed: ";

    String BINARY_TEMP_FILE_WRITTEN_PREFIX = "Binary STL test file written to: ";
    String BINARY_PARSE_RESULT_HEADER = "=== BINARY STL PARSE RESULT ===";
    String BINARY_TRIANGLE_COUNT_PREFIX = "Triangle count: ";
    String BINARY_SURFACE_AREA_PREFIX = "Surface area: ";
    String BINARY_VOLUME_PREFIX = "Volume: ";

    String GEOMETRY_TESTS_HEADER = "=== Geometry Model Tests ===";
    String GEOMETRY_TEST_SUMMARY_HEADER = "=== Test Summary ===";
    String GEOMETRY_TEST_PASSED_PREFIX = "Passed: ";
    String GEOMETRY_TEST_FAILED_PREFIX = "Failed: ";
    String GEOMETRY_PASS_PREFIX = "[PASS] ";
    String GEOMETRY_FAIL_PREFIX = "[FAIL] ";

    String TEST_VERTICES_EQUAL = "Vertices with equal coordinates are equal";
    String TEST_VERTICES_HASH_EQUAL = "Equal vertices have equal hash codes";
    String TEST_VERTICES_DIFFERENT = "Vertices with different coordinates are not equal";
    String TEST_EDGES_EQUAL = "Edges with same start and end are equal";
    String TEST_EDGES_OPPOSITE = "Edges with opposite direction are not equal";
    String TEST_CROSS_PRODUCT_X = "Cross product x-component";
    String TEST_CROSS_PRODUCT_Y = "Cross product y-component";
    String TEST_CROSS_PRODUCT_Z = "Cross product z-component";
    String TEST_DOT_PRODUCT_ZERO = "Dot product of orthogonal vectors is zero";
    String TEST_MAGNITUDE_UNIT_VECTOR = "Magnitude of unit vector is one";
    String TEST_VALID_POLYGONAL_CHAIN = "Valid polygonal chain is created";
    String TEST_INVALID_POLYGONAL_CHAIN = "Invalid polygonal chain is rejected";
    String TEST_VALID_POLYGON = "Valid closed polygon is created";
    String TEST_OPEN_POLYGON = "Open polygon is rejected";
    String TEST_RIGHT_TRIANGLE_AREA = "Right triangle area is 0.5";
    String TEST_DEGENERATED_TRIANGLE = "Degenerated triangle is rejected";
    String TEST_CONNECTED_MESH = "Connected mesh is created";
    String TEST_DISCONNECTED_MESH = "Disconnected mesh is rejected";
    String TEST_CLOSED_TETRAHEDRON = "Closed tetrahedron polyhedron is created";
    String TEST_OPEN_POLYHEDRON = "Open polyhedron is rejected";
    String TEST_SURFACE_AREA_POSITIVE = "Surface area of tetrahedron is positive";
}
