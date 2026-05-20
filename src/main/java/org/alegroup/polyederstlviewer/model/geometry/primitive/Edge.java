package org.alegroup.polyederstlviewer.model.geometry.primitive;

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
}
