package engine.maths;

import java.util.Objects;

public class Vector4f {
    public float x, y, z, w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }



    //Modification
    public static Vector4f add(Vector4f vec1, Vector4f vec2) {
        return new Vector4f(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z, vec1.w + vec2.w);
    }

    public static Vector4f sub(Vector4f vec1, Vector4f vec2) {
        return new Vector4f(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z, vec1.w - vec2.w);
    }

    public static Vector4f mul(Vector4f vec1, Vector4f vec2) {
        return new Vector4f(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z, vec1.w * vec2.w);
    }

    public static Vector4f div(Vector4f vec1, Vector4f vec2) {
        return new Vector4f(vec1.x / vec2.x, vec1.y / vec2.y, vec1.z / vec2.z, vec1.w / vec2.w);
    }

    public static float length(Vector4f vector) {
        return (float) Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z + vector.w * vector.w);
    }

    public static Vector4f normalize(Vector4f vector) {
        float len = Vector4f.length(vector);
        return Vector4f.div(vector, new Vector4f(len, len, len, len));
    }

    public static float dot(Vector4f vec1, Vector4f vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z + vec1.w * vec2.w;
    }
    //Modification



    //Equals and hash
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector4f)) return false;
        Vector4f vector4f = (Vector4f) o;
        return Float.compare(vector4f.x, x) == 0 && Float.compare(vector4f.y, y) == 0 && Float.compare(vector4f.z, z) == 0 && Float.compare(vector4f.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }
    //Equals and hash
}
