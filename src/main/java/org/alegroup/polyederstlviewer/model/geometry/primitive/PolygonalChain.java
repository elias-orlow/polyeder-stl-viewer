package org.alegroup.polyederstlviewer.model.geometry.primitive;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Collections;
import java.util.Objects;

/**
 * Represents a polygonal chain consisting of connected edges.
 * Each edge must end at the start vertex of the next edge.
 *
 * @precondition The edges are not null and contain valid Edge objects.
 * @postcondition A polygonal chain can be created if all edges are connected.
 */
public class PolygonalChain
{
    private final List<Edge> edges;

    /**
     * Creates a polygonal chain from a list of edges.
     *
     * @param edges the list of edges used to create the polygonal chain
     * @throws IllegalArgumentException if the given edges do not form a polygonal chain
     * @throws NullPointerException     if edges is null
     * @precondition edges is not null and all edges are connected in order.
     * @postcondition A new PolygonalChain object is created.
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
     * @param edges the array of edges used to create the polygonal chain
     * @throws IllegalArgumentException if the given edges do not form a polygonal chain
     * @throws NullPointerException     if edges is null
     * @precondition edges is not null and all edges are connected in order.
     * @postcondition A new PolygonalChain object is created.
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
     * Checks whether the given list of edges forms a valid polygonal chain.
     * A valid polygonal chain requires that the end vertex of each edge equals
     * the start vertex of the following edge.
     *
     * @param edges the list of edges to check
     * @return true if the edges form a polygonal chain, otherwise false
     * @precondition edges is not null and contains valid Edge objects.
     * @postcondition The validation result is returned.
     */
    private boolean isPolygonalChain (List<Edge> edges)
    {
        for (int i = GeneralConstants.FIRST_INDEX;
             i < edges.size() - GeneralConstants.NEXT_INDEX_OFFSET;
             i++)
        {
            if (!edges.get(i).getEnd().equals(edges.get(i + GeneralConstants.NEXT_INDEX_OFFSET).getStart()))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the edges of this polygonal chain.
     *
     * @return an unmodifiable list of edges of this polygonal chain
     * @precondition None.
     * @postcondition The edges of this polygonal chain are returned.
     */
    public List<Edge> getEdges ()
    {
        return Collections.unmodifiableList(edges);
    }
}
