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
    public ByteBuffer imgData;
    public float[] coordData;   //4 floats for a single image

    /**
     * Returns a texture atlas object that stores a bytebuffer of a single large image created from a collection of other images provided to it using a packing algorithm.
     * @param textures A list of unsorted textures (do not sort your textures by size the constructor will)
     */
    public TextureAtlas(ArrayList<Texture> textures) {
        HashMap<Integer, ArrayList<Texture>> sortedTextures = new HashMap<>();       //Arraylist of texture arrays for all textures of specific sizes, starting with largest and ending with smallest
        HashMap<Integer, ArrayList<Integer>> textureIDs = new HashMap<>();           //This is here because there seems to be a little issue with the IDs not being properly linked to coordData

        ArrayList<Integer> sizes = new ArrayList<>();

        int counter = -1;
        for(Texture texture : textures) {
            if (!sortedTextures.containsKey(texture.width)) {
                sizes.add(texture.width);
                sortedTextures.put(texture.width, new ArrayList<>());
                textureIDs.put(texture.width, new ArrayList<>());
            }
            sortedTextures.get(texture.width).add(texture);
            textureIDs.get(texture.width).add(++counter);

        }

        sizes.sort(Collections.reverseOrder());
        Deque<Vector2i> ladder = new ArrayDeque<>();
        coordData = new float[textures.size() * 4];

        //Calculate total area of texture atlas
        for(int size : sizes) {
            this.size += sortedTextures.get(size).size() * (size * size);
        }

        size = nextPower((int) Math.ceil(Math.sqrt(size)));
        imgData = ByteBuffer.allocateDirect(4 * size * size);

        Vector2i pen = new Vector2i(0, 0);
        int id;

        for(int texSize : sizes) {
            counter = -1;
            for(Texture texture : sortedTextures.get(texSize)) {

                //Read texture data into atlas buffer
                for(int y = 0; y < texSize; y++) {
                    imgData.position((pen.y + y) * 4 * size + pen.x * 4);
                    for(int x = 0; x < texSize; x++) {
                        imgData.put(texture.imgData.get());
                        imgData.put(texture.imgData.get());
                        imgData.put(texture.imgData.get());
                        imgData.put(texture.imgData.get());
                    }
                }
                texture.imgData.position(0);

                /*
                Floats are put in for top left and bottom right, interchange them for other 2 positions
                 */
                id = textureIDs.get(texSize).get(++counter);

                coordData[id * 4] = (pen.x) / (float) size;
                coordData[id * 4 + 1] = (pen.y) / (float) size;
                coordData[id * 4 + 2] = (pen.x + texSize) / (float) size;
                coordData[id * 4 + 3] = (pen.y + texSize) / (float) size;

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
        imgData.position(0);

        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int r = imgData.get() & 0xFF;
                int g = imgData.get() & 0xFF;
                int b = imgData.get() & 0xFF;
                int a = imgData.get() & 0xFF;

                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, argb);
            }
        }
        try {
            ImageIO.write(img, "png", new File("output.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgData.flip();
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
