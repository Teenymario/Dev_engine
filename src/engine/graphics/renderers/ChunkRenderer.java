package engine.graphics.renderers;

import engine.IO.Window;
import engine.graphics.Shader;
import engine.graphics.models.ChunkMesh;
import engine.world.terrain.Chunk;
import main.main;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;

//This will be short lived
public class ChunkRenderer implements IChunkRenderer {
    private Shader shader;
    private Window window;

    public ChunkRenderer(Shader shader) {
        this.shader = shader;
        window = Window.getInstance();
    }

    public void render(ArrayList<Chunk> blocks) {
        for(Chunk chunk : blocks) {
            bindMesh(chunk.mesh);
            //GL30.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            shader.setUniform("worldPos", chunk.pos);
            shader.setUniform("view", main.camera.viewMatrix);
            shader.setUniform("project", window.getProjectionMatrix());
            shader.setUniform("skyColor", window.background);
            GL11.glDrawElements(GL11.GL_TRIANGLES, chunk.mesh.indCount, GL11.GL_UNSIGNED_INT, 0);
        }
        unbindMesh();
    }

    public void bindMesh(ChunkMesh mesh) {
        //if(mesh.transparent) {
        //    disableCulling();
        //}
        GL30.glBindVertexArray(mesh.vao); //Bind vertices used for drawing
        GL30.glEnableVertexAttribArray(0); //Enable the pos attribute
        GL30.glEnableVertexAttribArray(1); //Enable the tex attribute
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.ibo); //Bind the indices used to connect vertices
        shader.bind(); //Bind the shader to the gpu
    }

    public void unbindMesh() {
        shader.unbind();
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        //GL30.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        enableCulling();
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
}
