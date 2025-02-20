package engine.graphics.models;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static engine.maths.Vector3.Vector3f;

//Short lived class, wont last long
public class BlockMesh {
    public int indCount;
    public int vao, pbo, ibo, tbo;

    public BlockMesh(Vector3f[] vertices, FloatBuffer texCoords) {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        //Store vertex positions
        FloatBuffer posBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] posData = new float[vertices.length * 3];
        for(int i = 0; i < vertices.length; i++) {
            posData[i * 3] = vertices[i].x;
            posData[i * 3 + 1] = vertices[i].y;
            posData[i * 3 + 2] = vertices[i].z;
        }
        posBuffer.put(posData).flip();

        pbo = storeData(posBuffer, 0, 3);

        //Store texture positions
        tbo = storeData(texCoords, 1, 2);

        //Store indices positions
        //Apparently the indicies were only an array with numbers going up from 0 to the size of the array
        int[] indices = new int[36];
        for(int i = 0; i < 35; i++) {
            indices[i] = i;
        }
        IntBuffer indBuffer = MemoryUtil.memAllocInt(indices.length);
        indBuffer.put(indices).flip();
        indCount = indBuffer.capacity();

        //Bind buffers with stored data
        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indBuffer, GL15.GL_STATIC_DRAW);
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