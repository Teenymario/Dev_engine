package engine.graphics.renderers;

import engine.IO.Window;
import engine.graphics.Mesh;
import engine.graphics.Shader;
import engine.objects.GameObject;
import engine.objects.Light;
import main.main;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import java.util.List;
import java.util.Map;

public class GameObjectRenderer implements IGameObjectRenderer {
    private Shader shader;
    private Window window;

    public GameObjectRenderer(Shader shader) {
        this.shader = shader;
        this.window = Window.getInstance();
    }

    public void render(Map<Integer, List<GameObject>> objects, Light light) {
        for(int meshID : objects.keySet()) {
            bindMesh(main.meshes.get(meshID));
            List<GameObject> batch = objects.get(meshID);
            for(GameObject object : batch) {
                prepareInstance(object, light);
                GL11.glDrawElements(GL11.GL_TRIANGLES, main.meshes.get(object.meshID).indices.length, GL11.GL_UNSIGNED_INT, 0);
            }
            unbindMesh();
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
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.ibo); //Bind the indices used to connect vertices
        shader.bind(); //Bind the shader to the gpu
    }

    public void unbindMesh() {
        shader.unbind();
        GL30.glDisableVertexAttribArray(2);
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        //GL30.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        enableCulling();
    }

    public void prepareInstance(GameObject object, Light light) {
        //GL30.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        shader.setUniform("view", main.camera.viewMatrix);
        shader.setUniform("project", window.getProjectionMatrix());
        shader.setUniform("skyColor", window.background);
        shader.setUniform("blockID", 50);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, main.resourceManager.atlasID);
        GL43.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 0, main.resourceManager.coordSSBO);
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
}
