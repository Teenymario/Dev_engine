package engine.maths;

import java.util.Objects;

public class Vector3l {
    public long x, y, z;

    public Vector3l(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }



    //Util
    public String print() {
        return "x: " + this.x + " | y: " + this.y + " | z: " + this.z;
    }

    public void redefine(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    //Util



    //Modification
    public static Vector3l add(Vector3l vec1, Vector3l vec2) {
        return new Vector3l(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
    }

    public static Vector3l sub(Vector3l vec1, Vector3l vec2) {
        return new Vector3l(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
    }

    public static Vector3l mul(Vector3l vec1, Vector3l vec2) {
        return new Vector3l(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
    }

    public static Vector3l div(Vector3l vec1, Vector3l vec2) {
        return new Vector3l(vec1.x / vec2.x, vec1.y / vec2.y, vec1.z / vec2.z);
    }

    public static long length(Vector3l vector) {
        return (long) Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
    }

    public static Vector3l normalize(Vector3l vector) {
        long len = Vector3l.length(vector);
        return Vector3l.div(vector, new Vector3l(len, len, len));
    }

    public static long dot(Vector3l vec1, Vector3l vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
    }
    //Modification


    //Equals and hash

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3l)) return false;
        Vector3l Vector3l = (Vector3l) o;
        return x == Vector3l.x && y == Vector3l.y && z == Vector3l.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    //Equals and hash
}
