package engine.graphics.renderers;

import engine.content.BlockBase;
import engine.graphics.Shader;
import engine.graphics.models.BlockModel;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public interface IBlockRenderer {

    void bindMesh(BlockModel model);

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

    void render(ArrayList<BlockBase> block);

}
