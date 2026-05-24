package org.alegroup.polyederstlviewer.model.geometry.mesh;

import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Mesh
{

    private final List<Triangle> triangles;

    public Mesh (List<Triangle> triangles)
    {
        if (!isMesh(triangles))
        {
            throw new IllegalArgumentException("The given triangles do not form a connected triangle mesh.");
        }

        this.triangles = List.copyOf(triangles);
    }

    public Mesh (Triangle[] triangles)
    {
        this(Arrays.asList(triangles));
    }

    private boolean isMesh (List<Triangle> triangles)
    {
        return hasTriangles(triangles)
                && hasNoNullTriangles(triangles)
                && isConnected(triangles);
    }

    private boolean hasTriangles (List<Triangle> triangles)
    {
        return triangles != null && !triangles.isEmpty();
    }

    private boolean hasNoNullTriangles (List<Triangle> triangles)
    {
        for (Triangle triangle : triangles)
        {
            if (triangle == null)
            {
                return false;
            }
        }

        return true;
    }

    private boolean isConnected (List<Triangle> triangles)
    {
        Set<Triangle> visitedTriangles = new HashSet<>();
        Queue<Triangle> queue = new ArrayDeque<>();

        Triangle startTriangle = triangles.get(0);

        visitedTriangles.add(startTriangle);
        queue.add(startTriangle);

        while (!queue.isEmpty())
        {
            Triangle currentTriangle = queue.poll();

            for (Triangle otherTriangle : triangles)
            {
                if (!visitedTriangles.contains(otherTriangle)
                        && shareEdge(currentTriangle, otherTriangle))
                {
                    visitedTriangles.add(otherTriangle);
                    queue.add(otherTriangle);
                }
            }
        }

        return visitedTriangles.size() == triangles.size();
    }

    private boolean shareEdge (Triangle firstTriangle, Triangle secondTriangle)
    {
        for (Edge firstEdge : firstTriangle.getEdges())
        {
            for (Edge secondEdge : secondTriangle.getEdges())
            {
                if (isSameUndirectedEdge(firstEdge, secondEdge))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isSameUndirectedEdge (Edge firstEdge, Edge secondEdge)
    {
        boolean sameDirection =
                firstEdge.getStart().equals(secondEdge.getStart())
                        && firstEdge.getEnd().equals(secondEdge.getEnd());

        boolean oppositeDirection =
                firstEdge.getStart().equals(secondEdge.getEnd())
                        && firstEdge.getEnd().equals(secondEdge.getStart());

        return sameDirection || oppositeDirection;
    }

    public List<Triangle> getTriangles ()
    {
        return triangles;
    }

    public int getTriangleCount ()
    {
        return triangles.size();
    }
}