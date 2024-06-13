package engine.graphics.renderers;

import engine.IO.Window;
import engine.graphics.GUIMesh;
import engine.graphics.Shader;
import engine.objects.GUITexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class GUIRenderer implements IGUIRendererBase{
    private Shader shader;
    private Window window;
    private final GUIMesh quad;

    public GUIRenderer(Shader shader) {
        this.shader = shader;
        this.window = Window.getInstance();
        quad = new GUIMesh(new float[]{-1, 1, -1, -1, 1, 1, 1, -1});
        quad.constructMesh();
    }

    public void render(List<GUITexture> GUIs) {
        GL30.glBindVertexArray(quad.vao);
        GL20.glEnableVertexAttribArray(0);
        shader.bind();
        for(GUITexture gui: GUIs) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.textureID);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.positions.length);
        }
        shader.unbind();
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public Shader getShader() {
        return shader;
    }
}
