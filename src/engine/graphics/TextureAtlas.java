package engine.graphics;

import java.nio.ByteBuffer;
import java.util.*;

import static engine.maths.Vector2.Vector2i;
import static engine.maths.Vector2.Vector2f;

//Create single large image to map textures to for rendering
public class TextureAtlas {
    public int size;
    public Texture atlas;

    /**
     * Returns a texture atlas object that stores a bytebuffer of a single large image created from a collection of other images provided to it using a packing algorithm.
     * @param textures A list of unsorted textures (do not sort your textures by size the constructor will)
     */
    public TextureAtlas(Texture[] textures) {
        HashMap<Integer, ArrayList<Texture>> sortedTextures = new HashMap<>();       //Arraylist of texture arrays for all textures of specific sizes, starting with smallest and ending with smallest
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
        size = (int) (Math.log(nextPower(size)) / Math.log(2));
        System.out.println(size);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * size * size);

        Vector2i pen = new Vector2i(0, 0);
        for(int texSize : sizes) {
            for(Texture texture : sortedTextures.get(texSize)) {
                if (pen.x + texSize > size) {
                    // Move to next row
                    pen.y += texSize;
                    if (!ladder.isEmpty()) {
                        pen.x = ladder.getLast().x;
                    } else {
                        pen.x = 0;
                    }
                }

                for(int y = 0; y < texSize; y++) {
                    buffer.position(((pen.y + y) * size + pen.x) * 4);
                    for(int x = 0; x < texSize; x++) {
                        buffer.put(texture.imgData.get(y * texture.width + x));
                    }
                }
            }
        }
    }

    private int nextPower(int v) {
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        return ++v;
    }
}
