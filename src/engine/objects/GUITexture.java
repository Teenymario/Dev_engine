package engine.objects;

import engine.maths.Vector2f;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GUITexture {
    public int id;
    public Vector2f pos;
    public Vector2f scale;
    public int textureID;

    public GUITexture(int id, Vector2f pos, String path) {
        this.id = id;
        this.pos = pos;

        //Attach texture
        try {
            FileInputStream in = new FileInputStream("resources" + path);
            PNGDecoder decoder = new PNGDecoder(in);
            ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.RGBA);
            buf.flip();

            textureID = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            in.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
