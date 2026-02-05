package engine.maths;

import java.util.Objects;

public class Vector2 {
    public static class Vector2f {
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
            return vector2f.x == 0 && vector2f.y == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        //Equals and hash
    }

    public static class Vector2i {
        public int x, y;

        public Vector2i(int x, int y) {
            this.x = x;
            this.y = y;
        }


        //Util
        public String print() {
            return "x: " + this.x + " | y: " + this.y;
        }
        //Util


        //Modification
        public static Vector2i add(Vector2i vec1, Vector2i vec2) {
            return new Vector2i(vec1.x + vec2.x, vec1.y + vec2.y);
        }

        public static Vector2i sub(Vector2i vec1, Vector2i vec2) {
            return new Vector2i(vec1.x - vec2.x, vec1.y - vec2.y);
        }

        public static Vector2i mul(Vector2i vec1, Vector2i vec2) {
            return new Vector2i(vec1.x * vec2.x, vec1.y * vec2.y);
        }

        public static Vector2i div(Vector2i vec1, Vector2i vec2) {
            return new Vector2i(vec1.x / vec2.x, vec1.y / vec2.y);
        }

        public static int length(Vector2i vector) {
            return (int) Math.sqrt(vector.x * vector.x + vector.y * vector.y);
        }

        public static Vector2i normalize(Vector2i vector) {
            int len = Vector2i.length(vector);
            return Vector2i.div(vector, new Vector2i(len, len));
        }

        public static float dot(Vector2i vec1, Vector2i vec2) {
            return vec1.x * vec2.x + vec1.y * vec2.y;
        }
        //Modification


        //Equals and hash

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vector2i Vector2i = (Vector2i) o;
            return Vector2i.x == 0 && Vector2i.y == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        //Equals and hash
    }
}
