package org.alegroup.polyederstlviewer.model.geometry.primitive;

public class Vertex
{
    private final float x;
    private final float y;

    public Vertex(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX ()
    {
        return x;
    }
    public float getY ()
    {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vertex other = (Vertex) obj;
        return this.x == other.x && this.y == other.y;
    }
}
