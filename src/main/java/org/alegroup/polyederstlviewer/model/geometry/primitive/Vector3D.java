package org.alegroup.polyederstlviewer.model.geometry.primitive;

/**
 * 3D vector with common vector operations.
 *
 * @precondition None.
 * @postcondition Instance provides vector arithmetic operations.
 */
public class Vector3D {

    private final float x;
    private final float y;
    private final float z;

    /**
     * Create a vector with components.
     *
     * @precondition Components are finite numbers.
     * @postcondition Vector instance created.
     */
    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Subtract another vector from this vector.
     *
     * @precondition other != null.
     * @postcondition Returns a new Vector3D representing this - other.
     */
    public Vector3D subtract(Vector3D other)
    {
        return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Cross product of this vector with other.
     *
     * @precondition other != null.
     * @postcondition Returns a new Vector3D representing this x other.
     */
    public Vector3D cross(Vector3D other)
    {
        float cx = this.y * other.z - this.z * other.y;
        float cy = this.z * other.x - this.x * other.z;
        float cz = this.x * other.y - this.y * other.x;
        return new Vector3D(cx, cy, cz);
    }

    /**
     * Dot product of this vector with other.
     *
     * @precondition other != null.
     * @postcondition Returns scalar dot product.
     */
    public float dot(Vector3D other)
    {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Euclidean norm (magnitude).
     *
     * @precondition None.
     * @postcondition Returns non-negative magnitude.
     */
    public float magnitude()
    {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Scale vector by scalar.
     *
     * @precondition scalar is finite.
     * @postcondition Returns new scaled vector.
     */
    public Vector3D scale(float scalar)
    {
        return new Vector3D(x * scalar, y * scalar, z * scalar);
    }

    @Override
    public String toString()
    {
        return String.format("Vector3D(%.6f, %.6f, %.6f)", x, y, z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vector3D vector3D = (Vector3D) obj;
        return Float.compare(x, vector3D.x) == 0
                && Float.compare(y, vector3D.y) == 0
                && Float.compare(z, vector3D.z) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        result = 31 * result + Float.hashCode(z);
        return result;
    }
}
