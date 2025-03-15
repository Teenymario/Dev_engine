package engine.graphics.renderers;

import engine.IO.Window;
import engine.graphics.Shader;
import engine.graphics.models.ChunkMesh;
import engine.world.terrain.Chunk;
import main.main;
import org.lwjgl.opengl.GL46;

//This will not be short lived
public class ChunkRenderer implements IChunkRenderer {
    private Shader shader;
    private Window window;

    public ChunkRenderer(Shader shader) {
        this.shader = shader;
        window = Window.getInstance();
    }

    public void render(Chunk[] chunks) {
        for(Chunk chunk : chunks) {
            bindMesh(chunk.mesh);
            shader.setUniform("worldPos", chunk.visualPos);
            shader.setUniform("view", main.camera.viewMatrix);
            shader.setUniform("project", window.getProjectionMatrix());
            shader.setUniform("skyColor", window.background);
            //GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, GL46.GL_LINE);
            GL46.glDrawElements(GL46.GL_TRIANGLES, chunk.mesh.indCount, GL46.GL_UNSIGNED_INT, 0);
            //GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, GL46.GL_FILL);
        }
        unbindMesh();
    }

    public void bindMesh(ChunkMesh mesh) {
        //if(mesh.transparent) {
        //    disableCulling();
        //}
        GL46.glBindVertexArray(mesh.vao); //Bind vertices used for drawing
        GL46.glEnableVertexAttribArray(0); //Enable the pos attribute
        GL46.glEnableVertexAttribArray(1); //Enable the tex attribute
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, mesh.ibo); //Bind the indices used to connect vertices
        shader.bind(); //Bind the shader to the gpu
    }

    public void unbindMesh() {
        shader.unbind();
        GL46.glDisableVertexAttribArray(1);
        GL46.glDisableVertexAttribArray(0);
        GL46.glBindVertexArray(0);
        //enableCulling();
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
}
