package engine.graphics.renderers;

import engine.IO.Window;
import engine.maths.Vector3f;
import engine.maths.Vector3i;
import engine.world.terrain.BlockBase;
import engine.world.terrain.Chunk;
import engine.graphics.Mesh;
import engine.graphics.Shader;
import engine.objects.Light;
import main.main;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class TerrainRenderer implements ITerrainRenderer {
    private Shader shader;
    private Window window;

    //Rendering utils
    private Vector3i chunkOffset = new Vector3i(0, 0, 0);
    private Vector3f blockPos = new Vector3f(0, 0, 0);
    private static int bitsPerCoord = (int) (Math.log(Chunk.SIZE) / Math.log(2));
    private static int MASK = (1 << bitsPerCoord) - 1;

    public TerrainRenderer(Shader shader) {
        this.shader = shader;
        this.window = Window.getInstance();
    }

    public void render(Chunk[] chunks, Light light) {
        for(Chunk chunk : chunks) {
            chunkOffset.redefine(chunk.pos.x * Chunk.SIZE, chunk.pos.y * Chunk.SIZE, chunk.pos.z * Chunk.SIZE);
            for(int i = 0; i < chunk.blocks.length; i++) {
                blockPos.redefine(chunkOffset.x + (i & MASK), chunkOffset.y + ((i >> bitsPerCoord) & MASK), chunkOffset.z + ((i >> (2 * bitsPerCoord)) & MASK));
                bindMesh(main.meshes.get(main.contentBlocks.get(chunk.blocks[i]).getMeshID()));
                prepareInstance(main.contentBlocks.get(chunk.blocks[i]), light);
                GL11.glDrawElements(GL11.GL_TRIANGLES, main.meshes.get(main.contentBlocks.get(chunk.blocks[i]).getMeshID()).indices.length, GL11.GL_UNSIGNED_INT, 0);
                unbindMesh();
            }
        }
    }

    public void bindMesh(Mesh mesh) {
        if(mesh.hasTransparency()) {
            disableCulling();
        }
        GL30.glBindVertexArray(mesh.vao); //Bind vertices used for drawing
        GL30.glEnableVertexAttribArray(0); //Enable the pos attribute
        GL30.glEnableVertexAttribArray(1); //Enable the textureCoord attribute
        GL30.glEnableVertexAttribArray(2); //Enable the normals attribute
        GL30.glEnableVertexAttribArray(3); //Enable the materials attribute
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.ibo); //Bind the indices used to connect vertices
        shader.bind(); //Bind the shader to the gpu
    }

    public void unbindMesh() {
        shader.unbind();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(3);
        GL30.glDisableVertexAttribArray(2);
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        enableCulling();
    }

    public void prepareInstance(BlockBase block, Light light) {
        shader.setUniform("blockPos", blockPos);
        shader.setUniform("cameraPos", main.camera.pos);
        shader.setUniform("view", main.camera.viewMatrix);
        shader.setUniform("project", window.getProjectionMatrix());
        shader.setUniform("lightPos", light.pos);
        shader.setUniform("lightCol", light.color);
        shader.setUniform("skyColor", window.background);
        shader.setUniform("materials", main.meshes.get(block.getMeshID()).materials);
        for (int i = 0; i < main.meshes.get(block.getMeshID()).textures(); i++) {
            GL30.glActiveTexture(GL30.GL_TEXTURE0 + i);
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, main.meshes.get(block.getMeshID()).textureIDs()[i]);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            shader.setUniform("textureSampler[" + i + "]", i);
        }
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
}
