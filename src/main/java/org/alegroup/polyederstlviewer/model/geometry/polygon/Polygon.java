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
 * Represents a closed polygon defined by a connected polygonal chain.
 * A polygon must consist of at least three edges and must be closed,
 * meaning the last edge's end vertex equals the first edge's start vertex.
 *
 * @precondition Edges must be non-null, connected in order, and form a closed shape.
 * @postcondition A valid Polygon instance is created if all validation rules are fulfilled.
 */
public class Polygon extends PolygonalChain
{

    /**
     * Creates a polygon from a list of connected edges.
     *
     * @param edges the list of edges defining the polygon
     * @throws IllegalArgumentException if the edges do not form a closed polygon
     * @precondition edges != null AND edges contain at least three connected edges AND edges form a closed polygon
     * @postcondition A new Polygon instance is created
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
     * @param edges the array of edges defining the polygon
     * @throws NullPointerException     if edges is null
     * @throws IllegalArgumentException if the edges do not form a closed polygon
     * @precondition edges != null AND edges contain at least three connected edges AND edges form a closed polygon
     * @postcondition A new Polygon instance is created
     */
    public Polygon (Edge[] edges)
    {
        this(Arrays.asList(Objects.requireNonNull(edges, ErrorMessages.EDGES_ARRAY_NULL_MESSAGE)));
    }

    /**
     * Determines whether the given edges form a valid polygon.
     * A valid polygon must have at least three edges and must be closed.
     *
     * @param edges the list of edges to validate
     * @return true if the edges form a valid polygon, otherwise false
     * @precondition edges != null AND edges contain valid Edge objects
     * @postcondition A boolean indicating polygon validity is returned
     */
    private boolean isPolygon (List<Edge> edges)
    {
        return hasAtLeastThreeEdges(edges) && isClosed(edges);
    }

    /**
     * Checks whether the given edge list contains at least the minimum number of edges
     * required to form a polygon.
     *
     * @param edges the list of edges to check
     * @return true if the list contains at least three edges, otherwise false
     * @precondition edges != null
     * @postcondition A boolean indicating edge count validity is returned
     */
    private boolean hasAtLeastThreeEdges (List<Edge> edges)
    {
        return edges.size() >= ModelConstants.MIN_POLYGON_EDGE_COUNT;
    }

    /**
     * Checks whether the polygonal chain is closed.
     * A polygon is closed if the end vertex of the last edge equals
     * the start vertex of the first edge.
     *
     * @param edges the list of edges to check
     * @return true if the polygonal chain is closed, otherwise false
     * @precondition edges != null AND edges contain at least one edge
     * @postcondition A boolean indicating closure is returned
     */
    private boolean isClosed (List<Edge> edges)
    {
        Edge firstEdge = edges.get(GeneralConstants.FIRST_INDEX);
        Edge lastEdge = edges.get(edges.size() - GeneralConstants.NEXT_INDEX_OFFSET);

        return lastEdge.getEnd().equals(firstEdge.getStart());
    }
}