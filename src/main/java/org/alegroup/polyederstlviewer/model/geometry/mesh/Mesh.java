package org.alegroup.polyederstlviewer.model.geometry.mesh;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Represents a triangle mesh consisting of connected triangles.
 * A mesh is valid if it contains at least one triangle, contains no null triangles
 * and all triangles are connected through shared edges.
 *
 * @precondition The triangle list is not null, contains no null values and forms a connected mesh.
 * @postcondition A mesh object can be created if all validation rules are fulfilled.
 */
public class Mesh
{
    private final List<Triangle> triangles;

    /**
     * Creates a mesh from a list of triangles.
     *
     * @param triangles the list of triangles used to create the mesh
     * @throws IllegalArgumentException if the given triangles do not form a connected triangle mesh
     * @precondition triangles is not null, not empty, contains no null triangles and is connected.
     * @postcondition A new Mesh object with the given triangles is created.
     */
    public Mesh (List<Triangle> triangles)
    {
        if (!isMesh(triangles))
        {
            throw new IllegalArgumentException(ErrorMessages.MESH_NOT_CONNECTED_MESSAGE);
        }

        this.triangles = List.copyOf(triangles);
    }

    /**
     * Creates a mesh from an array of triangles.
     *
     * @param triangles the array of triangles used to create the mesh
     * @precondition triangles is not null, not empty, contains no null triangles and is connected.
     * @postcondition A new Mesh object with the given triangles is created.
     */
    public Mesh (Triangle[] triangles)
    {
        this(Arrays.asList(triangles));
    }

    /**
     * Checks whether the given triangle list forms a valid mesh.
     * A valid mesh must contain triangles, must not contain null triangles
     * and must be connected.
     *
     * @param triangles the list of triangles to check
     * @return true if the triangles form a valid mesh, otherwise false
     * @precondition triangles may be null.
     * @postcondition The mesh validation result is returned.
     */
    private boolean isMesh (List<Triangle> triangles)
    {
        return hasTriangles(triangles)
                && hasNoNullTriangles(triangles)
                && isConnected(triangles);
    }

    /**
     * Checks whether the given triangle list exists and contains at least one triangle.
     *
     * @param triangles the list of triangles to check
     * @return true if the list is not null and not empty, otherwise false
     * @precondition triangles may be null.
     * @postcondition The result of the triangle existence check is returned.
     */
    private boolean hasTriangles (List<Triangle> triangles)
    {
        return triangles != null && !triangles.isEmpty();
    }

    /**
     * Checks whether the given triangle list contains no null values.
     *
     * @param triangles the list of triangles to check
     * @return true if all triangles are not null, otherwise false
     * @precondition triangles is not null.
     * @postcondition The null validation result is returned.
     */
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

    /**
     * Checks whether all triangles in the given list are connected.
     * The method performs a breadth-first search starting with the first triangle.
     * Two triangles are considered connected if they share at least one edge.
     *
     * @param triangles the list of triangles to check
     * @return true if all triangles are connected, otherwise false
     * @precondition triangles is not null, not empty and contains no null triangles.
     * @postcondition The connectivity validation result is returned.
     */
    private boolean isConnected (List<Triangle> triangles)
    {
        Set<Triangle> visitedTriangles = new HashSet<>();
        Queue<Triangle> queue = new ArrayDeque<>();

        Triangle startTriangle = triangles.get(GeneralConstants.FIRST_INDEX);

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

    /**
     * Checks whether two triangles share at least one edge.
     *
     * @param firstTriangle  the first triangle to compare
     * @param secondTriangle the second triangle to compare
     * @return true if both triangles share an edge, otherwise false
     * @precondition firstTriangle and secondTriangle are not null.
     * @postcondition The edge sharing result is returned.
     */
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

    /**
     * Checks whether two edges are equal without considering their direction.
     * This means that two edges are equal if they have the same start and end vertices,
     * or if the start and end vertices are swapped.
     *
     * @param firstEdge  the first edge to compare
     * @param secondEdge the second edge to compare
     * @return true if both edges represent the same undirected edge, otherwise false
     * @precondition firstEdge and secondEdge are not null.
     * @postcondition The undirected edge comparison result is returned.
     */
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

    /**
     * Returns the triangles of this mesh.
     *
     * @return the triangles of this mesh
     * @precondition None.
     * @postcondition The triangle list of this mesh is returned.
     */
    public List<Triangle> getTriangles ()
    {
        return triangles;
    }

    /**
     * Returns the number of triangles in this mesh.
     *
     * @return the number of triangles
     * @precondition None.
     * @postcondition The triangle count of this mesh is returned.
     */
    public int getTriangleCount ()
    {
        return triangles.size();
    }
}