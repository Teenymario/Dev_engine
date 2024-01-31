package engine.graphics.renderers;

import engine.graphics.Mesh;
import engine.graphics.Shader;
import org.lwjgl.opengl.GL11;

public interface IRendererBase {

    void bindMesh(Mesh mesh);

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

}
