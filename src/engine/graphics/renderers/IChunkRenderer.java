package engine.graphics.renderers;

import engine.graphics.Shader;
import engine.graphics.models.ChunkMesh;
import engine.world.terrain.Chunk;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public interface IChunkRenderer {

    void bindMesh(ChunkMesh model);

    void unbindMesh();

    Shader getShader();

    void setShader(Shader shader);

    default void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    default void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    void render(ArrayList<Chunk> block);

}
