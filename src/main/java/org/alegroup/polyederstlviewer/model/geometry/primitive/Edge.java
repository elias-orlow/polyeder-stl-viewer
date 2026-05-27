package org.alegroup.polyederstlviewer.model.geometry.primitive;

import java.util.Objects;

public class Edge
{
    private final Vertex start;
    private final Vertex end;

    public Edge(Vertex start, Vertex end)
    {
        this.start = start;
        this.end = end;
    }

    public Vertex getStart ()
    {
        return start;
    }
    public Vertex getEnd ()
    {
        return end;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Edge other = (Edge) obj;
        return this.start.equals(other.start) && this.end.equals(other.end);
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash(getStart(), getEnd());
    }
}
