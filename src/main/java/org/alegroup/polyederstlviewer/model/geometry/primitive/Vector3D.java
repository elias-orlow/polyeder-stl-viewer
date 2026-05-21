package org.alegroup.polyederstlviewer.model.geometry.primitive;

public class Vector3D {

    private final float x;
    private final float y;
    private final float z;

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
