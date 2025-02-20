package engine.graphics.models;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static engine.maths.Vector3.Vector3f;

import static main.main.resourceManager;

public class BlockModel {
    public boolean transparent = false;
    public BlockMesh mesh;
    public ModelType type;

    /* Face order goes as follows:
    0 - North
    1 - South
    2 - West
    3 - East
    4 - Top
    5 - Bottom
     */
    public void setTransparent() {
        this.transparent = true;
    }

    //Will be short lived
    public void setTextureCoords(int[] faces) {
        FloatBuffer texCoords = MemoryUtil.memAllocFloat(72);

        //North
        //resourceManager.atlas.coordData[faces[0] * 4]
        //resourceManager.atlas.coordData[faces[0] * 4 + 1]
        //resourceManager.atlas.coordData[faces[0] * 4 + 2]
        //resourceManager.atlas.coordData[faces[0] * 4 + 3]

        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[0] * 4 + 1]);

        //South
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[1] * 4 + 1]);

        //West
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[2] * 4 + 1]);

        //East
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[3] * 4 + 1]);

        //Top
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[4] * 4 + 1]);

        //Bottom
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4 + 1]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4 + 2]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4 + 3]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4]);
        texCoords.put(resourceManager.atlas.coordData[faces[5] * 4 + 1]);

        //Hardcode block model
        texCoords.position(0);
        mesh = new BlockMesh(new Vector3f[] {
                new Vector3f(-0.5f, 1.0f, -0.5f), new Vector3f(0.5f, 1.0f, 0.5f), new Vector3f(0.5f, 1.0f, -0.5f), new Vector3f(-0.5f, 1.0f, -0.5f), new Vector3f(-0.5f, 1.0f, 0.5f), new Vector3f(0.5f, 1.0f, 0.5f), new Vector3f(0.5f, -0.0f, -0.5f), new Vector3f(0.5f, 0.0f, 0.5f), new Vector3f(-0.5f, -0.0f, -0.5f), new Vector3f(0.5f, 0.0f, 0.5f), new Vector3f(-0.5f, 0.0f, 0.5f), new Vector3f(-0.5f, -0.0f, -0.5f), new Vector3f(0.5f, 1.0f, -0.5f), new Vector3f(0.5f, 0.0f, 0.5f), new Vector3f(0.5f, -0.0f, -0.5f), new Vector3f(0.5f, 1.0f, -0.5f), new Vector3f(0.5f, 1.0f, 0.5f), new Vector3f(0.5f, 0.0f, 0.5f), new Vector3f(-0.5f, -0.0f, -0.5f), new Vector3f(-0.5f, 1.0f, 0.5f), new Vector3f(-0.5f, 1.0f, -0.5f), new Vector3f(-0.5f, -0.0f, -0.5f), new Vector3f(-0.5f, 0.0f, 0.5f), new Vector3f(-0.5f, 1.0f, 0.5f), new Vector3f(-0.5f, 1.0f, 0.5f), new Vector3f(0.5f, 0.0f, 0.5f), new Vector3f(0.5f, 1.0f, 0.5f), new Vector3f(-0.5f, 1.0f, 0.5f), new Vector3f(-0.5f, 0.0f, 0.5f), new Vector3f(0.5f, 0.0f, 0.5f), new Vector3f(-0.5f, 1.0f, -0.5f), new Vector3f(0.5f, -0.0f, -0.5f), new Vector3f(-0.5f, -0.0f, -0.5f), new Vector3f(0.5f, 1.0f, -0.5f), new Vector3f(-0.5f, 1.0f, -0.5f), new Vector3f(0.5f, -0.0f, -0.5f),
        }, texCoords);
    }

    public void setType(ModelType type) {
        this.type = type;
    }
}
