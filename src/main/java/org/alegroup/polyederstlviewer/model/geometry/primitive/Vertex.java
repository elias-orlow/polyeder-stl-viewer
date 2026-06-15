package org.alegroup.polyederstlviewer.model.geometry.primitive;

import org.alegroup.polyederstlviewer.constants.ModelConstants;

import java.util.Objects;

/**
 * Vertex wrapper for a 3D point.
 *
 * @precondition coordinates are finite numbers.
 * @postcondition Vertex instance created.
 */
public class Vertex
{
    private final float x;
    private final float y;
    private final float z;

    public float getX ()
    {
        return x;
    }

    public float getY ()
    {
        return y;
    }

    public float getZ ()
    {
        return z;
    }

    /**
     * Create a vertex from coordinates.
     *
     * @precondition x, y, z are finite.
     * @postcondition Vertex created with given coordinates.
     */
    public Vertex (float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Compares this vertex with another object.
     * Two vertices are equal if they are of the same class and have exactly
     * the same x-, y- and z-coordinates.
     *
     * @param obj the object to compare with this vertex
     * @return true if the given object is equal to this vertex, otherwise false
     * @precondition obj may be null or any object.
     * @postcondition The equality result is returned.
     */
    @Override
    public boolean equals (Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vertex other = (Vertex) obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    /**
     * Returns a textual representation of this vertex.
     *
     * @return a formatted string containing the coordinates of this vertex
     * @precondition ModelConstants.VertexToStingText is a valid format string.
     * @postcondition A string representation of this vertex is returned.
     */
    @Override
    public String toString ()
    {
        return String.format(ModelConstants.VertexToStringText, x, y, z);
    }

    /**
     * Calculates the hash code of this vertex based on its coordinates.
     *
     * @return the hash code of this vertex
     * @precondition none.
     * @postcondition A hash code consistent with equals is returned.
     */
    @Override
    public int hashCode ()
    {
        return Objects.hash(getX(), getY(), getZ());
    }

}
