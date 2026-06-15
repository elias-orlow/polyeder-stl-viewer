package org.alegroup.polyederstlviewer.model.geometry.mesh;

import javafx.scene.shape.TriangleMesh;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.*;

/**
 * Represents a 3D polyhedron composed of triangles.
 * Provides geometric analysis (surface area, volume) and conversion to JavaFX TriangleMesh.
 *
 * @precondition The provided triangles form a closed, consistently oriented triangle mesh.
 * @postcondition A valid Polyhedron instance is created and ready for geometric evaluation.
 */
public class Polyhedron extends Mesh
{

    /**
     * Creates a Polyhedron from a list of triangles.
     *
     * @param triangles list of triangles forming a closed mesh
     * @throws IllegalArgumentException if the triangle mesh is not closed
     *
     * @precondition triangles != null, contains at least 4 triangles, forms a closed mesh
     * @postcondition A new Polyhedron instance is created
     */
    public Polyhedron(List<Triangle> triangles)
    {
        super(triangles);

        if (!isClosed(getTriangles()))
        {
            throw new IllegalArgumentException("A polyhedron must be a closed triangle mesh.");
        }
    }

    /**
     * Creates a Polyhedron from an array of triangles.
     *
     * @param triangles array of triangles forming a closed mesh
     * @throws IllegalArgumentException if the triangle mesh is not closed
     *
     * @precondition triangles != null
     * @postcondition Delegates to the list-based constructor
     */
    public Polyhedron(Triangle[] triangles)
    {
        this(Arrays.asList(triangles));
    }

    /**
     * Checks whether the triangle mesh is closed.
     * A closed mesh has each edge exactly twice (once per adjacent triangle).
     *
     * @param triangles list of triangles to validate
     * @return true if the mesh is closed, false otherwise
     *
     * @precondition triangles != null
     * @postcondition Returns whether the mesh satisfies the closure condition
     */
    private boolean isClosed(List<Triangle> triangles)
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
     * Returns a new list of triangles sorted by descending area.
     *
     * @return sorted list of triangles (largest area first)
     *
     * @precondition none
     * @postcondition Returned list is a copy and does not modify internal state
     */
    public List<Triangle> trianglesSortedByAreaDesc()
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
    public float surfaceArea()
    {
        float sum = 0.0f;
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
    public float volume()
    {
        float signedSum = 0.0f;
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
    public int triangleCount()
    {
        return getTriangles().size();
    }

    /**
     * Helper class used to uniquely identify edges independent of direction.
     */
    private static class EdgeKey
    {
        /** Start vertex of the edge. */
        private final Vertex start;

        /** End vertex of the edge. */
        private final Vertex end;

        /**
         * Creates an EdgeKey from an edge.
         *
         * @param edge the edge to wrap
         *
         * @precondition edge != null
         * @postcondition EdgeKey stores start and end vertices
         */
        public EdgeKey(Edge edge)
        {
            this.start = edge.getStart();
            this.end = edge.getEnd();
        }

        @Override
        public String toString()
        {
            return this.end + "" + this.start;
        }

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

        @Override
        public int hashCode()
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
