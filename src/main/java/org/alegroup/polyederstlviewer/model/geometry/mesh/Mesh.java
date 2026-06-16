package org.alegroup.polyederstlviewer.model.geometry.mesh;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.*;

/**
 * Represents a triangle mesh consisting of connected triangles.
 * A mesh is valid if it contains at least one triangle, contains no null triangles
 * and all triangles are connected through shared edges.
 *
 * @precondition The triangle list is not null, contains no null values and forms a connected mesh.
 * @postcondition A mesh object can be created if all validation rules are fulfilled.
 */
public class Mesh
{
    private final List<Triangle> triangles;

    /**
     * Creates a mesh from a list of triangles.
     *
     * @param triangles the list of triangles used to create the mesh
     * @throws IllegalArgumentException if the given triangles do not form a connected triangle mesh
     * @precondition triangles is not null, not empty, contains no null triangles and is connected.
     * @postcondition A new Mesh object with the given triangles is created.
     */
    public Mesh (List<Triangle> triangles)
    {
        if (!isMesh(triangles))
        {
            throw new IllegalArgumentException(ErrorMessages.MESH_NOT_CONNECTED_MESSAGE);
        }

        this.triangles = List.copyOf(triangles);
    }

    /**
     * Creates a mesh from an array of triangles.
     *
     * @param triangles the array of triangles used to create the mesh
     * @precondition triangles is not null, not empty, contains no null triangles and is connected.
     * @postcondition A new Mesh object with the given triangles is created.
     */
    public Mesh (Triangle[] triangles)
    {
        this(Arrays.asList(triangles));
    }

    /**
     * Checks whether the given triangle list forms a valid mesh.
     * A valid mesh must contain triangles, must not contain null triangles
     * and must be connected.
     *
     * @param triangles the list of triangles to check
     * @return true if the triangles form a valid mesh, otherwise false
     * @precondition triangles may be null.
     * @postcondition The mesh validation result is returned.
     */
    private boolean isMesh (List<Triangle> triangles)
    {
        return hasTriangles(triangles)
                && hasNoNullTriangles(triangles)
                && isConnected(triangles);
    }

    /**
     * Checks whether the given triangle list exists and contains at least one triangle.
     *
     * @param triangles the list of triangles to check
     * @return true if the list is not null and not empty, otherwise false
     * @precondition triangles may be null.
     * @postcondition The result of the triangle existence check is returned.
     */
    private boolean hasTriangles (List<Triangle> triangles)
    {
        return triangles != null && !triangles.isEmpty();
    }

    /**
     * Checks whether the given triangle list contains no null values.
     *
     * @param triangles the list of triangles to check
     * @return true if all triangles are not null, otherwise false
     * @precondition triangles is not null.
     * @postcondition The null validation result is returned.
     */
    private boolean hasNoNullTriangles (List<Triangle> triangles)
    {
        for (Triangle triangle : triangles)
        {
            if (triangle == null)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether all triangles in the given list are connected.
     * The method first creates a map from undirected edges to their triangles.
     * Then it performs a breadth-first search using this map to find neighboring triangles efficiently.
     *
     * @param triangles the list of triangles to check
     * @return true if all triangles are connected, otherwise false
     * @precondition triangles is not null, not empty and contains no null triangles.
     * @postcondition The connectivity validation result is returned.
     */
    private boolean isConnected(List<Triangle> triangles)
    {
        Map<EdgeKey, List<Triangle>> edgeToTriangles = new HashMap<>();

        for (Triangle triangle : triangles)
        {
            for (Edge edge : triangle.getEdges())
            {
                EdgeKey edgeKey = new EdgeKey(edge);

                edgeToTriangles
                        .computeIfAbsent(edgeKey, key -> new ArrayList<>())
                        .add(triangle);
            }
        }

        Set<Triangle> visitedTriangles = new HashSet<>();
        Queue<Triangle> queue = new ArrayDeque<>();

        Triangle startTriangle = triangles.get(GeneralConstants.FIRST_INDEX);

        visitedTriangles.add(startTriangle);
        queue.add(startTriangle);

        while (!queue.isEmpty())
        {
            Triangle currentTriangle = queue.poll();

            for (Edge edge : currentTriangle.getEdges())
            {
                EdgeKey edgeKey = new EdgeKey(edge);
                List<Triangle> neighbourTriangles = edgeToTriangles.get(edgeKey);

                for (Triangle neighbourTriangle : neighbourTriangles)
                {
                    if (visitedTriangles.add(neighbourTriangle))
                    {
                        queue.add(neighbourTriangle);
                    }
                }
            }
        }

        return visitedTriangles.size() == triangles.size();
    }

    /**
     * Returns the triangles of this mesh.
     *
     * @return the triangles of this mesh
     * @precondition None.
     * @postcondition The triangle list of this mesh is returned.
     */
    public List<Triangle> getTriangles ()
    {
        return triangles;
    }

    /**
     * Returns the number of triangles in this mesh.
     *
     * @return the number of triangles
     * @precondition None.
     * @postcondition The triangle count of this mesh is returned.
     */
    public int getTriangleCount ()
    {
        return triangles.size();
    }

    /**
     * Represents an undirected edge key.
     * The direction of the edge is ignored for equality.
     *
     * @precondition The edge is not null.
     * @postcondition An edge key can be used in hash-based collections.
     */
    private static class EdgeKey
    {
        private final Vertex start;
        private final Vertex end;

        /**
         * Creates an edge key from the given edge.
         *
         * @param edge the edge used to create the key
         * @precondition edge is not null.
         * @postcondition A new EdgeKey object is created.
         */
        public EdgeKey(Edge edge)
        {
            this.start = edge.getStart();
            this.end = edge.getEnd();
        }

        /**
         * Compares this edge key with another object.
         * Two edge keys are equal if they represent the same edge, independent of direction.
         *
         * @param object the object to compare with this edge key
         * @return true if both objects represent the same undirected edge, otherwise false
         * @precondition object may be null or any object.
         * @postcondition The equality result is returned.
         */
        @Override
        public boolean equals(Object object)
        {
            if (this == object)
            {
                return true;
            }

            if (!(object instanceof EdgeKey other))
            {
                return false;
            }

            boolean sameDirection =
                    this.start.equals(other.start)
                            && this.end.equals(other.end);

            boolean oppositeDirection =
                    this.start.equals(other.end)
                            && this.end.equals(other.start);

            return sameDirection || oppositeDirection;
        }

        /**
         * Calculates the hash code of this edge key.
         * The hash code is independent of the edge direction.
         *
         * @return the hash code of this edge key
         * @precondition None.
         * @postcondition A hash code consistent with equals is returned.
         */
        @Override
        public int hashCode()
        {
            return start.hashCode() + end.hashCode();
        }
    }
}