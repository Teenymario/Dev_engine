package engine.world.terrain;

import engine.maths.Vector3i;

public class Chunk {
    public static final int SIZE = 8;
    public static final int SIZESQUARED = SIZE * SIZE;
    public Short[] blocks = new Short[SIZE * SIZE * SIZE];
    public Vector3i pos;

    //Construction utils
    private int bitsPerCoord = (int) (Math.log(SIZE) / Math.log(2));
    private int MASK = (1 << bitsPerCoord) - 1;

    public Chunk(int x, int y, int z) {
        pos = new Vector3i(x, y, z);

        for(int i = 0; i < blocks.length; i++) {
            if(((i >> bitsPerCoord) & MASK) == SIZE - 1) {
                blocks[i] = 0;
            } else if(((i >> bitsPerCoord) & MASK) > (SIZE - 4)) {
                blocks[i] = 1;
            } else {
                blocks[i] = 2;
            }
        }
    }

    public short getBlock(int x, int y, int z) {
        return blocks[(y * SIZE * SIZE) + (z * SIZE) + x];
    }
}
