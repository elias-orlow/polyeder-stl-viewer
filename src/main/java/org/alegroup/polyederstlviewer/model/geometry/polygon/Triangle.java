package org.alegroup.polyederstlviewer.model.geometry.polygon;

import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vector3D;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.Arrays;
import java.util.List;

public class Triangle extends Polygon
{

    private static final float EPSILON = 1e-6f;

    private final Vector3D normalVector;

    public Triangle (List<Edge> edges, Vector3D normalVector)
    {
        super(edges);

        if (!isTriangle(edges))
        {
            throw new IllegalArgumentException("A triangle must have exactly three edges.");
        }

        if (isDegenerated(edges))
        {
            throw new IllegalArgumentException("A triangle must not be degenerated.");
        }

        if (normalVector == null)
        {
            throw new IllegalArgumentException("A triangle needs a normal vector.");
        }

        this.normalVector = normalVector;
    }

    public Triangle (Edge[] edges, Vector3D normalVector)
    {
        this(Arrays.asList(edges), normalVector);
    }

    private boolean isTriangle (List<Edge> edges)
    {
        return edges != null && edges.size() == 3;
    }

    private boolean isDegenerated (List<Edge> edges)
    {
        Vertex a = edges.get(0).getStart();
        Vertex b = edges.get(0).getEnd();
        Vertex c = edges.get(1).getEnd();

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
     * @precondition Vertices define a non-degenerate triangle (area >= 0).
     * @postcondition Returns non-negative area.
     */
    public float area()
    {
        Vector3D a = new Vector3D(getA().getX(), getA().getY(), getA().getZ()).subtract(new Vector3D(getB().getX(), getA().getY(), getA().getZ()));
        Vector3D b = new Vector3D(getB().getX(), getB().getY(), getB().getZ()).subtract(new Vector3D(getB().getX(), getB().getY(), getB().getZ()));
        Vector3D cross = a.cross(b);
        return (float)(0.5 * cross.magnitude());
    }

    /**
     * Signed volume contribution of the tetrahedron formed by triangle and origin.
     * Formula: (1/6) * (v0 dot (v1 x v2))
     *
     * @precondition Vertices are in absolute coordinates.
     * @postcondition Returns signed volume contribution (can be negative).
     */
    public double signedVolumeContribution()
    {
        Vector3D p0 = new Vector3D(getA().getX(), getA().getY(), getA().getZ());
        Vector3D p1 = new Vector3D(getB().getX(), getB().getY(), getB().getZ());
        Vector3D p2 = new Vector3D(getC().getX(), getC().getY(), getC().getZ());
        double triple = p0.dot(p1.cross(p2));
        return triple / 6.0;
    }

    @Override
    public String toString()
    {
        return "Triangle {\n" +
                "  A = " + getA().getX() + " | " + getA().getY() + " | " + getA().getZ() + ",\n" +
                "  B = " + getB().getX() + " | " + getB().getY() + " | " + getB().getZ() + ",\n" +
                "  C = " + getC().getX() + " | " + getC().getY() + " | " + getC().getZ() + ",\n" +
                "  normal = " + getNormalVector() + ",\n" +
                "  area = " + area() + "\n" +
                "}";
    }


    public Vertex getA ()
    {
        return getEdges().get(0).getStart();
    }

    public Vertex getB ()
    {
        return getEdges().get(0).getEnd();
    }

    public Vertex getC ()
    {
        return getEdges().get(1).getEnd();
    }

    public Vector3D getNormalVector ()
    {
        return normalVector;
    }
}
