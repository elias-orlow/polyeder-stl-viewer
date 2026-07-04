package org.alegroup.polyederstlviewer.model.geometry.mesh;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.*;

/**
 * Represents a triangle mesh consisting of connected triangles.
 * A mesh is valid if it contains at least one triangle, contains no null triangles,
 * and (optionally) forms a connected structure through shared edges.
 *
 * @precondition Triangle list must be non-null, non-empty, contain no null entries,
 * and satisfy mesh validity rules.
 * @postcondition A Mesh instance is created if all validation rules are fulfilled.
 */
public class Mesh
{

    /**
     * Immutable list of triangles forming this mesh.
     */
    private final List<Triangle> triangles;

    /**
     * Creates a mesh from a list of triangles.
     *
     * @param triangles the list of triangles forming the mesh
     * @throws IllegalArgumentException if the triangles do not form a valid mesh
     * @precondition triangles != null AND triangles not empty AND triangles contain no null values
     * @postcondition A new Mesh instance is created
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
     * @param triangles the array of triangles forming the mesh
     * @precondition triangles != null AND triangles not empty AND triangles contain no null values
     * @postcondition A new Mesh instance is created
     */
    public Mesh (Triangle[] triangles)
    {
        this(Arrays.asList(triangles));
    }

    /**
     * Validates whether the given triangle list forms a valid mesh.
     * A valid mesh must contain triangles and must not contain null entries.
     * Connectivity checking is currently disabled.
     *
     * @param triangles the list of triangles to validate
     * @return true if the triangles form a valid mesh, otherwise false
     * @precondition triangles may be null
     * @postcondition A boolean indicating mesh validity is returned
     */
    private boolean isMesh (List<Triangle> triangles)
    {
        return hasTriangles(triangles)
                && hasNoNullTriangles(triangles) && isConnected(triangles);
    }

    /**
     * Checks whether the triangle list exists and contains at least one triangle.
     *
     * @param triangles the list of triangles to check
     * @return true if the list is non-null and non-empty, otherwise false
     * @precondition triangles may be null
     * @postcondition A boolean indicating triangle existence is returned
     */
    private boolean hasTriangles (List<Triangle> triangles)
    {
        return triangles != null && !triangles.isEmpty();
    }

    /**
     * Checks whether the triangle list contains no null entries.
     *
     * @param triangles the list of triangles to check
     * @return true if all triangles are non-null, otherwise false
     * @precondition triangles != null
     * @postcondition A boolean indicating null-check validity is returned
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
     * Checks whether all triangles in the list are connected through shared edges.
     * Connectivity is determined using a breadth-first search over shared edges.
     *
     * @param triangles the list of triangles to check
     * @return true if all triangles are connected, otherwise false
     * @precondition triangles != null AND triangles not empty AND triangles contain no null values
     * @postcondition A boolean indicating connectivity is returned
     */
    private boolean isConnected (List<Triangle> triangles)
    {

        Map<EdgeKey, List<Triangle>> edgeToTriangles = new HashMap<>();

        for (Triangle triangle : triangles)
        {
            for (Edge edge : triangle.getEdges())
            {

                EdgeKey key = new EdgeKey(edge);

                edgeToTriangles
                        .computeIfAbsent(key, k -> new ArrayList<>())
                        .add(triangle);
            }
        }

        Set<Triangle> visited = new HashSet<>();
        Queue<Triangle> queue = new ArrayDeque<>();

        Triangle start = triangles.get(GeneralConstants.FIRST_INDEX);

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty())
        {

            Triangle current = queue.poll();

            for (Edge edge : current.getEdges())
            {

                EdgeKey key = new EdgeKey(edge);
                List<Triangle> neighbours = edgeToTriangles.get(key);

                for (Triangle neighbour : neighbours)
                {
                    if (visited.add(neighbour))
                    {
                        queue.add(neighbour);
                    }
                }
            }
        }

        return visited.size() == triangles.size();
    }

    /**
     * Returns the triangles of this mesh.
     *
     * @return an immutable list of triangles
     * @precondition none
     * @postcondition A non-null list is returned
     */
    public List<Triangle> getTriangles ()
    {
        return triangles;
    }

    /**
     * Returns the number of triangles in this mesh.
     *
     * @return the number of triangles
     * @precondition none
     * @postcondition integer >= 0
     */
    public int getTriangleCount ()
    {
        return triangles.size();
    }

    /**
     * Represents an undirected edge key for connectivity checking.
     * The direction of the edge is ignored for equality.
     *
     * @precondition edge != null
     * @postcondition EdgeKey can be used in hash-based collections
     */
    private static class EdgeKey
    {

        private final Vertex start;
        private final Vertex end;

        /**
         * Creates an undirected edge key from the given edge.
         *
         * @param edge the edge used to create the key
         * @precondition edge != null
         * @postcondition A new EdgeKey instance is created
         */
        public EdgeKey (Edge edge)
        {
            this.start = edge.getStart();
            this.end = edge.getEnd();
        }

        /**
         * Determines whether this edge key is equal to another object.
         * Two keys are equal if they represent the same undirected edge.
         *
         * @param obj the object to compare
         * @return true if equal, otherwise false
         * @precondition obj may be null
         * @postcondition A boolean indicating equality is returned
         */
        @Override
        public boolean equals (Object obj)
        {

            if (this == obj) return true;
            if (!(obj instanceof EdgeKey other)) return false;

            boolean sameDirection =
                    this.start.equals(other.start) &&
                            this.end.equals(other.end);

            boolean oppositeDirection =
                    this.start.equals(other.end) &&
                            this.end.equals(other.start);

            return sameDirection || oppositeDirection;
        }

        /**
         * Computes a hash code independent of edge direction.
         *
         * @return the hash code
         * @precondition none
         * @postcondition A hash code consistent with equals() is returned
         */
        @Override
        public int hashCode ()
        {
            return start.hashCode() + end.hashCode();
        }
    }
}