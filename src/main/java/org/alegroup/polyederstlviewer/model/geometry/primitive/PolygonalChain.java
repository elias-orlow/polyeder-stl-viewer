package org.alegroup.polyederstlviewer.model.geometry.primitive;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;

import java.util.*;

/**
 * Represents a polygonal chain consisting of sequentially connected edges.
 * A valid polygonal chain requires that the end vertex of each edge equals
 * the start vertex of the next edge. Instances of this class are immutable.
 */
public class PolygonalChain
{

    /**
     * Immutable list of edges forming the polygonal chain.
     */
    private final List<Edge> edges;

    /**
     * Creates a polygonal chain from a list of edges.
     *
     * @param edges the list of edges used to construct the polygonal chain
     * @throws IllegalArgumentException if the edges do not form a valid polygonal chain
     * @throws NullPointerException     if edges is null
     * @precondition edges != null AND edges must form a valid polygonal chain
     * @postcondition A new immutable PolygonalChain instance is created
     */
    public PolygonalChain (List<Edge> edges)
    {
        Objects.requireNonNull(edges, ErrorMessages.EDGES_LIST_NULL_MESSAGE);

        if (!isPolygonalChain(edges))
        {
            throw new IllegalArgumentException(ErrorMessages.POLYGONAL_CHAIN_INVALID_MESSAGE);
        }

        this.edges = new ArrayList<>(edges);
    }

    /**
     * Creates a polygonal chain from an array of edges.
     *
     * @param edges the array of edges used to construct the polygonal chain
     * @throws IllegalArgumentException if the edges do not form a valid polygonal chain
     * @throws NullPointerException     if edges is null
     * @precondition edges != null AND edges must form a valid polygonal chain
     * @postcondition A new immutable PolygonalChain instance is created
     */
    public PolygonalChain (Edge[] edges)
    {
        Objects.requireNonNull(edges, ErrorMessages.EDGES_ARRAY_NULL_MESSAGE);

        List<Edge> edgeList = new ArrayList<>(Arrays.asList(edges));

        if (!isPolygonalChain(edgeList))
        {
            throw new IllegalArgumentException(ErrorMessages.POLYGONAL_CHAIN_INVALID_MESSAGE);
        }

        this.edges = edgeList;
    }

    /**
     * Validates whether the given list of edges forms a polygonal chain.
     * A valid chain requires that each edge's end vertex matches the next edge's start vertex.
     *
     * @param edges the list of edges to validate
     * @return true if the edges form a valid polygonal chain, otherwise false
     * @precondition edges != null AND edges contain valid Edge objects
     * @postcondition A boolean indicating chain validity is returned
     */
    private boolean isPolygonalChain (List<Edge> edges)
    {
        for (int i = GeneralConstants.FIRST_INDEX;
             i < edges.size() - GeneralConstants.NEXT_INDEX_OFFSET;
             i++)
        {

            Edge current = edges.get(i);
            Edge next = edges.get(i + GeneralConstants.NEXT_INDEX_OFFSET);

            if (!current.getEnd().equals(next.getStart()))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an unmodifiable list of edges forming this polygonal chain.
     *
     * @return an unmodifiable list of edges
     * @precondition none
     * @postcondition A safe, immutable view of the edges is returned
     */
    public List<Edge> getEdges ()
    {
        return Collections.unmodifiableList(edges);
    }
}