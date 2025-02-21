package engine.graphics;

import org.newdawn.slick.opengl.PNGDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Image {
    public int width, height;
    public ByteBuffer imgData;

    public Image(String path) {
        try {
            FileInputStream in = new FileInputStream(path);
            PNGDecoder decoder = new PNGDecoder(in);
            imgData = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(imgData, decoder.getWidth() * 4, PNGDecoder.RGBA);
            imgData.flip();
            width = decoder.getWidth();
            height = decoder.getHeight();

            in.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
