package org.alegroup.polyederstlviewer.model.geometry.polygon;

import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vector3D;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.Arrays;
import java.util.List;

public class Triangle extends Polygon {

    private static final float EPSILON = 1e-6f;

    private final Vector3D normalVector;

    public Triangle(List<Edge> edges, Vector3D normalVector) {
        super(edges);

        if (!isTriangle(edges)) {
            throw new IllegalArgumentException("A triangle must have exactly three edges.");
        }

        if (isDegenerated(edges)) {
            throw new IllegalArgumentException("A triangle must not be degenerated.");
        }

        if (normalVector == null) {
            throw new IllegalArgumentException("A triangle needs a normal vector.");
        }

        this.normalVector = normalVector;
    }

    public Triangle(Edge[] edges, Vector3D normalVector) {
        this(Arrays.asList(edges), normalVector);
    }

    private boolean isTriangle(List<Edge> edges) {
        return edges != null && edges.size() == 3;
    }

    private boolean isDegenerated(List<Edge> edges) {
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

    public Vertex getA() {
        return getEdges().get(0).getStart();
    }

    public Vertex getB() {
        return getEdges().get(0).getEnd();
    }

    public Vertex getC() {
        return getEdges().get(1).getEnd();
    }

    public Vector3D getNormalVector() {
        return normalVector;
    }
}
