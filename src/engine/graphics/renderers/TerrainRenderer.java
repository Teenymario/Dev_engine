package engine.graphics.renderers;

import engine.IO.Window;
import engine.Terrain.Terrain;
import engine.graphics.Mesh;
import engine.graphics.Shader;
import engine.objects.Light;
import main.main;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class TerrainRenderer implements ITerrainRenderer {
    private Shader shader;
    private Window window;

    public TerrainRenderer(Shader shader) {
        this.shader = shader;
        this.window = Window.getInstance();
    }

    public void render(List<Terrain> terrains, Light light) {
        for(Terrain terrain : terrains) {
            bindMesh(main.meshes.get(terrain.meshID));
            prepareInstance(terrain, light);
            GL11.glDrawElements(GL11.GL_TRIANGLES, main.meshes.get(terrain.meshID).indices.length, GL11.GL_UNSIGNED_INT, 0);
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

    public void prepareInstance(Terrain terrain, Light light) {
        shader.setUniform("model", terrain.transformationMatrix);
        shader.setUniform("view", main.camera.viewMatrix);
        shader.setUniform("project", window.getProjectionMatrix());
        shader.setUniform("lightPos", light.pos);
        shader.setUniform("lightCol", light.color);
        shader.setUniform("skyColor", window.background);
        shader.setUniform("materials", main.meshes.get(terrain.meshID).materials);
        for (int i = 0; i < main.meshes.get(terrain.meshID).textures(); i++) {
            GL30.glActiveTexture(GL30.GL_TEXTURE0 + i);
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, main.meshes.get(terrain.meshID).textureIDs()[i]);
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
