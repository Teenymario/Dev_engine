package engine.maths;

import java.util.Objects;

public class Vector3f {
    public float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }



    //Util
    public String print() {
        return "x: " + this.x + " | y: " + this.y + " | z: " + this.z;
    }
    //Util



    //Modification
    public static Vector3f add(Vector3f vec1, Vector3f vec2) {
        return new Vector3f(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
    }

    public static Vector3f sub(Vector3f vec1, Vector3f vec2) {
        return new Vector3f(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
    }

    public static Vector3f mul(Vector3f vec1, Vector3f vec2) {
        return new Vector3f(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
    }

    public static Vector3f div(Vector3f vec1, Vector3f vec2) {
        return new Vector3f(vec1.x / vec2.x, vec1.y / vec2.y, vec1.z / vec2.z);
    }

    public static float length(Vector3f vector) {
        return (float) Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
    }

    public static Vector3f normalize(Vector3f vector) {
        float len = Vector3f.length(vector);
        return Vector3f.div(vector, new Vector3f(len, len, len));
    }

    public static float dot(Vector3f vec1, Vector3f vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
    }
    //Modification



    //Equals and hash
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3f vector3f = (Vector3f) o;
        return Float.compare(vector3f.x, x) == 0 && Float.compare(vector3f.y, y) == 0 && Float.compare(vector3f.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    //Equals and hash
}