package engine.graphics.models;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ChunkMesh {
    public int vao, pbo, ibo, tbo;
    public int indCount;
    private FloatBuffer vertices;
    private IntBuffer indices;
    private FloatBuffer texCoords;

    //We cant use opengl in worker threads, moving the window context is going to be nasty
    public ChunkMesh(FloatBuffer vertices, IntBuffer indices, FloatBuffer texCoords) {
        this.vertices = vertices;
        this.indices = indices;
        this.texCoords = texCoords;
    }

    public void upload() {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        pbo = storeData(vertices, 0, 3);
        tbo = storeData(texCoords, 1, 2);

        indCount = indices.capacity();
        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private int storeData(FloatBuffer buffer, int index, int size) {
        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return bufferID;
    }

    public void destroy() {
        GL15.glDeleteBuffers(pbo);
        GL15.glDeleteBuffers(ibo);
        GL15.glDeleteBuffers(tbo);

        GL30.glDeleteVertexArrays(vao);
    }
}