package engine.objects;

import engine.maths.Vector3f;

public class Light {

    public Vector3f pos, color;

    public Light(Vector3f pos, Vector3f color) {
        this.pos = pos;
        this.color = color;
    }

    public void setPos(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public void setColor(float r, float g, float b) {
        this.color.x = r;
        this.color.y = g;
        this.color.z = b;
    }
}
