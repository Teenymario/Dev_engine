package engine.graphics;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static main.main.concat;

//Simple class to store openGL texture ID and bits of data from said texture
public class Texture {
    public int textureID;
    public int width, height;
    public ByteBuffer imgData;

    public Texture(String path) {
        try {
            FileInputStream in = new FileInputStream(path);
            PNGDecoder decoder = new PNGDecoder(in);
            ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.RGBA);
            buf.flip();
            imgData = buf;

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

    /**
     * Creates an RGBA texture from a ByteBuffer provided with suitable width and height parameters. Uses 4 bytes per pixel, hence width * height * 4 must be equal to the size of the buffer.
     * @param buffer The buffer containing the raw RGBA data of the image
     * @param width The width of the image
     * @param height The height of the image
     */
    public Texture(ByteBuffer buffer, int width, int height) {
        if(buffer.limit() != width * height * 4) {
            throw new IllegalStateException(concat("Buffer limit is not correct, image requires ", width * height * 4, " Bytes for a size of ", width, "x", height, ", buffer has ", buffer.limit(), " Bytes."));
        }
        textureID = GL11.glGenTextures();
        this.width = width;
        this.height = height;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    }
}
