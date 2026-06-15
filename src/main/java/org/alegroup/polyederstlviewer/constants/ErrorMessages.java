package org.alegroup.polyederstlviewer.constants;

public interface ErrorMessages
{
    String POLYGONAL_CHAIN_INVALID_MESSAGE = "Edges do not form a polygonal chain.";
    String EDGES_LIST_NULL_MESSAGE = "Edge list must not be null.";
    String EDGES_ARRAY_NULL_MESSAGE = "Edge array must not be null.";
    String EDGE_START_NULL_MESSAGE = "Start vertex must not be null.";
    String EDGE_END_NULL_MESSAGE = "End vertex must not be null.";
    String POLYGON_NOT_CLOSED_MESSAGE = "The given polygonal chain is not a closed polygon.";
    String TRIANGLE_EDGE_COUNT_MESSAGE = "A triangle must have exactly three edges.";
    String TRIANGLE_DEGENERATED_MESSAGE = "A triangle must not be degenerated.";
    String TRIANGLE_NORMAL_VECTOR_NULL_MESSAGE = "A triangle needs a normal vector.";
    String MESH_NOT_CONNECTED_MESSAGE = "The given triangles do not form a connected triangle mesh.";
    String POLYHEDRON_NOT_CLOSED_MESSAGE = "A polyhedron must be a closed triangle mesh.";
    String NO_STL_FILE = "No STL file found.";
    String POLYHEDRON_EMPTY = "Cannot create a mesh from an empty polyhedron.";
}
