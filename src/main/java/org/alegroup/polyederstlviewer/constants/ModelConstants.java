package org.alegroup.polyederstlviewer.constants;

/**
 * Centralized constants for all geometry and model-related classes.
 * <p>
 * This interface groups together formatting strings, numeric constants,
 * and structural definitions used throughout the geometry, mesh, and math
 * subsystems of the application. It serves as a single source of truth
 * for model-level literals to ensure consistency and avoid duplication.
 */
public interface ModelConstants
{
    String VertexToStringText = "Vertex{ \n  x=%f, y=%f, z=%f \n}";
    String VectorToStringText = "Vector3D(%.6f, %.6f, %.6f)";
    int MIN_POLYGON_EDGE_COUNT = 3;

    int TRIANGLE_EDGE_COUNT = 3;
    float TRIANGLE_EPSILON = 1e-6f;
    float TRIANGLE_AREA_FACTOR = 0.5f;
    float SIGNED_VOLUME_DIVISOR = 6.0f;

    String TRIANGLE_TO_STRING_FORMAT = """
            Triangle {
              A = %.6f | %.6f | %.6f,
              B = %.6f | %.6f | %.6f,
              C = %.6f | %.6f | %.6f,
              normal = %s,
              area = %.6f
            }""";

    int CLOSED_MESH_EDGE_USAGE_COUNT = 2;
    int EDGE_COUNTER_INCREMENT = 1;

    String EDGE_KEY_TO_STRING_FORMAT = "%s%s";

    int DEFAULT_BATCH_SIZE = 1000;
    String AREA_WORKER_THREAD_NAME_PREFIX = "area-worker-";
}