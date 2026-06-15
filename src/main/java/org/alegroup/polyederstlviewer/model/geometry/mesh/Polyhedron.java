package org.alegroup.polyederstlviewer.model.geometry.mesh;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.constants.ModelConstants;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a polyhedron as a closed triangle mesh.
 * A polyhedron can calculate surface area, volume and provide triangle analysis methods.
 *
 * @precondition Triangles form a closed mesh.
 * @postcondition A polyhedron instance is created and provides analysis methods.
 */
public class Polyhedron extends Mesh
{
    /**
     * Creates a polyhedron from a list of triangles.
     *
     * @param triangles the list of triangles used to create the polyhedron
     * @throws IllegalArgumentException if the triangle mesh is not closed
     * @precondition triangles is not null, connected and forms a closed triangle mesh.
     * @postcondition A new Polyhedron object is created.
     */
    public Polyhedron (List<Triangle> triangles)
    {
        super(triangles);

        if (!isClosed(getTriangles()))
        {
            throw new IllegalArgumentException(ErrorMessages.POLYHEDRON_NOT_CLOSED_MESSAGE);
        }
    }

    /**
     * Creates a polyhedron from an array of triangles.
     *
     * @param triangles the array of triangles used to create the polyhedron
     * @precondition triangles is not null, connected and forms a closed triangle mesh.
     * @postcondition A new Polyhedron object is created.
     */
    public Polyhedron (Triangle[] triangles)
    {
        this(Arrays.asList(triangles));
    }

    /**
     * Checks whether the given triangle mesh is closed.
     * A mesh is closed if every undirected edge is used exactly twice.
     *
     * @param triangles the list of triangles to check
     * @return true if the mesh is closed, otherwise false
     * @precondition triangles is not null and contains valid triangles.
     * @postcondition The closure validation result is returned.
     */
    private boolean isClosed (List<Triangle> triangles)
    {
        Map<EdgeKey, Integer> edgeCounter = new HashMap<>();

        for (Triangle triangle : triangles)
        {
            for (Edge edge : triangle.getEdges())
            {
                EdgeKey edgeKey = new EdgeKey(edge);

                int currentCount = edgeCounter.getOrDefault(edgeKey, GeneralConstants.INT_ZERO);
                edgeCounter.put(edgeKey, currentCount + ModelConstants.EDGE_COUNTER_INCREMENT);
            }
        }

        for (int count : edgeCounter.values())
        {
            if (count != ModelConstants.CLOSED_MESH_EDGE_USAGE_COUNT)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Return a new list of triangles sorted by area descending.
     *
     * @return a new list of triangles sorted by area in descending order
     * @precondition Triangles list may be empty.
     * @postcondition Returns sorted list (largest first).
     */
    public List<Triangle> trianglesSortedByAreaDesc ()
    {
        List<Triangle> copy = new ArrayList<>(getTriangles());
        copy.sort(Comparator.comparingDouble(Triangle::area).reversed());
        return copy;
    }

    /**
     * Compute surface area as sum of triangle areas.
     *
     * @return the total surface area of this polyhedron
     * @precondition Triangles represent the surface (no duplicates expected).
     * @postcondition Returns non-negative surface area.
     */
    public float surfaceArea ()
    {
        float sum = GeneralConstants.ZERO_FLOAT;

        for (Triangle t : getTriangles())
        {
            sum += t.area();
        }

        return sum;
    }

    /**
     * Compute volume using signed tetrahedron contributions relative to origin.
     * Works for closed meshes (also non-convex) if triangles are consistently oriented.
     *
     * @return the absolute volume of this polyhedron
     * @precondition Mesh is closed and oriented (consistent vertex winding).
     * @postcondition Returns absolute volume (non-negative).
     */
    public float volume ()
    {
        float signedSum = GeneralConstants.ZERO_FLOAT;
        Vertex reference = getTriangles().getFirst().getA();

        for (Triangle t : getTriangles())
        {
            if (!t.hasReference(reference))
            {
                signedSum += t.signedVolumeContribution(reference);
            }
        }

        return Math.abs(signedSum);
    }

    /**
     * Number of triangles.
     *
     * @return the number of triangles in this polyhedron
     * @precondition None.
     * @postcondition Returns integer >= 0.
     */
    public int triangleCount ()
    {
        return getTriangles().size();
    }

    /**
     * Represents an undirected edge key for counting edge usages in a mesh.
     * The direction of the edge is ignored for equality.
     *
     * @precondition The edge is not null.
     * @postcondition An edge key can be used in maps and sets.
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
         * @postcondition A new EdgeKey object is created from the edge vertices.
         */
        public EdgeKey (Edge edge)
        {
            this.start = edge.getStart();
            this.end = edge.getEnd();
        }

        /**
         * Returns a textual representation of this edge key.
         *
         * @return a formatted string containing the end and start vertices
         * @precondition None.
         * @postcondition A string representation of this edge key is returned.
         */
        @Override
        public String toString ()
        {
            return String.format(ModelConstants.EDGE_KEY_TO_STRING_FORMAT, this.end, this.start);
        }

        /**
         * Compares this edge key with another object.
         * Two edge keys are equal if they represent the same undirected edge.
         *
         * @param object the object to compare with this edge key
         * @return true if the object represents the same undirected edge, otherwise false
         * @precondition object may be null or any object.
         * @postcondition The equality result is returned.
         */
        @Override
        public boolean equals (Object object)
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
        public int hashCode ()
        {
            return start.hashCode() + end.hashCode();
        }
    }
}