package engine.maths;

import java.util.Objects;

public class Vector2f {
    public float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }



    //Util
    public String print() {
        return "x: " + this.x + " | y: " + this.y;
    }
    //Util



    //Modification    
    public static Vector2f add(Vector2f vec1, Vector2f vec2) {
        return new Vector2f(vec1.x + vec2.x, vec1.y + vec2.y);
    }

    public static Vector2f sub(Vector2f vec1, Vector2f vec2) {
        return new Vector2f(vec1.x - vec2.x, vec1.y - vec2.y);
    }

    public static Vector2f mul(Vector2f vec1, Vector2f vec2) {
        return new Vector2f(vec1.x * vec2.x, vec1.y * vec2.y);
    }

    public static Vector2f div(Vector2f vec1, Vector2f vec2) {
        return new Vector2f(vec1.x / vec2.x, vec1.y / vec2.y);
    }

    public static float length(Vector2f vector) {
        return (float) Math.sqrt(vector.x * vector.x + vector.y * vector.y);
    }

    public static Vector2f normalize(Vector2f vector) {
        float len = Vector2f.length(vector);
        return Vector2f.div(vector, new Vector2f(len, len));
    }

    public static float dot(Vector2f vec1, Vector2f vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y;
    }
    //Modification


    
    //Equals and hash

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2f vector2f = (Vector2f) o;
        return Float.compare(vector2f.x, x) == 0 && Float.compare(vector2f.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    //Equals and hash
}
