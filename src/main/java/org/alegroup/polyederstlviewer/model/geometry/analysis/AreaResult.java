package org.alegroup.polyederstlviewer.model.geometry.analysis;

/**
 * Stores the result of a surface area calculation.
 *
 * @precondition Surface area and duration are valid numeric values.
 * @postcondition AreaResult object stores calculation data.
 */
public class AreaResult
{
    private final double surfaceArea;
    private final long durationNanos;
    private final int triangleCount;
    private final int threadCount;

    /**
     * Creates an area result.
     *
     * @param surfaceArea the calculated surface area
     * @param durationNanos the calculation duration in nanoseconds
     * @param triangleCount the number of calculated triangles
     * @param threadCount the number of used threads
     *
     * @precondition surfaceArea is non-negative.
     * @postcondition A new AreaResult object is created.
     */
    public AreaResult(double surfaceArea, long durationNanos, int triangleCount, int threadCount)
    {
        this.surfaceArea = surfaceArea;
        this.durationNanos = durationNanos;
        this.triangleCount = triangleCount;
        this.threadCount = threadCount;
    }

    public double getSurfaceArea()
    {
        return surfaceArea;
    }

    public long getDurationNanos()
    {
        return durationNanos;
    }

    public long getDurationMillis()
    {
        return durationNanos / 1_000_000;
    }

    public int getTriangleCount()
    {
        return triangleCount;
    }

    public int getThreadCount()
    {
        return threadCount;
    }
}