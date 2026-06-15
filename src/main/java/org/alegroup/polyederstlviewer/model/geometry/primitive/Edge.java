package org.alegroup.polyederstlviewer.model.geometry.primitive;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;

import java.util.Objects;

/**
 * Represents an edge between two vertices.
 * An edge has a start vertex and an end vertex.
 *
 * @precondition start and end are not null.
 * @postcondition An edge can be created between two vertices.
 */
public class Edge
{
    private final Vertex start;
    private final Vertex end;

    /**
     * Creates an edge with the given start and end vertices.
     *
     * @param start the start vertex of the edge
     * @param end the end vertex of the edge
     *
     * @precondition start and end are not null.
     * @postcondition A new Edge object with the given start and end vertices is created.
     *
     * @throws NullPointerException if start or end is null
     */
    public Edge(Vertex start, Vertex end)
    {
        this.start = Objects.requireNonNull(start, ErrorMessages.EDGE_START_NULL_MESSAGE);
        this.end = Objects.requireNonNull(end, ErrorMessages.EDGE_END_NULL_MESSAGE);
    }

    /**
     * Returns the start vertex of this edge.
     *
     * @return the start vertex
     *
     * @precondition None.
     * @postcondition The start vertex of this edge is returned.
     */
    public Vertex getStart()
    {
        return start;
    }

    /**
     * Returns the end vertex of this edge.
     *
     * @return the end vertex
     *
     * @precondition None.
     * @postcondition The end vertex of this edge is returned.
     */
    public Vertex getEnd()
    {
        return end;
    }

    /**
     * Compares this edge with another object.
     * Two edges are equal if they have the same start vertex and the same end vertex.
     *
     * @param obj the object to compare with this edge
     * @return true if the given object is equal to this edge, otherwise false
     *
     * @precondition obj may be null or any object.
     * @postcondition The equality result is returned.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Edge other = (Edge) obj;
        return this.start.equals(other.start) && this.end.equals(other.end);
    }

    /**
     * Calculates the hash code of this edge based on its start and end vertices.
     *
     * @return the hash code of this edge
     *
     * @precondition None.
     * @postcondition A hash code consistent with equals is returned.
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getStart(), getEnd());
    }
}