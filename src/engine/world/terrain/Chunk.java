package engine.world.terrain;

import engine.graphics.ChunkMesher;
import engine.graphics.models.ChunkMesh;

import static engine.maths.Vector3.Vector3i;

public class Chunk {
    public static final int SIZE = 16;
    public Short[] blocks = new Short[SIZE * SIZE * SIZE];
    public Vector3i pos;
    public ChunkMesh mesh;

    /* Position utils
     *  x = i & MASK
     *  y = (i >> bitsPerCoord) & MASK
     *  z = (i >> (2 * bitsPerCoord)) & MASK
     * */
    private int bitsPerCoord = (int) (Math.log(SIZE) / Math.log(2));
    private int MASK = (1 << bitsPerCoord) - 1;

    public Chunk(int x, int y, int z) {
        pos = new Vector3i(x, y, z);

        for(int i = 0; i < blocks.length; i++) {
            y = (i >> bitsPerCoord) & MASK;
            if(y == SIZE - 1) {
                blocks[i] = 1;
            } else if(y >= SIZE - 3) {
                blocks[i] = 2;
            } else if(y < SIZE - 4) {
                blocks[i] = 3;
            } else {
                blocks[i] = 0;
            }
        }

        mesh = ChunkMesher.meshSingleChunk(this);
    }

    public Chunk remesh() {
        mesh = ChunkMesher.meshSingleChunk(this);
        return this;
    }

    public short getBlock(int x, int y, int z) {
        return blocks[(y * SIZE * SIZE) + (z * SIZE) + x];
    }
}
