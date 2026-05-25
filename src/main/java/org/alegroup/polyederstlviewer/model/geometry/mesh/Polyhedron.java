package org.alegroup.polyederstlviewer.model.geometry.mesh;

import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.*;

/**
 * Polyhedron represented as a collection of triangles.
 *
 * @precondition Triangles form a closed mesh (for correct volume).
 * @postcondition Polyhedron instance created and provides analysis methods.
 */
public class Polyhedron extends Mesh
{

    public Polyhedron (List<Triangle> triangles)
    {
        super(triangles);

        if (!isClosed(getTriangles()))
        {
            throw new IllegalArgumentException("A polyhedron must be a closed triangle mesh.");
        }
    }

    public Polyhedron (Triangle[] triangles)
    {
        this(Arrays.asList(triangles));
    }

    private boolean isClosed (List<Triangle> triangles)
    {
        Map<EdgeKey, Integer> edgeCounter = new HashMap<>();

        for (Triangle triangle : triangles)
        {
            for (Edge edge : triangle.getEdges())
            {
                EdgeKey edgeKey = new EdgeKey(edge);

                int currentCount = edgeCounter.getOrDefault(edgeKey, 0);
                edgeCounter.put(edgeKey, currentCount + 1);
            }
        }

        for (int count : edgeCounter.values())
        {
            if (count != 2)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Return a new list of triangles sorted by area descending.
     *
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
     * @precondition Triangles represent the surface (no duplicates expected).
     * @postcondition Returns non-negative surface area.
     */
    public double surfaceArea ()
    {
        double sum = 0.0;
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
     * @precondition Mesh is closed and oriented (consistent vertex winding).
     * @postcondition Returns absolute volume (non-negative).
     */
    public double volume ()
    {
        double signedSum = 0.0;
        for (Triangle t : getTriangles())
        {
            signedSum += t.signedVolumeContribution();
        }
        return Math.abs(signedSum);
    }

    /**
     * Number of triangles.
     *
     * @precondition None.
     * @postcondition Returns integer >= 0.
     */
    public int triangleCount ()
    {
        return getTriangles().size();
    }

    private static class EdgeKey
    {

        private final Vertex start;
        private final Vertex end;

        public EdgeKey (Edge edge)
        {
            this.start = edge.getStart();
            this.end = edge.getEnd();
        }

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

        @Override
        public int hashCode ()
        {
            return start.hashCode() + end.hashCode();
        }
    }
}
