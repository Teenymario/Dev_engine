package engine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import static engine.maths.Vector2.Vector2i;

//Create single large image to map textures to for rendering
public class TextureAtlas {
    public int size;
    public Texture atlas;

    /**
     * Returns a texture atlas object that stores a bytebuffer of a single large image created from a collection of other images provided to it using a packing algorithm.
     * @param textures A list of unsorted textures (do not sort your textures by size the constructor will)
     */
    public TextureAtlas(Texture[] textures) {
        HashMap<Integer, ArrayList<Texture>> sortedTextures = new HashMap<>();       //Arraylist of texture arrays for all textures of specific sizes, starting with largest and ending with smallest
        ArrayList<Integer> sizes = new ArrayList<>();

        for(Texture texture : textures) {
            if(sortedTextures.containsKey(texture.width)) {
                sortedTextures.get(texture.width).add(texture);
            } else {
                sizes.add(texture.width);
                sortedTextures.put(texture.width, new ArrayList<Texture>());
                sortedTextures.get(texture.width).add(texture);
            }
        }

        sizes.sort(Collections.reverseOrder());
        Deque<Vector2i> ladder = new ArrayDeque<>();

        //Calculate total area of texture atlas
        for(int size : sizes) {
            this.size += sortedTextures.get(size).size() * (size * size);
        }
        System.out.println(nextPower(size));
        size = nextPower((int) Math.ceil(Math.sqrt(size)));
        System.out.println(size);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * size * size);

        Vector2i pen = new Vector2i(0, 0);
        for(int texSize : sizes) {
            for(Texture texture : sortedTextures.get(texSize)) {

                //Read texture data into atlas buffer
                for(int y = 0; y < texSize; y++) {
                    buffer.position((pen.y + y) * 4 * size + pen.x * 4);
                    for(int x = 0; x < texSize; x++) {
                        buffer.put(texture.imgData.get());
                        buffer.put(texture.imgData.get());
                        buffer.put(texture.imgData.get());
                        buffer.put(texture.imgData.get());
                    }
                }
                texture.imgData.flip();

                pen.x += texSize;

                if(!ladder.isEmpty() && ladder.peekLast().y == pen.y + texSize) {
                    ladder.peekLast().x = pen.x;
                } else {
                    ladder.addLast(new Vector2i(pen.x, pen.y + texSize));
                }

                if(pen.x == size) {
                    ladder.removeLast();
                    pen.y += texSize;

                    if(!ladder.isEmpty()) {
                        pen.x = ladder.peekLast().x;
                    } else {
                        pen.x = 0;
                    }
                }
            }
        }
        buffer.position(0);

        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int r = buffer.get() & 0xFF;
                int g = buffer.get() & 0xFF;
                int b = buffer.get() & 0xFF;
                int a = buffer.get() & 0xFF; // Ensure your format has alpha!

                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, argb);
            }
        }
        try {
            ImageIO.write(img, "png", new File("output.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        atlas = new Texture(buffer, size, size);
    }

    private int nextPower(int v) {
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        return ++v;
    }
}
