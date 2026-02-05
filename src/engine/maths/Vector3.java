package engine.maths;

import java.util.Objects;

public class Vector3 {
    public static class Vector3f {
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

        public void redefine(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void redefine(Vector3f newPos) {
            this.x = newPos.x;
            this.y = newPos.y;
            this.z = newPos.z;
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

    public static class Vector3i {
        public int x, y, z;

        public Vector3i(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }



        //Util
        public String print() {
            return "x: " + this.x + " | y: " + this.y + " | z: " + this.z;
        }

        public void redefine(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        //Util



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

    public static class Vector3l {
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
}