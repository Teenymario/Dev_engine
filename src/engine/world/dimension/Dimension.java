package engine.world.dimension;

import engine.world.terrain.Chunk;

public class Dimension implements IDimensionBase {
    private final String registryName;
    public final int SIZE = 1;
    public Chunk[] chunks = new Chunk[SIZE * SIZE * SIZE];

    //Construction utils
    private int bitsPerCoord = (int) (Math.log(SIZE) / Math.log(2));
    private int MASK = (1 << bitsPerCoord) - 1;

    public Dimension(String registryName) {
        this.registryName = registryName;

        for(int i = 0; i < chunks.length; i++) {
            chunks[i] = new Chunk(i & MASK, (i >> bitsPerCoord) & MASK, (i >> (2 * bitsPerCoord)) & MASK);
        }
    }

    public String getRegistryName() {
        return registryName;
    }
}
