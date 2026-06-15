package org.alegroup.polyederstlviewer.model.geometry.polygon;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.constants.ModelConstants;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vector3D;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a triangle as a special polygon with exactly three edges.
 * A triangle has a normal vector and must not be degenerated.
 *
 * @precondition The edges are not null, connected, closed and define a non-degenerate triangle.
 * @postcondition A triangle object can be created if all validation rules are fulfilled.
 */
public class Triangle extends Polygon
{
    private static final float EPSILON = ModelConstants.TRIANGLE_EPSILON;

    private final Vector3D normalVector;

    /**
     * Creates a triangle from a list of edges and a normal vector.
     *
     * @param edges        the list of edges used to create the triangle
     * @param normalVector the normal vector of the triangle
     * @throws IllegalArgumentException if the edges do not form a valid triangle
     * @throws IllegalArgumentException if the triangle is degenerated
     * @throws IllegalArgumentException if the normal vector is null
     * @precondition edges is not null, contains exactly three connected edges and is not degenerated.
     * @postcondition A new Triangle object with the given edges and normal vector is created.
     */
    public Triangle (List<Edge> edges, Vector3D normalVector)
    {
        super(edges);

        if (!isTriangle(edges))
        {
            throw new IllegalArgumentException(ErrorMessages.TRIANGLE_EDGE_COUNT_MESSAGE);
        }

        if (isDegenerated(edges))
        {
            throw new IllegalArgumentException(ErrorMessages.TRIANGLE_DEGENERATED_MESSAGE);
        }

        if (normalVector == null)
        {
            throw new IllegalArgumentException(ErrorMessages.TRIANGLE_NORMAL_VECTOR_NULL_MESSAGE);
        }

        this.normalVector = normalVector;
    }

    /**
     * Creates a triangle from an array of edges and a normal vector.
     *
     * @param edges        the array of edges used to create the triangle
     * @param normalVector the normal vector of the triangle
     * @precondition edges is not null, contains exactly three connected edges and is not degenerated.
     * @postcondition A new Triangle object with the given edges and normal vector is created.
     */
    public Triangle (Edge[] edges, Vector3D normalVector)
    {
        this(Arrays.asList(edges), normalVector);
    }

    /**
     * Checks whether the given edge list contains exactly three edges.
     *
     * @param edges the list of edges to check
     * @return true if the edge list is not null and contains exactly three edges, otherwise false
     * @precondition edges may be null.
     * @postcondition The triangle edge count validation result is returned.
     */
    private boolean isTriangle (List<Edge> edges)
    {
        return edges != null && edges.size() == ModelConstants.TRIANGLE_EDGE_COUNT;
    }

    /**
     * Checks whether the triangle is degenerated.
     * A triangle is degenerated if its three vertices are collinear or if the calculated area is almost zero.
     *
     * @param edges the edges of the triangle
     * @return true if the triangle is degenerated, otherwise false
     * @precondition edges is not null and contains exactly three connected edges.
     * @postcondition The degeneration validation result is returned.
     */
    private boolean isDegenerated (List<Edge> edges)
    {
        Vertex a = edges.get(GeneralConstants.FIRST_INDEX).getStart();
        Vertex b = edges.get(GeneralConstants.FIRST_INDEX).getEnd();
        Vertex c = edges.get(GeneralConstants.SECOND_INDEX).getEnd();

        float abX = b.getX() - a.getX();
        float abY = b.getY() - a.getY();
        float abZ = b.getZ() - a.getZ();

        float acX = c.getX() - a.getX();
        float acY = c.getY() - a.getY();
        float acZ = c.getZ() - a.getZ();

        float crossX = abY * acZ - abZ * acY;
        float crossY = abZ * acX - abX * acZ;
        float crossZ = abX * acY - abY * acX;

        float crossLengthSquared = crossX * crossX
                + crossY * crossY
                + crossZ * crossZ;

        return crossLengthSquared <= EPSILON * EPSILON;
    }

    /**
     * Compute the area of the triangle.
     *
     * @return the area of the triangle
     * @precondition Vertices define a non-degenerate triangle (area >= 0).
     * @postcondition Returns non-negative area.
     */
    public float area ()
    {
        Vector3D a = new Vector3D(getA().getX(), getA().getY(), getA().getZ()).subtract(new Vector3D(getB().getX(), getB().getY(), getB().getZ()));
        Vector3D b = new Vector3D(getA().getX(), getA().getY(), getA().getZ()).subtract(new Vector3D(getC().getX(), getC().getY(), getC().getZ()));
        Vector3D cross = a.cross(b);
        return ModelConstants.TRIANGLE_AREA_FACTOR * cross.magnitude();
    }

//    /**
//     * Signed volume contribution of the tetrahedron formed by triangle and origin.
//     * Formula: (1/6) * (v0 dot (v1 x v2))
//     *
//     * @precondition Vertices are in absolute coordinates.
//     * @postcondition Returns signed volume contribution (can be negative).
//     */
//    public float signedVolumeContribution()
//    {
//        Vector3D A = new Vector3D(getA().getX(), getA().getY(), getA().getZ());
//        Vector3D B = new Vector3D(getB().getX(), getB().getY(), getB().getZ());
//        Vector3D C = new Vector3D(getC().getX(), getC().getY(), getC().getZ());
//
//        Vector3D b = B.subtract(A);
//        Vector3D c = C.subtract(A);
//
//        return b.cross(c).dot(A) / 6.0f;
//    }

    /**
     * Signed volume contribution using the STL normal to determine orientation.
     *
     * @param reference the reference vertex used for the volume calculation
     * @return the signed volume contribution of this triangle
     * @precondition Vertices and normal are in absolute coordinates.
     * @postcondition Returns signed volume contribution (translation invariant).
     */
    public float signedVolumeContribution (Vertex reference)
    {
        Vector3D A = new Vector3D(getA().getX(), getA().getY(), getA().getZ());
        Vector3D B = new Vector3D(getB().getX(), getB().getY(), getB().getZ());
        Vector3D C = new Vector3D(getC().getX(), getC().getY(), getC().getZ());

        Vector3D referenceVertex = new Vector3D(reference.getX(), reference.getY(), reference.getZ());

        Vector3D a = referenceVertex.subtract(A);

        Vector3D b = B.subtract(A);
        Vector3D c = C.subtract(A);

        float unsignedVolume = a.dot(b.cross(c)) / ModelConstants.SIGNED_VOLUME_DIVISOR; // das macht halt gar kein Sinn ?!
        float orientation = getNormalVector().dot(b.cross(c));

        return (orientation >= GeneralConstants.ZERO_FLOAT ? unsignedVolume : -unsignedVolume);
    }

    /**
     * Checks whether the given reference vertex is part of this triangle.
     *
     * @param reference the reference vertex to search for
     * @return true if the reference vertex is part of this triangle, otherwise false
     * @precondition reference may be null.
     * @postcondition The result of the reference check is returned.
     */
    public boolean hasReference (Vertex reference)
    {
        boolean hasReference = false;
        List<Edge> edges = getEdges();
        List<Vertex> vertices = new ArrayList<>();

        for (Edge e : edges)
        {
            vertices.add(e.getStart());
            vertices.add(e.getEnd());
        }

        if (vertices.contains(reference))
        {
            return true;
        } else return false;
    }

    /**
     * Returns a textual representation of this triangle.
     *
     * @return a formatted string containing the vertices, normal vector and area of this triangle
     * @precondition None.
     * @postcondition A string representation of this triangle is returned.
     */
    @Override
    public String toString ()
    {
        return String.format(
                ModelConstants.TRIANGLE_TO_STRING_FORMAT,
                getA().getX(), getA().getY(), getA().getZ(),
                getB().getX(), getB().getY(), getB().getZ(),
                getC().getX(), getC().getY(), getC().getZ(),
                getNormalVector(),
                area()
        );
    }

    /**
     * Returns the first vertex of this triangle.
     *
     * @return the first vertex of this triangle
     * @precondition The triangle contains valid edges.
     * @postcondition The first vertex of this triangle is returned.
     */
    public Vertex getA ()
    {
        return getEdges().get(GeneralConstants.FIRST_INDEX).getStart();
    }

    /**
     * Returns the second vertex of this triangle.
     *
     * @return the second vertex of this triangle
     * @precondition The triangle contains valid edges.
     * @postcondition The second vertex of this triangle is returned.
     */
    public Vertex getB ()
    {
        return getEdges().get(GeneralConstants.FIRST_INDEX).getEnd();
    }

    /**
     * Returns the third vertex of this triangle.
     *
     * @return the third vertex of this triangle
     * @precondition The triangle contains valid edges.
     * @postcondition The third vertex of this triangle is returned.
     */
    public Vertex getC ()
    {
        return getEdges().get(GeneralConstants.SECOND_INDEX).getEnd();
    }

    /**
     * Returns the normal vector of this triangle.
     *
     * @return the normal vector of this triangle
     * @precondition None.
     * @postcondition The normal vector of this triangle is returned.
     */
    public Vector3D getNormalVector ()
    {
        return normalVector;
    }
}
