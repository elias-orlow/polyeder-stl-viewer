package org.alegroup.polyederstlviewer.model.client;

public class RotateObjectJSON {

    private float rotateX;
    private float rotateY;
    private float rotateZ;

    public RotateObjectJSON(float x, float y, float z){
        this.rotateX = x;
        this.rotateY = y;
        this.rotateZ = z;
    }

    /* Getter */
    public float getRotateX() {
        return rotateX;
    }
    public float getRotateY() {
        return rotateY;
    }
    public float getRotateZ() {
        return rotateZ;
    }
}
