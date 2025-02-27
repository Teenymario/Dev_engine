package engine.world.dimension;

import engine.world.terrain.Chunk;

import java.util.ArrayList;

public class Dimension implements IDimensionBase {
    private final String registryName;
    public final int SIZE = 1;
    public ArrayList<Chunk> chunks;

    /* Position utils
    *  x = i & MASK
    *  y = (i >> bitsPerCoord) & MASK
    *  z = (i >> (2 * bitsPerCoord)) & MASK
    * */
    private int bitsPerCoord = (int) (Math.log(SIZE) / Math.log(2));
    private int MASK = (1 << bitsPerCoord) - 1;

    public Dimension(String registryName) {
        this.registryName = registryName;
        this.chunks = new ArrayList<>();

        for(int i = 0; i < (SIZE * SIZE * SIZE); i++) {
            chunks.add(new Chunk(i & MASK, (i >> bitsPerCoord) & MASK, (i >> (2 * bitsPerCoord)) & MASK));
        }
    }

    public String getRegistryName() {
        return registryName;
    }
}
