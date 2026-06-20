package org.alegroup.polyederstlviewer.model.rendering;

public class TransformationState {

    public double objX, objY, objZ;
    public double objRotate;

    public double camX, camY, camZ;
    public double orbitX, orbitY;
    public double camZoom;

    public TransformationState(double objX, double objY, double objZ, double objRotate,
                               double camX, double camY, double camZ,
                               double orbitX, double orbitY,
                               double camZoom) {
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
