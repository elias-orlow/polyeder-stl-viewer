package org.alegroup.polyederstlviewer.model.geometry.polygon;

import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vector3D;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.ArrayList;
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
    public float area ()
    {
        Vector3D a = new Vector3D(getA().getX(), getA().getY(), getA().getZ()).subtract(new Vector3D(getB().getX(), getB().getY(), getB().getZ()));
        Vector3D b = new Vector3D(getA().getX(), getA().getY(), getA().getZ()).subtract(new Vector3D(getC().getX(), getC().getY(), getC().getZ()));
        Vector3D cross = a.cross(b);
        return 0.5f * cross.magnitude();
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
     * @precondition Vertices and normal are in absolute coordinates.
     * @postcondition Returns signed volume contribution (translation invariant).
     */
    public float signedVolumeContribution(Vertex reference)
    {
        Vector3D A = new Vector3D(getA().getX(), getA().getY(), getA().getZ());
        Vector3D B = new Vector3D(getB().getX(), getB().getY(), getB().getZ());
        Vector3D C = new Vector3D(getC().getX(), getC().getY(), getC().getZ());

        Vector3D referenceVertex = new Vector3D(reference.getX(), reference.getY(), reference.getZ());

        Vector3D a = referenceVertex.subtract(A);

        Vector3D b = B.subtract(A);
        Vector3D c = C.subtract(A);

        float unsignedVolume = a.dot(b.cross(c)) / 6.0f; // das macht halt gar kein Sinn ?!
        float orientation = getNormalVector().dot(b.cross(c));

        return (orientation >= 0 ? unsignedVolume : -unsignedVolume);
    }

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
        }
        else return false;
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
