package engine.graphics.renderers;

import engine.IO.Window;
import engine.content.BlockBase;
import engine.graphics.Mesh;
import engine.graphics.ObjectMesh;
import engine.graphics.Shader;
import main.main;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;

//This will be short lived
public class BlockRenderer implements IBlockRenderer {
    private Shader shader;
    private Window window;
    private Mesh blockMesh;

    public BlockRenderer(Shader shader) {
        this.shader = shader;
        window = Window.getInstance();
        blockMesh = ObjectMesh.construct("resources/models/light.obj");
        blockMesh.createMeshData();
    }

    public void render(ArrayList<BlockBase> blocks) {
        bindMesh(blockMesh);
        for(BlockBase block : blocks) {
            //GL30.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            shader.setUniform("view", main.camera.viewMatrix);
            shader.setUniform("project", window.getProjectionMatrix());
            shader.setUniform("skyColor", window.background);
            shader.setUniform("faces", block.getModel().faces);
            GL11.glDrawElements(GL11.GL_TRIANGLES, blockMesh.indices.length, GL11.GL_UNSIGNED_INT, 0);
        }
        unbindMesh();
    }

    public void bindMesh(Mesh mesh) {
        if(mesh.hasTransparency()) {
            disableCulling();
        }
        GL30.glBindVertexArray(mesh.vao); //Bind vertices used for drawing
        GL30.glEnableVertexAttribArray(0); //Enable the pos attribute
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.ibo); //Bind the indices used to connect vertices
        shader.bind(); //Bind the shader to the gpu
    }

    public void unbindMesh() {
        shader.unbind();
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
