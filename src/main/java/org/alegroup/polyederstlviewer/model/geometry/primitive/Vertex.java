package org.alegroup.polyederstlviewer.model.geometry.primitive;

public class Vertex
{
    private final float x;
    private final float y;
    private final float z;

    public Vertex(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vertex other = (Vertex) obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
}
