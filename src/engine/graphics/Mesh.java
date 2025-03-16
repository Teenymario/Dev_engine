package engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {
    public Vertex[] vertices;
    public int[] indices;
    public float[] materials;
    public int vao, pbo, ibo, nbo, tbo, mbo;
    public boolean hasTransparency;
    private int[] textureIDs;
    public int width, height;

    public Mesh(Vertex[] vertices, int[] indices, float[] materials, int[] textureIDs, int width, int height) {
        this.vertices = vertices;
        this.indices = indices;
        this.materials = materials;
        this.hasTransparency = false;
        this.textureIDs = textureIDs;
        this.width = width;
        this.height = height;
    }

    public void constructMesh() {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        //Store vertex positions
        FloatBuffer posBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] posData = new float[vertices.length * 3];
        for(int i = 0; i < vertices.length; i++) {
            posData[i * 3] = vertices[i].pos().x;
            posData[i * 3 + 1] = vertices[i].pos().y;
            posData[i * 3 + 2] = vertices[i].pos().z;
        }
        posBuffer.put(posData).flip();

        pbo = storeData(posBuffer, 0, 3);

        //Store texture positions
        FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
        float[] textureData = new float[vertices.length * 2];
        for(int i = 0; i < vertices.length; i++) {
            textureData[i * 2] = vertices[i].textureCoord().x;
            textureData[i * 2 + 1] = vertices[i].textureCoord().y;
        }
        textureBuffer.put(textureData).flip();

        tbo = storeData(textureBuffer, 1, 2);

        //Store vertex normals
        FloatBuffer normBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] normData = new float[vertices.length * 3];
        for(int i = 0; i < vertices.length; i++) {
            normData[i * 3] = vertices[i].normal().x;
            normData[i * 3 + 1] = vertices[i].normal().y;
            normData[i * 3 + 2] = vertices[i].normal().z;
        }
        normBuffer.put(normData).flip();

        nbo = storeData(normBuffer, 2, 3);

        //Store vertex materials
        FloatBuffer matBuffer = MemoryUtil.memAllocFloat(vertices.length);
        float[] matData = new float[vertices.length];
        for(int i = 0; i < vertices.length; i++) {
            matData[i] = vertices[i].material();
        }
        matBuffer.put(matData).flip();

        mbo = storeData(matBuffer, 3, 1);

        //Store indices positions
        IntBuffer indBuffer = MemoryUtil.memAllocInt(indices.length);
        indBuffer.put(indices).flip();

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
        GL15.glDeleteBuffers(nbo);
        GL15.glDeleteBuffers(ibo);
        GL15.glDeleteBuffers(tbo);
        GL15.glDeleteBuffers(mbo);

        GL30.glDeleteVertexArrays(vao);
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void hasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public int[] textureIDs() {
        return textureIDs;
    }

    public int textures() {
        return textureIDs.length;
    }
}