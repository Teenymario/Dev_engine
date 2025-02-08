package engine.objects;

import engine.maths.Matrix4f;

import static engine.maths.Vector3.Vector3f;

public class GameObject {
    public int meshID;
    public Vector3f pos, rot, scale;
    private Matrix4f transformMatrix;

    public GameObject(int meshID, Vector3f pos, Vector3f rot, Vector3f scale) {
        this.meshID = meshID;
        this.pos = pos;
        this.rot = rot;
        this.scale = scale;
        this.transformMatrix = Matrix4f.transform(pos, rot, scale);
    }



    //Update
    public void update() {
        updateMatrix();
    }

    private void updateMatrix() {
        this.transformMatrix = Matrix4f.transform(pos, rot, scale);
    }
    //Update



    //Getters
    public Matrix4f transformMatrix() {
        return transformMatrix;
    }
    //Getters



    //Setters
    public void setPos(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
        updateMatrix();
    }

    public void setRot(float pitch, float yaw, float roll) {
        this.rot.x = pitch;
        this.rot.y = yaw;
        this.rot.z = roll;
        updateMatrix();
    }

    public void setScale(float x, float y, float z) {
        this.scale.x = x;
        this.scale.y = y;
        this.scale.z = z;
        updateMatrix();
    }

    public void setMeshID(int meshID) {
        this.meshID = meshID;
        updateMatrix();
    }

    public void setTransformMatrix(Matrix4f transformMatrix) {
        this.transformMatrix = transformMatrix;
        updateMatrix();
    }
    //Setters
}
