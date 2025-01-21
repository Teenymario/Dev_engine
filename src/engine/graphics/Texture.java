package engine.graphics;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

//Simple class to store openGL texture ID and bits of data from said texture
public class Texture {
    public int textureID;
    public int width, height;

    public Texture(String path) {
        try {
            FileInputStream in = new FileInputStream(path);
            PNGDecoder decoder = new PNGDecoder(in);
            ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.RGBA);
            buf.flip();

            textureID = GL11.glGenTextures();
            width = decoder.getWidth();
            height = decoder.getHeight();
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
