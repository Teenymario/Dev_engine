package engine.objects;

import engine.IO.Input;
import engine.maths.Matrix4f;
import engine.maths.Vector3f;
import main.main;
import org.lwjgl.glfw.GLFW;

import static main.main.sensitivity;
import static main.main.speed;

public class Camera {
    public Vector3f pos, rot;
    private float horizAngle, vertAngle, distance = 2;
    private int oldX = 0, oldY = 0, newX, newY;
    public Matrix4f viewMatrix;

    public Camera(Vector3f pos, Vector3f rot) {
        this.pos = pos;
        this.rot = rot;
        this.viewMatrix = Matrix4f.view(pos, rot);
    }

    public void updateVisual() {
        boolean updateMatrix = false;
        newX = Input.getMouseX();
        newY = Input.getMouseY();

        float x = (float) Math.sin(Math.toRadians(rot.y)) * speed * (main.sprinting ? 2 : 1) * main.speedModifier;
        float z = (float) Math.cos(Math.toRadians(rot.y)) * speed * (main.sprinting ? 2 : 1) * main.speedModifier;

        if(Input.isKeyDown(GLFW.GLFW_KEY_A)) {
            pos.x -= z;
            pos.z += x;
            updateMatrix = true;
        }
        if(Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            pos.x += z;
            pos.z -= x;
            updateMatrix = true;
        }
        if(Input.isKeyDown(GLFW.GLFW_KEY_W)) {
            pos.x -= x;
            pos.z -= z;
            updateMatrix = true;
        }
        if(Input.isKeyDown(GLFW.GLFW_KEY_S)){
            pos.x += x;
            pos.z += z;
            updateMatrix = true;
        }
        if(Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            pos.y += speed * main.speedModifier;
            updateMatrix = true;
        }
        if(Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            pos.y -= speed * main.speedModifier;
            updateMatrix = true;
        }

        float dx = (float) (newX - oldX);
        float dy = (float) (newY - oldY);

        if(oldX != newX || oldY != newY) {
            rot.x -= dy * sensitivity;
            rot.y -= dx * sensitivity;
            rot.x = Math.max(-90, Math.min(90, rot.x));

            oldX = newX;
            oldY = newY;
            updateMatrix = true;
        }

        if(updateMatrix) {
            this.viewMatrix = Matrix4f.view(pos, rot);
        }
    }

    public void updateVisual(GameObject target) {
        boolean updateMatrix = false;
        newX = Input.getMouseX();
        newY = Input.getMouseY();

        float dx = (float) (newX - oldX);
        float dy = (float) (newY - oldY);

        if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            vertAngle -= dy * sensitivity;
            horizAngle += dx * sensitivity;
            vertAngle = Math.max(-90, Math.min(90, vertAngle));
            updateMatrix = true;
        }
        if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            if(distance > 0) {
                distance += dy * 0.005f;
            } else {
                distance = 0.1f;
            }
            updateMatrix = true;
        }

        float horizontal = (float) (distance * Math.cos(Math.toRadians(vertAngle)));
        float vertical = (float) (distance * Math.sin(Math.toRadians(vertAngle)));

        float xOff = (float) (horizontal * Math.sin(Math.toRadians(-horizAngle)));
        float zOff = (float) (horizontal * Math.cos(Math.toRadians(-horizAngle)));

        pos.x = target.pos.x + xOff;
        pos.y = target.pos.y - vertical;
        pos.z = target.pos.z + zOff;

        rot.x = vertAngle;
        rot.y = -horizAngle;

        if(updateMatrix) {
            this.viewMatrix = Matrix4f.view(pos, rot);
        }

        oldX = newX;
        oldY = newY;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getRot() {
        return rot;
    }

    public void setRot(Vector3f rot) {
        this.rot = rot;
    }
}