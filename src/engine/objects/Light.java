package engine.objects;

import engine.maths.Vector3f;

public class Light {

    public Vector3f pos, color;

    public Light(Vector3f pos, Vector3f color) {
        this.pos = pos;
        this.color = color;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
