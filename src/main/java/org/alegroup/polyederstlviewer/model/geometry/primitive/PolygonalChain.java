package org.alegroup.polyederstlviewer.model.geometry.primitive;

import java.util.ArrayList;
import java.util.Arrays;

public class PolygonalChain
{
    private ArrayList<Edge> edges;

    public PolygonalChain (ArrayList<Edge> edges)
    {
        if(!isPolygonalChain(edges)){
            throw new IllegalArgumentException("kein PolygonalChain");
        } else {
            this.edges = edges;
        }

    }

    public PolygonalChain(Edge[] edges)
    {
        if(!isPolygonalChain(new ArrayList<>(Arrays.asList(edges)))){
            throw new IllegalArgumentException("kein PolygonalChain");
        } else {
            this.edges.addAll(Arrays.asList(edges));
        }
    }

    private boolean isPolygonalChain(ArrayList<Edge> edges){
        for(int i = 0; i < edges.size() - 1; i++){
            if(!(edges.get(i).getEnd().equals(edges.get(i+1).getStart()))){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
}
