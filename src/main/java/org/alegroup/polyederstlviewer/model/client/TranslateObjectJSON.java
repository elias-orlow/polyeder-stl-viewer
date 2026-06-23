package org.alegroup.polyederstlviewer.model.client;

public class TranslateObjectJSON {

    private float translateX;
    private float translateY;
    private float translateZ;

    public TranslateObjectJSON(float x, float y, float z){
        this.translateX = x;
        this.translateY = y;
        this.translateZ = z;
    }

    /* Getter */
    public float getTranslateX() {
        return translateX;
    }
    public float getTranslateY() {
        return translateY;
    }
    public float getTranslateZ() {
        return translateZ;
    }
}
