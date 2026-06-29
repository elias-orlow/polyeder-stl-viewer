package org.alegroup.polyederstlviewer.model.client;

/**
 * Represents a translation vector used for JSON-based client communication.
 * Stores translation values along the X-, Y-, and Z-axes.
 *
 * @precondition All translation values must be finite floating-point numbers.
 * @postcondition A fully initialized TranslateObjectJSON instance is created.
 */
public class TranslateObjectJSON
{

    /**
     * Translation along the X-axis.
     */
    private final float translateX;

    /**
     * Translation along the Y-axis.
     */
    private final float translateY;

    /**
     * Translation along the Z-axis.
     */
    private final float translateZ;

    /**
     * Creates a new translation object with the given components.
     *
     * @param x translation along the X-axis
     * @param y translation along the Y-axis
     * @param z translation along the Z-axis
     * @precondition x, y, z must be finite floating-point values
     * @postcondition A new TranslateObjectJSON instance is created
     */
    public TranslateObjectJSON (float x, float y, float z)
    {
        this.translateX = x;
        this.translateY = y;
        this.translateZ = z;
    }

    /**
     * Returns the translation along the X-axis.
     *
     * @return the X translation value
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getTranslateX ()
    {
        return translateX;
    }

    /**
     * Returns the translation along the Y-axis.
     *
     * @return the Y translation value
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getTranslateY ()
    {
        return translateY;
    }

    /**
     * Returns the translation along the Z-axis.
     *
     * @return the Z translation value
     * @precondition none
     * @postcondition A valid float value is returned
     */
    public float getTranslateZ ()
    {
        return translateZ;
    }
}