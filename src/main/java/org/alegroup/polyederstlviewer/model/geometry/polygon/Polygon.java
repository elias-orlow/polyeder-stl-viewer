package org.alegroup.polyederstlviewer.model.geometry.polygon;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.constants.ModelConstants;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.PolygonalChain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a closed polygon based on a polygonal chain.
 * A polygon must consist of at least three connected edges and must be closed.
 *
 * @precondition The edges are not null, connected in order and form a closed shape.
 * @postcondition A polygon object can be created if the given edges form a valid polygon.
 */
public class Polygon extends PolygonalChain
{
    /**
     * Creates a polygon from a list of connected edges.
     *
     * @param edges the list of edges used to create the polygon
     * @throws IllegalArgumentException if the given edges do not form a closed polygon
     * @precondition edges is not null, contains at least three connected edges and is closed.
     * @postcondition A new Polygon object is created.
     */
    public Polygon (List<Edge> edges)
    {
        super(edges);

        if (!isPolygon(edges))
        {
            throw new IllegalArgumentException(ErrorMessages.POLYGON_NOT_CLOSED_MESSAGE);
        }
    }

    /**
     * Creates a polygon from an array of connected edges.
     *
     * @param edges the array of edges used to create the polygon
     * @throws NullPointerException     if edges is null
     * @throws IllegalArgumentException if the given edges do not form a closed polygon
     * @precondition edges is not null, contains at least three connected edges and is closed.
     * @postcondition A new Polygon object is created.
     */
    public Polygon (Edge[] edges)
    {
        this(Arrays.asList(Objects.requireNonNull(edges, ErrorMessages.EDGES_ARRAY_NULL_MESSAGE)));
    }

    /**
     * Checks whether the given edges form a valid polygon.
     * A valid polygon must have at least three edges and must be closed.
     *
     * @param edges the list of edges to check
     * @return true if the edges form a valid polygon, otherwise false
     * @precondition edges is not null and all edges are connected in order.
     * @postcondition The validation result is returned.
     */
    private boolean isPolygon (List<Edge> edges)
    {
        return hasAtLeastThreeEdges(edges) && isClosed(edges);
    }

    /**
     * Checks whether the given edge list contains the minimum number of edges required for a polygon.
     *
     * @param edges the list of edges to check
     * @return true if the list contains at least the required number of edges, otherwise false
     * @precondition edges is not null.
     * @postcondition The result of the size check is returned.
     */
    private boolean hasAtLeastThreeEdges (List<Edge> edges)
    {
        return edges.size() >= ModelConstants.MIN_POLYGON_EDGE_COUNT;
    }

    /**
     * Checks whether the polygonal chain is closed.
     * A polygonal chain is closed if the end vertex of the last edge equals
     * the start vertex of the first edge.
     *
     * @param edges the list of edges to check
     * @return true if the polygonal chain is closed, otherwise false
     * @precondition edges is not null and contains at least one edge.
     * @postcondition The result of the closure check is returned.
     */
    private boolean isClosed (List<Edge> edges)
    {
        Edge firstEdge = edges.get(GeneralConstants.FIRST_INDEX);
        Edge lastEdge = edges.get(edges.size() - GeneralConstants.NEXT_INDEX_OFFSET);

        return lastEdge.getEnd().equals(firstEdge.getStart());
    }
}
