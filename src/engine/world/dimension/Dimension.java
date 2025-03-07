package engine.world.dimension;

import engine.world.terrain.Chunk;

import java.util.ArrayList;

public class Dimension implements IDimensionBase {
    public final int SIZE = 4;
    private final String registryName;
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

        for(int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                for (int y = 0; y < SIZE; y++) {
                    chunks.add(new Chunk(x, y, z));
                }
            }
        }
    }

    public String getRegistryName() {
        return registryName;
    }
}
