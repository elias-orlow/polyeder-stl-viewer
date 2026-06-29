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
 * Represents a polyhedron defined by a closed triangle mesh.
 * A polyhedron provides geometric analysis such as surface area,
 * volume computation, and triangle sorting.
 *
 * @precondition Triangles must form a closed mesh.
 * @postcondition A valid Polyhedron instance is created and provides analysis methods.
 */
public class Polyhedron extends Mesh
{

    /**
     * Creates a polyhedron from a list of triangles.
     *
     * @param triangles the list of triangles forming the polyhedron
     * @throws IllegalArgumentException if the triangle mesh is not closed
     * @precondition triangles != null AND triangles form a closed mesh
     * @postcondition A new Polyhedron instance is created
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
     * @param triangles the array of triangles forming the polyhedron
     * @precondition triangles != null AND triangles form a closed mesh
     * @postcondition A new Polyhedron instance is created
     */
    public Polyhedron (Triangle[] triangles)
    {
        this(Arrays.asList(triangles));
    }

    /**
     * Checks whether the triangle mesh is closed.
     * A mesh is closed if every undirected edge is used exactly twice.
     *
     * @param triangles the list of triangles to validate
     * @return true if the mesh is closed, otherwise false
     * @precondition triangles != null AND triangles contain valid edges
     * @postcondition A boolean indicating closure is returned
     */
    private boolean isClosed (List<Triangle> triangles)
    {

        Map<EdgeKey, Integer> edgeCounter = new HashMap<>();

        for (Triangle triangle : triangles)
        {
            for (Edge edge : triangle.getEdges())
            {

                EdgeKey key = new EdgeKey(edge);
                int currentCount = edgeCounter.getOrDefault(key, GeneralConstants.INT_ZERO);

                edgeCounter.put(key, currentCount + ModelConstants.EDGE_COUNTER_INCREMENT);
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
     * Returns a new list of triangles sorted by area in descending order.
     *
     * @return a list sorted by triangle area (largest first)
     * @precondition none
     * @postcondition A new sorted list is returned
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
     * @return the non-negative surface area
     * @precondition Triangles represent the full surface
     * @postcondition A valid area value is returned
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
     * @return the absolute volume of the polyhedron
     * @precondition Mesh is closed AND triangles are consistently oriented
     * @postcondition A non-negative volume is returned
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
     * @return the number of triangles
     * @precondition none
     * @postcondition integer >= 0
     */
    public int triangleCount ()
    {
        return getTriangles().size();
    }

    /**
     * Represents an undirected edge key for counting edge usages.
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
         * Returns a formatted string representation of this edge key.
         *
         * @return a string containing the vertices
         * @precondition none
         * @postcondition A non-null string is returned
         */
        @Override
        public String toString ()
        {
            return String.format(ModelConstants.EDGE_KEY_TO_STRING_FORMAT, end, start);
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

    // -------------------------------------------------------------------------
    //  JAVA FX SUPPORT
    // -------------------------------------------------------------------------

    /**
     * Converts the polyhedron into a JavaFX TriangleMesh.
     * Each triangle becomes one face in the mesh.
     *
     * @return a TriangleMesh representing the polyhedron
     * @throws IllegalStateException if the polyhedron contains no triangles
     * @precondition Polyhedron contains at least one triangle
     * @postcondition A valid TriangleMesh is returned
     */
    public TriangleMesh toTriangleMesh ()
    {

        if (getTriangles().isEmpty())
        {
            throw new IllegalStateException(ErrorMessages.POLYHEDRON_EMPTY);
        }

        TriangleMesh mesh = new TriangleMesh();

        mesh.getTexCoords().addAll(
                GeneralConstants.FIRST_INDEX,
                GeneralConstants.FIRST_INDEX
        );

        List<Float> pointList = new ArrayList<>();
        List<Integer> faceList = new ArrayList<>();

        int vertexIndex = GeneralConstants.FIRST_INDEX;

        for (Triangle t : getTriangles())
        {

            pointList.add(t.getA().getX());
            pointList.add(t.getA().getY());
            pointList.add(t.getA().getZ());

            pointList.add(t.getB().getX());
            pointList.add(t.getB().getY());
            pointList.add(t.getB().getZ());

            pointList.add(t.getC().getX());
            pointList.add(t.getC().getY());
            pointList.add(t.getC().getZ());

            faceList.add(vertexIndex);
            faceList.add(GeneralConstants.FIRST_INDEX);

            faceList.add(vertexIndex + 1);
            faceList.add(GeneralConstants.FIRST_INDEX);

            faceList.add(vertexIndex + 2);
            faceList.add(GeneralConstants.FIRST_INDEX);

            vertexIndex += 3;
        }

        float[] points = new float[pointList.size()];
        for (int i = GeneralConstants.FIRST_INDEX; i < pointList.size(); i++)
        {
            points[i] = pointList.get(i);
        }
        mesh.getPoints().addAll(points);

        int[] faces = new int[faceList.size()];
        for (int i = GeneralConstants.FIRST_INDEX; i < faceList.size(); i++)
        {
            faces[i] = faceList.get(i);
        }
        mesh.getFaces().addAll(faces);

        return mesh;
    }
}