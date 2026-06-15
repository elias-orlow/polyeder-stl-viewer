package org.alegroup.polyederstlviewer.constants;

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
}

