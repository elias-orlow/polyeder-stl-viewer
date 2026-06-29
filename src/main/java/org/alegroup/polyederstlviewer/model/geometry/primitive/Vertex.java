package org.alegroup.polyederstlviewer.model.geometry.primitive;

import org.alegroup.polyederstlviewer.constants.ModelConstants;

import java.util.Objects;

/**
 * Represents a 3D vertex defined by its x-, y-, and z-coordinates.
 * Instances of this class are immutable and serve as fundamental
 * geometric primitives within the mesh and polygon system.
 */
public class Vertex
{

    /**
     * X-coordinate of the vertex.
     */
    private final float x;

    /**
     * Y-coordinate of the vertex.
     */
    private final float y;

    /**
     * Z-coordinate of the vertex.
     */
    private final float z;

    /**
     * Creates a new vertex with the given coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @precondition x, y, z must be finite floating-point values
     * @postcondition A new immutable Vertex instance is created
     */
    public Vertex (float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x-coordinate of this vertex.
     *
     * @return the x-coordinate
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getX ()
    {
        return x;
    }

    /**
     * Returns the y-coordinate of this vertex.
     *
     * @return the y-coordinate
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getY ()
    {
        return y;
    }

    /**
     * Returns the z-coordinate of this vertex.
     *
     * @return the z-coordinate
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getZ ()
    {
        return z;
    }

    /**
     * Determines whether this vertex is equal to another object.
     * Two vertices are considered equal if they have identical coordinates.
     *
     * @param obj the object to compare with this vertex
     * @return true if the object is a Vertex with identical coordinates, otherwise false
     * @precondition obj may be null or any object
     * @postcondition A boolean indicating equality is returned
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
     * Returns a formatted string representation of this vertex.
     *
     * @return a string containing the coordinates of this vertex
     * @precondition ModelConstants.VertexToStringText must be a valid format string
     * @postcondition A non-null string is returned
     */
    @Override
    public String toString ()
    {
        return String.format(ModelConstants.VertexToStringText, x, y, z);
    }

    /**
     * Computes the hash code for this vertex based on its coordinates.
     *
     * @return the hash code of this vertex
     * @precondition none
     * @postcondition A hash code consistent with equals() is returned
     */
    @Override
    public int hashCode ()
    {
        return Objects.hash(x, y, z);
    }
}