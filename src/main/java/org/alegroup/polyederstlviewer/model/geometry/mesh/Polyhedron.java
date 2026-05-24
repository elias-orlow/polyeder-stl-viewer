package org.alegroup.polyederstlviewer.model.geometry.mesh;

import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Polyhedron extends Mesh {

    public Polyhedron(List<Triangle> triangles) {
        super(triangles);

        if (!isClosed(getTriangles())) {
            throw new IllegalArgumentException("A polyhedron must be a closed triangle mesh.");
        }
    }

    public Polyhedron(Triangle[] triangles) {
        this(Arrays.asList(triangles));
    }

    private boolean isClosed(List<Triangle> triangles) {
        Map<EdgeKey, Integer> edgeCounter = new HashMap<>();

        for (Triangle triangle : triangles) {
            for (Edge edge : triangle.getEdges()) {
                EdgeKey edgeKey = new EdgeKey(edge);

                int currentCount = edgeCounter.getOrDefault(edgeKey, 0);
                edgeCounter.put(edgeKey, currentCount + 1);
            }
        }

        for (int count : edgeCounter.values()) {
            if (count != 2) {
                return false;
            }
        }

        return true;
    }

    private static class EdgeKey {

        private final Vertex start;
        private final Vertex end;

        public EdgeKey(Edge edge) {
            this.start = edge.getStart();
            this.end = edge.getEnd();
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (!(object instanceof EdgeKey other)) {
                return false;
            }

            boolean sameDirection =
                    this.start.equals(other.start)
                            && this.end.equals(other.end);

            boolean oppositeDirection =
                    this.start.equals(other.end)
                            && this.end.equals(other.start);

            return sameDirection || oppositeDirection;
        }

        @Override
        public int hashCode() {
            return start.hashCode() + end.hashCode();
        }
    }
}
