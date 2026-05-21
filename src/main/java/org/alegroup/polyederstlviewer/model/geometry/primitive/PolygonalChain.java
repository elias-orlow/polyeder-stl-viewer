package org.alegroup.polyederstlviewer.model.geometry.primitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolygonalChain
{
    private List<Edge> edges;

    public PolygonalChain (List<Edge> edges)
    {
        if (!isPolygonalChain(edges))
        {
            throw new IllegalArgumentException("kein PolygonalChain");
        } else
        {
            this.edges = edges;
        }

    }

    public PolygonalChain (Edge[] edges)
    {
        if (!isPolygonalChain(new ArrayList<>(Arrays.asList(edges))))
        {
            throw new IllegalArgumentException("kein PolygonalChain");
        } else
        {
            this.edges.addAll(Arrays.asList(edges));
        }
    }

    private boolean isPolygonalChain (List<Edge> edges)
    {
        for (int i = 0; i < edges.size() - 1; i++)
        {
            if (!(edges.get(i).getEnd().equals(edges.get(i + 1).getStart())))
            {
                return false;
            }
        }
        return true;
    }

    public List<Edge> getEdges ()
    {
        return edges;
    }
}
