package org.alegroup.polyederstlviewer.model.geometry.analysis;

import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;

public class STLParseResult
{
    private final Polyhedron polyhedron;
    private final AreaResult areaResult;

    public STLParseResult(Polyhedron polyhedron, AreaResult areaResult)
    {
        this.polyhedron = polyhedron;
        this.areaResult = areaResult;
    }

    public Polyhedron getPolyhedron()
    {
        return polyhedron;
    }

    public AreaResult getAreaResult()
    {
        return areaResult;
    }
}
