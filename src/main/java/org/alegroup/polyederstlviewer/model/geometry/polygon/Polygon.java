package org.alegroup.polyederstlviewer.model.geometry.polygon;

import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.PolygonalChain;

import java.util.Arrays;
import java.util.List;

public class Polygon extends PolygonalChain {

    public Polygon(List<Edge> edges) {
        super(edges);

        if (!isPolygon(edges)) {
            throw new IllegalArgumentException("The given polygonal chain is not a closed polygon.");
        }
    }

    public Polygon(Edge[] edges) {
        this(Arrays.asList(edges));
    }

    private boolean isPolygon(List<Edge> edges) {
        return hasAtLeastThreeEdges(edges) && isClosed(edges);
    }

    private boolean hasAtLeastThreeEdges(List<Edge> edges) {
        return edges.size() >= 3;
    }

    private boolean isClosed(List<Edge> edges) {
        Edge firstEdge = edges.get(0);
        Edge lastEdge = edges.get(edges.size() - 1);

        return lastEdge.getEnd().equals(firstEdge.getStart());
    }
}
