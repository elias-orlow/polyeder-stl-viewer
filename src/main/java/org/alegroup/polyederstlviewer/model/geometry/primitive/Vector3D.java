package org.alegroup.polyederstlviewer.model.geometry.primitive;

import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.constants.ModelConstants;

/**
 * Represents a 3D vector with common vector operations such as subtraction,
 * dot product, cross product, scaling, and magnitude calculation.
 * Instances of this class are immutable.
 */
public class Vector3D
{

    /**
     * X-component of the vector.
     */
    private final float x;

    /**
     * Y-component of the vector.
     */
    private final float y;

    /**
     * Z-component of the vector.
     */
    private final float z;

    /**
     * Creates a new vector with the given components.
     *
     * @param x the x-component
     * @param y the y-component
     * @param z the z-component
     * @precondition x, y, z must be finite floating-point values
     * @postcondition A new immutable Vector3D instance is created
     */
    public Vector3D (float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x-component of this vector.
     *
     * @return the x-component
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getX ()
    {
        return x;
    }

    /**
     * Returns the y-component of this vector.
     *
     * @return the y-component
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getY ()
    {
        return y;
    }

    /**
     * Returns the z-component of this vector.
     *
     * @return the z-component
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getZ ()
    {
        return z;
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param other the vector to subtract
     * @return a new Vector3D representing this - other
     * @precondition other != null
     * @postcondition A new vector instance is returned
     */
    public Vector3D subtract (Vector3D other)
    {
        return new Vector3D(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    /**
     * Computes the cross product of this vector with another.
     *
     * @param other the vector to cross with
     * @return a new Vector3D representing this × other
     * @precondition other != null
     * @postcondition A new vector instance is returned
     */
    public Vector3D cross (Vector3D other)
    {
        float cx = this.y * other.z - this.z * other.y;
        float cy = this.z * other.x - this.x * other.z;
        float cz = this.x * other.y - this.y * other.x;
        return new Vector3D(cx, cy, cz);
    }

    /**
     * Computes the dot product of this vector with another.
     *
     * @param other the vector to dot with
     * @return the scalar dot product
     * @precondition other != null
     * @postcondition A valid float value is returned
     */
    public float dot (Vector3D other)
    {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Computes the Euclidean magnitude (norm) of this vector.
     *
     * @return the non-negative magnitude of the vector
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float magnitude ()
    {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Scales this vector by a scalar value.
     *
     * @param scalar the scalar multiplier
     * @return a new scaled Vector3D
     * @precondition scalar must be finite
     * @postcondition A new vector instance is returned
     */
    public Vector3D scale (float scalar)
    {
        return new Vector3D(
                x * scalar,
                y * scalar,
                z * scalar
        );
    }

    /**
     * Returns a formatted string representation of this vector.
     *
     * @return a string containing the components of this vector
     * @precondition ModelConstants.VectorToStringText must be a valid format string
     * @postcondition A non-null string is returned
     */
    @Override
    public String toString ()
    {
        return String.format(ModelConstants.VectorToStringText, x, y, z);
    }

    /**
     * Determines whether this vector is equal to another object.
     * Two vectors are equal if they have identical components.
     *
     * @param obj the object to compare with this vector
     * @return true if the object is a Vector3D with identical components, otherwise false
     * @precondition obj may be null or any object
     * @postcondition A boolean indicating equality is returned
     */
    @Override
    public boolean equals (Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vector3D other = (Vector3D) obj;

        return Float.compare(x, other.x) == GeneralConstants.INT_ZERO &&
                Float.compare(y, other.y) == GeneralConstants.INT_ZERO &&
                Float.compare(z, other.z) == GeneralConstants.INT_ZERO;
    }

    /**
     * Computes the hash code for this vector based on its components.
     *
     * @return the hash code of this vector
     * @precondition none
     * @postcondition A hash code consistent with equals() is returned
     */
    @Override
    public int hashCode ()
    {
        int result = Float.hashCode(x);
        result = GeneralConstants.HASH_MULTIPLIER * result + Float.hashCode(y);
        result = GeneralConstants.HASH_MULTIPLIER * result + Float.hashCode(z);
        return result;
    }
}