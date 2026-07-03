package org.alegroup.polyederstlviewer.model.geometry.analysis;

import org.alegroup.polyederstlviewer.constants.GeneralConstants;

/**
 * Represents the result of a surface area calculation, including
 * the computed area, the calculation duration, the number of processed
 * triangles, and the number of threads used.
 *
 * @precondition All numeric values must be valid and non-negative.
 * @postcondition A fully initialized AreaResult instance is created.
 */
public class AreaResult
{

    /**
     * The calculated surface area.
     */
    private final double surfaceArea;

    /**
     * The calculation duration in nanoseconds.
     */
    private final long durationNanos;

    /**
     * The number of triangles processed during the calculation.
     */
    private final int triangleCount;

    /**
     * The number of threads used for the calculation.
     */
    private final int threadCount;

    /**
     * Creates a new AreaResult instance.
     *
     * @param surfaceArea   the computed surface area (non-negative)
     * @param durationNanos the calculation duration in nanoseconds
     * @param triangleCount the number of triangles processed
     * @param threadCount   the number of threads used
     * @precondition surfaceArea >= 0 AND durationNanos >= 0 AND triangleCount >= 0 AND threadCount >= 1
     * @postcondition A new AreaResult instance is created with the given values
     */
    public AreaResult (double surfaceArea, long durationNanos, int triangleCount, int threadCount)
    {
        this.surfaceArea = surfaceArea;
        this.durationNanos = durationNanos;
        this.triangleCount = triangleCount;
        this.threadCount = threadCount;
    }

    /**
     * Returns the calculated surface area.
     *
     * @return the surface area
     * @precondition none
     * @postcondition A non-negative double value is returned
     */
    public double getSurfaceArea ()
    {
        return surfaceArea;
    }

    /**
     * Returns the calculation duration in nanoseconds.
     *
     * @return the duration in nanoseconds
     * @precondition none
     * @postcondition A non-negative long value is returned
     */
    public long getDurationNanos ()
    {
        return durationNanos;
    }

    /**
     * Returns the calculation duration in milliseconds.
     *
     * @return the duration in milliseconds
     * @precondition none
     * @postcondition A non-negative long value is returned
     */
    public long getDurationMillis ()
    {
        return durationNanos / GeneralConstants.NANOS_PER_MILLISECOND;
    }

    /**
     * Returns the number of triangles processed during the calculation.
     *
     * @return the triangle count
     * @precondition none
     * @postcondition A non-negative integer is returned
     */
    public int getTriangleCount ()
    {
        return triangleCount;
    }

    /**
     * Returns the number of threads used for the calculation.
     *
     * @return the thread count
     * @precondition none
     * @postcondition An integer >= 1 is returned
     */
    public int getThreadCount ()
    {
        return threadCount;
    }
}