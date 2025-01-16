package engine.maths;

public class Matrix4f {
    public static final int SIZE = 4;
    public float[] elements = new float[SIZE * SIZE];

    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                result.set(i, j, 0);
            }
        }

        result.set(0, 0, 1);
        result.set(1, 1, 1);
        result.set(2, 2, 1);
        result.set(3, 3, 1);

        return  result;
    }

    public static Matrix4f translate(Vector3f translate) {
        Matrix4f result = Matrix4f.identity();

        result.set(3, 0, translate.x);
        result.set(3, 1, translate.y);
        result.set(3, 2, translate.z);

        return result;
    }

    /**
     * This is a special equation designed to rotate a three dimensional vector in three dimensional space.
     * It is used for things like simulating the behaviour of objects in motion or to make something on a pedestal spin as an example.
     *
     *  @param angle or otherly known θ is to determine the angle of rotation
     *  @param axis is the axis or origin of rotation. If in the center of the object it would rotate in its place but if on the corner or anywhere else it would rotate outside of its place
     *  @return A {@code Matrix4f} of rotation that can be used to rotate the object it's applied to
     *
     */

    public static Matrix4f rotate(float angle, Vector3f axis) {
        Matrix4f result = Matrix4f.identity();

        float θ = (float) Math.toRadians(angle);
        float sin = (float) Math.sin(θ); //sine(θ)
        float cos = (float) Math.cos(θ); //cosine(θ)
        float inCos = 1 - cos; //Inverse of cos or (1 - cosine(θ))

        //Equation from https://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle
        //First section
        result.set(0, 0, cos + axis.x * axis.x * inCos);
        result.set(0, 1, axis.x * axis.y * inCos - axis.z * sin);
        result.set(0, 2, axis.x * axis.z * inCos + axis.y * sin);

        //Second section
        result.set(1, 0, axis.y * axis.x * inCos + axis.z * sin);
        result.set(1, 1, cos + axis.y * axis.y * inCos);
        result.set(1, 2, axis.y * axis.z * inCos - axis.x * sin);

        //Third section
        result.set(2, 0, axis.z * axis.x * inCos - axis.y * sin);
        result.set(2, 1, axis.z * axis.y * inCos + axis.x * sin);
        result.set(2, 2, cos + axis.z * axis.z * inCos);

        return result;
    }

    public static Matrix4f scale(Vector3f scaler) {
        Matrix4f result = Matrix4f.identity();

        result.set(0, 0, scaler.x);
        result.set(1, 1, scaler.y);
        result.set(2, 2, scaler.z);

        return result;
    }



    //Transform matrix
    public static Matrix4f transform(Vector3f pos, Vector3f rot, Vector3f scale) {
        Matrix4f transMatrix = Matrix4f.translate(pos);
        Matrix4f rotXMatrix = Matrix4f.rotate(rot.x, new Vector3f(1, 0, 0));
        Matrix4f rotYMatrix = Matrix4f.rotate(rot.y, new Vector3f(0, 1, 0));
        Matrix4f rotZMatrix = Matrix4f.rotate(rot.z, new Vector3f(0, 0, 1));
        Matrix4f scaleMatrix = Matrix4f.scale(scale);

        Matrix4f rotMatrix = Matrix4f.multiply(rotXMatrix, Matrix4f.multiply(rotYMatrix, rotZMatrix));

        return Matrix4f.multiply(transMatrix, Matrix4f.multiply(rotMatrix, scaleMatrix));
    }
    //Transform matrix



    //2D transformation matrix
    public static Matrix4f transform2D(Vector2f translation, Vector2f scale) {
        return Matrix4f.multiply(Matrix4f.translate(new Vector3f(translation.x, translation.y, 0)), Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f)));
    }
    //2D transformation matrix



    public static Matrix4f projection(float fov, float aspect, float near, float far) {
        Matrix4f result = Matrix4f.identity();

        float tanFOV = (float) Math.tan(Math.toRadians(fov / 2));
        float range = far - near;

        result.set(0, 0, 0.5f / (aspect * tanFOV));
        result.set(1, 1, 0.5f / tanFOV);
        result.set(2, 2, -((far + near) / range));
        result.set(2, 3, -1.0f);
        result.set(3, 2, -((2 * far * near) / range));
        result.set(3, 3, 0.0f);

        return result;
    }

    public static Matrix4f view(Vector3f pos, Vector3f rot) {
        Matrix4f transMatrix = Matrix4f.translate(new Vector3f(-pos.x, -pos.y, -pos.z));
        Matrix4f rotXMatrix = Matrix4f.rotate(rot.x, new Vector3f(1, 0, 0));
        Matrix4f rotYMatrix = Matrix4f.rotate(rot.y, new Vector3f(0, 1, 0));
        Matrix4f rotZMatrix = Matrix4f.rotate(rot.z, new Vector3f(0, 0, 1));

        Matrix4f rotMatrix = Matrix4f.multiply(rotZMatrix, Matrix4f.multiply(rotYMatrix, rotXMatrix));

        return Matrix4f.multiply(transMatrix, rotMatrix);
    }

    public static Matrix4f multiply(Matrix4f matrix, Matrix4f other) {
        Matrix4f result = Matrix4f.identity();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                result.set(i, j, matrix.get(i, 0) * other.get(0, j) +
                                     matrix.get(i, 1) * other.get(1, j) +
                                     matrix.get(i, 2) * other.get(2, j) +
                                     matrix.get(i, 3) * other.get(3, j));
            }
        }

        return result;
    }

    public float get(int x, int y) {
        return elements[y * SIZE + x];
    }

    public void set(int x, int y, float val) {
        elements[y * SIZE + x] = val;
    }

    public float[] getAll() {
        return elements;
    }
}
