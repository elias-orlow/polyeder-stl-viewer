package org.alegroup.polyederstlviewer.model.geometry.mesh;

import javafx.scene.shape.TriangleMesh;
import org.alegroup.polyederstlviewer.constants.ErrorMessages;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.constants.ModelConstants;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.*;

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
     * Computes the total surface area of the polyhedron.
     *
     * @return non-negative surface area
     *
     * @precondition Triangles represent the surface of the polyhedron
     * @postcondition Returns sum of all triangle areas
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
     * Computes the volume of the polyhedron using signed tetrahedron contributions.
     *
     * @return absolute volume of the polyhedron
     *
     * @precondition Mesh is closed and triangles are consistently oriented
     * @postcondition Returns non-negative volume
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
     * Returns the number of triangles in the polyhedron.
     *
     * @return number of triangles
     *
     * @precondition none
     * @postcondition integer >= 0
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
        /** Start vertex of the edge. */
        private final Vertex start;

        /** End vertex of the edge. */
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

    // -------------------------------------------------------------------------
    //  JAVA FX SUPPORT FOR TASK 4
    // -------------------------------------------------------------------------

    /**
     * Converts the entire polyhedron into a JavaFX TriangleMesh.
     * Each triangle becomes one face in the mesh.
     *
     * @return TriangleMesh representing the polyhedron
     * @throws IllegalStateException if the polyhedron contains no triangles
     *
     * @precondition Polyhedron contains at least one triangle
     * @postcondition A valid TriangleMesh is created with points, faces and dummy texture coordinates
     */
    public TriangleMesh toTriangleMesh()
    {
        if (getTriangles().isEmpty())
        {
            throw new IllegalStateException("Cannot create a mesh from an empty polyhedron.");
        }

        TriangleMesh mesh = new TriangleMesh();

        // JavaFX requires at least one texture coordinate
        mesh.getTexCoords().addAll(0, 0);

        // Collect all unique vertices
        Map<Vertex, Integer> vertexIndexMap = new LinkedHashMap<>();
        List<Float> pointList = new ArrayList<>();

        for (Triangle t : getTriangles())
        {
            List<Vertex> vertices = List.of(t.getA(), t.getB(), t.getC());

            for (Vertex v : vertices)
            {
                if (!vertexIndexMap.containsKey(v))
                {
                    vertexIndexMap.put(v, vertexIndexMap.size());
                    pointList.add(v.getX());
                    pointList.add(v.getY());
                    pointList.add(v.getZ());
                }
            }
        }

        // Convert points to float[]
        float[] points = new float[pointList.size()];
        for (int i = 0; i < pointList.size(); i++)
        {
            points[i] = pointList.get(i);
        }
        mesh.getPoints().addAll(points);

        // Build faces (triangle indices)
        List<Integer> faceList = new ArrayList<>();

        for (Triangle t : getTriangles())
        {
            int a = vertexIndexMap.get(t.getA());
            int b = vertexIndexMap.get(t.getB());
            int c = vertexIndexMap.get(t.getC());

            // JavaFX requires vertexIndex/texCoordIndex pairs
            faceList.add(a); faceList.add(0);
            faceList.add(b); faceList.add(0);
            faceList.add(c); faceList.add(0);
        }

        // Convert faces to int[]
        int[] faces = new int[faceList.size()];
        for (int i = 0; i < faceList.size(); i++)
        {
            faces[i] = faceList.get(i);
        }
        mesh.getFaces().addAll(faces);

        return mesh;
    }
}
