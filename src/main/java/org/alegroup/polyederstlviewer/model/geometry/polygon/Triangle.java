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
 * Represents a triangle defined by exactly three connected edges and a normal vector.
 * A triangle must be non-degenerate and its edges must form a closed polygon.
 *
 * @precondition Edges must be non-null, connected, closed, and define a non-degenerate triangle.
 * @postcondition A valid Triangle instance is created if all validation rules are fulfilled.
 */
public class Triangle extends Polygon
{

    /**
     * Threshold used to detect degenerate triangles.
     */
    private static final float EPSILON = ModelConstants.TRIANGLE_EPSILON;

    /**
     * The normal vector of the triangle.
     */
    private final Vector3D normalVector;

    /**
     * Creates a triangle from a list of edges and a normal vector.
     *
     * @param edges        the list of edges defining the triangle
     * @param normalVector the normal vector of the triangle
     * @throws IllegalArgumentException if edges do not form a valid triangle
     * @throws IllegalArgumentException if the triangle is degenerated
     * @throws IllegalArgumentException if the normal vector is null
     * @precondition edges != null AND edges contain exactly three connected edges
     * @postcondition A new Triangle instance is created
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
     * @param edges        the array of edges defining the triangle
     * @param normalVector the normal vector of the triangle
     * @precondition edges != null AND edges contain exactly three connected edges
     * @postcondition A new Triangle instance is created
     */
    public Triangle (Edge[] edges, Vector3D normalVector)
    {
        this(Arrays.asList(edges), normalVector);
    }

    /**
     * Checks whether the given list contains exactly three edges.
     *
     * @param edges the list of edges to check
     * @return true if edges contain exactly three elements, otherwise false
     * @precondition edges may be null
     * @postcondition A boolean indicating triangle edge count validity is returned
     */
    private boolean isTriangle (List<Edge> edges)
    {
        return edges != null && edges.size() == ModelConstants.TRIANGLE_EDGE_COUNT;
    }

    /**
     * Checks whether the triangle is degenerated.
     * A triangle is degenerated if its vertices are collinear or its area is nearly zero.
     *
     * @param edges the edges of the triangle
     * @return true if the triangle is degenerated, otherwise false
     * @precondition edges != null AND edges contain exactly three connected edges
     * @postcondition A boolean indicating degeneration is returned
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

        float crossLengthSquared =
                crossX * crossX +
                        crossY * crossY +
                        crossZ * crossZ;

        return crossLengthSquared <= EPSILON * EPSILON;
    }

    /**
     * Computes the area of the triangle.
     *
     * @return the non-negative area of the triangle
     * @precondition Triangle must be non-degenerate
     * @postcondition A valid area value is returned
     */
    public float area ()
    {
        Vector3D a = new Vector3D(getA().getX(), getA().getY(), getA().getZ())
                .subtract(new Vector3D(getB().getX(), getB().getY(), getB().getZ()));

        Vector3D b = new Vector3D(getA().getX(), getA().getY(), getA().getZ())
                .subtract(new Vector3D(getC().getX(), getC().getY(), getC().getZ()));

        Vector3D cross = a.cross(b);

        return ModelConstants.TRIANGLE_AREA_FACTOR * cross.magnitude();
    }

    /**
     * Computes the signed volume contribution of this triangle using a reference vertex.
     *
     * @param reference the reference vertex
     * @return the signed volume contribution
     * @precondition reference != null AND vertices are in absolute coordinates
     * @postcondition A signed volume value is returned
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

        float unsignedVolume = a.dot(b.cross(c)) / ModelConstants.SIGNED_VOLUME_DIVISOR;
        float orientation = getNormalVector().dot(b.cross(c));

        return (orientation >= GeneralConstants.ZERO_FLOAT ? unsignedVolume : -unsignedVolume);
    }

    /**
     * Checks whether the given reference vertex is part of this triangle.
     *
     * @param reference the vertex to check
     * @return true if the vertex is part of the triangle, otherwise false
     * @precondition reference may be null
     * @postcondition A boolean indicating membership is returned
     */
    public boolean hasReference (Vertex reference)
    {

        List<Vertex> vertices = new ArrayList<>();

        for (Edge e : getEdges())
        {
            vertices.add(e.getStart());
            vertices.add(e.getEnd());
        }

        return vertices.contains(reference);
    }

    /**
     * Returns a formatted string representation of this triangle.
     *
     * @return a string containing vertices, normal vector, and area
     * @precondition none
     * @postcondition A non-null string is returned
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
     * Returns the first vertex of the triangle.
     */
    public Vertex getA ()
    {
        return getEdges().get(GeneralConstants.FIRST_INDEX).getStart();
    }

    /**
     * Returns the second vertex of the triangle.
     */
    public Vertex getB ()
    {
        return getEdges().get(GeneralConstants.FIRST_INDEX).getEnd();
    }

    /**
     * Returns the third vertex of the triangle.
     */
    public Vertex getC ()
    {
        return getEdges().get(GeneralConstants.SECOND_INDEX).getEnd();
    }

    /**
     * Returns the normal vector of the triangle.
     */
    public Vector3D getNormalVector ()
    {
        return normalVector;
    }
}