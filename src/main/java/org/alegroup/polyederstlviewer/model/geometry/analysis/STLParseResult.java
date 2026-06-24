package org.alegroup.polyederstlviewer.model.geometry.analysis;

import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;

public class STLParseResult
{
    private final Polyhedron polyhedron;
    private final double surfaceArea;
    private final long durationNanos;

    public STLParseResult(Polyhedron polyhedron, double surfaceArea, long durationNanos)
    {
        this.polyhedron = polyhedron;
        this.surfaceArea = surfaceArea;
        this.durationNanos = durationNanos;
    }

    public Polyhedron getPolyhedron()
    {
        return polyhedron;
    }

    public double getSurfaceArea()
    {
        return surfaceArea;
    }

    public long getDurationMillis()
    {
        return durationNanos / 1_000_000;
    }
}