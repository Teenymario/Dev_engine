package engine.graphics;

import engine.maths.Vector2f;
import engine.maths.Vector3f;

public class Vertex {
    private Vector3f pos, normal;
    private Vector2f textureCoord;
    private float material;

    public Vertex(Vector3f pos, Vector3f normal, Vector2f textureCoord, float material) {
        this.pos = pos;
        this.normal = normal;
        this.textureCoord = textureCoord;
        this.material = material;
    }

    public Vector3f pos() {
        return pos;
    }

    public Vector2f textureCoord() {
        return textureCoord;
    }

    public Vector3f normal() {
        return normal;
    }

    public float material() {
        return material;
    }
}
