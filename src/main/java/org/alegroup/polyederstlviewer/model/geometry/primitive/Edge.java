package org.alegroup.polyederstlviewer.model.geometry.primitive;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;

import java.util.Objects;

/**
 * Represents an immutable edge defined by a start and an end vertex.
 * Edges are fundamental geometric primitives used in polygonal chains
 * and mesh structures.
 */
public class Edge
{

    /**
     * The start vertex of the edge.
     */
    private final Vertex start;

    /**
     * The end vertex of the edge.
     */
    private final Vertex end;

    /**
     * Creates a new edge with the given start and end vertices.
     *
     * @param start the start vertex of the edge
     * @param end   the end vertex of the edge
     * @throws NullPointerException if start or end is null
     * @precondition start != null AND end != null
     * @postcondition A new immutable Edge instance is created
     */
    public Edge (Vertex start, Vertex end)
    {
        this.start = Objects.requireNonNull(start, ErrorMessages.EDGE_START_NULL_MESSAGE);
        this.end = Objects.requireNonNull(end, ErrorMessages.EDGE_END_NULL_MESSAGE);
    }

    /**
     * Returns the start vertex of this edge.
     *
     * @return the start vertex
     * @precondition none
     * @postcondition A valid Vertex instance is returned
     */
    public Vertex getStart ()
    {
        return start;
    }

    /**
     * Returns the end vertex of this edge.
     *
     * @return the end vertex
     * @precondition none
     * @postcondition A valid Vertex instance is returned
     */
    public Vertex getEnd ()
    {
        return end;
    }

    /**
     * Determines whether this edge is equal to another object.
     * Two edges are equal if both their start and end vertices are equal.
     *
     * @param obj the object to compare with this edge
     * @return true if the object is an Edge with identical vertices, otherwise false
     * @precondition obj may be null or any object
     * @postcondition A boolean indicating equality is returned
     */
    @Override
    public boolean equals (Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Edge other = (Edge) obj;
        return this.start.equals(other.start) && this.end.equals(other.end);
    }

    /**
     * Computes the hash code for this edge based on its start and end vertices.
     *
     * @return the hash code of this edge
     * @precondition none
     * @postcondition A hash code consistent with equals() is returned
     */
    @Override
    public int hashCode ()
    {
        return Objects.hash(start, end);
    }
}