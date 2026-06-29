package org.alegroup.polyederstlviewer.model.rendering;

/**
 * Represents a complete snapshot of all transformation parameters applied to the
 * 3D scene and the displayed polyhedron. This includes object translation,
 * object rotation, camera translation, camera orbit rotation, and camera zoom.
 * Instances of this class are used for undo/redo operations and for restoring
 * previous transformation states.
 */
public class TransformationState
{

    /**
     * Object translation along the X-axis.
     */
    public double objX;

    /**
     * Object translation along the Y-axis.
     */
    public double objY;

    /**
     * Object translation along the Z-axis.
     */
    public double objZ;

    /**
     * Object rotation around its local axis.
     */
    public double objRotate;

    /**
     * Camera translation along the X-axis.
     */
    public double camX;

    /**
     * Camera translation along the Y-axis.
     */
    public double camY;

    /**
     * Camera translation along the Z-axis.
     */
    public double camZ;

    /**
     * Camera orbit rotation around the X-axis.
     */
    public double orbitX;

    /**
     * Camera orbit rotation around the Y-axis.
     */
    public double orbitY;

    /**
     * Camera zoom factor.
     */
    public double camZoom;

    /**
     * Constructs a new transformation state containing all object and camera
     * transformation parameters.
     *
     * @param objX      object translation along X-axis
     * @param objY      object translation along Y-axis
     * @param objZ      object translation along Z-axis
     * @param objRotate object rotation angle
     * @param camX      camera translation along X-axis
     * @param camY      camera translation along Y-axis
     * @param camZ      camera translation along Z-axis
     * @param orbitX    camera orbit rotation around X-axis
     * @param orbitY    camera orbit rotation around Y-axis
     * @param camZoom   camera zoom factor
     * @precondition All parameters must be valid numerical values
     * @postcondition A fully initialized transformation state is created
     */
    public TransformationState (double objX, double objY, double objZ, double objRotate,
                                double camX, double camY, double camZ,
                                double orbitX, double orbitY,
                                double camZoom)
    {

        this.objX = objX;
        this.objY = objY;
        this.objZ = objZ;
        this.objRotate = objRotate;

        this.camX = camX;
        this.camY = camY;
        this.camZ = camZ;

        this.orbitX = orbitX;
        this.orbitY = orbitY;

        this.camZoom = camZoom;
    }
}