package engine.graphics;

import static engine.maths.Vector2.Vector2f;
import static engine.maths.Vector3.Vector3f;

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

    public String print() {
        return "pos: {" + pos.print() + "} | normals: {" + normal.print() + "} | texts: {" + textureCoord.print() + "}";
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