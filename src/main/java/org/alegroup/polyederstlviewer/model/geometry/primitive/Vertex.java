package org.alegroup.polyederstlviewer.model.geometry.primitive;

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

    /**
     * Create a vertex from coordinates.
     *
     * @precondition x,y,z are finite.
     * @postcondition Vertex created with given coordinates.
     */
    public Vertex(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vertex other = (Vertex) obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    @Override
    public String toString ()
    {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash(getX(), getY(), getZ());
    }

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
}
