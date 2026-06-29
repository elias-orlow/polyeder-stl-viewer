package org.alegroup.polyederstlviewer.model.geometry.analysis;

import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;

/**
 * Represents the result of parsing an STL file.
 * Contains the resulting polyhedron and its associated area analysis.
 *
 * @precondition polyhedron != null AND areaResult != null
 * @postcondition A valid STLParseResult instance is created
 */
public class STLParseResult
{

    /**
     * The parsed polyhedron.
     */
    private final Polyhedron polyhedron;

    /**
     * The computed area analysis result.
     */
    private final AreaResult areaResult;

    /**
     * Creates a new STLParseResult containing a polyhedron and its area analysis.
     *
     * @param polyhedron the parsed polyhedron
     * @param areaResult the computed area analysis result
     * @precondition polyhedron != null AND areaResult != null
     * @postcondition A new STLParseResult instance is created
     */
    public STLParseResult (Polyhedron polyhedron, AreaResult areaResult)
    {
        this.polyhedron = polyhedron;
        this.areaResult = areaResult;
    }

    /**
     * Returns the parsed polyhedron.
     *
     * @return the polyhedron
     * @precondition none
     * @postcondition A non-null Polyhedron instance is returned
     */
    public Polyhedron getPolyhedron ()
    {
        return polyhedron;
    }

    /**
     * Returns the computed area analysis result.
     *
     * @return the area result
     * @precondition none
     * @postcondition A non-null AreaResult instance is returned
     */
    public AreaResult getAreaResult ()
    {
        return areaResult;
    }
}