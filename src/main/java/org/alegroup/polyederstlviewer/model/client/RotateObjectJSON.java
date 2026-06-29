package org.alegroup.polyederstlviewer.model.client;

/**
 * Represents a rotation vector used for JSON-based client communication.
 * Stores rotation values around the X-, Y-, and Z-axes.
 *
 * @precondition All rotation values must be finite floating-point numbers.
 * @postcondition A fully initialized RotateObjectJSON instance is created.
 */
public class RotateObjectJSON
{

    /**
     * Rotation around the X-axis.
     */
    private final float rotateX;

    /**
     * Rotation around the Y-axis.
     */
    private final float rotateY;

    /**
     * Rotation around the Z-axis.
     */
    private final float rotateZ;

    /**
     * Creates a new rotation object with the given components.
     *
     * @param x rotation around the X-axis
     * @param y rotation around the Y-axis
     * @param z rotation around the Z-axis
     * @precondition x, y, z must be finite floating-point values
     * @postcondition A new RotateObjectJSON instance is created
     */
    public RotateObjectJSON (float x, float y, float z)
    {
        this.rotateX = x;
        this.rotateY = y;
        this.rotateZ = z;
    }

    /**
     * Returns the rotation around the X-axis.
     *
     * @return the X rotation value
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getRotateX ()
    {
        return rotateX;
    }

    /**
     * Returns the rotation around the Y-axis.
     *
     * @return the Y rotation value
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getRotateY ()
    {
        return rotateY;
    }

    /**
     * Returns the rotation around the Z-axis.
     *
     * @return the Z rotation value
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getRotateZ ()
    {
        return rotateZ;
    }
}