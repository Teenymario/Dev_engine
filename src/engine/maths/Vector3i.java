package engine.maths;

import java.util.Objects;

public class Vector3i {
    public int x, y, z;

    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }



    //Modification
    public static Vector3i add(Vector3i vec1, Vector3i vec2) {
        return new Vector3i(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
    }

    public static Vector3i sub(Vector3i vec1, Vector3i vec2) {
        return new Vector3i(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
    }

    public static Vector3i mul(Vector3i vec1, Vector3i vec2) {
        return new Vector3i(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
    }

    public static Vector3i div(Vector3i vec1, Vector3i vec2) {
        return new Vector3i(vec1.x / vec2.x, vec1.y / vec2.y, vec1.z / vec2.z);
    }

    public static int length(Vector3i vector) {
        return (int) Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
    }

    public static Vector3i normalize(Vector3i vector) {
        int len = Vector3i.length(vector);
        return Vector3i.div(vector, new Vector3i(len, len, len));
    }

    public static int dot(Vector3i vec1, Vector3i vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
    }
    //Modification



    //Equals and hash

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3i)) return false;
        Vector3i vector3i = (Vector3i) o;
        return x == vector3i.x && y == vector3i.y && z == vector3i.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    //Equals and hash
}
