package engine.world.terrain;

import engine.graphics.ChunkMesher;
import engine.graphics.models.ChunkMesh;

import static engine.maths.Vector3.Vector3i;

public class Chunk {
    public static final int SIZE = 16;
    public static final int SIZE_SQUARED = SIZE * SIZE;
    public static final int SIZE_CUBED = SIZE * SIZE * SIZE;

    public short[] blocks = new short[SIZE_CUBED];
    public Vector3i pos;
    public Vector3i visualPos;  //This is here because I dont want to constantly multiply and divide by 2 since opengl likes to render chunks apart if the positioning is 16 blocks
    public ChunkMesh mesh;

    /* Position utils
     *  x = i & MASK
     *  y = (i >> bitsPerCoord) & MASK
     *  z = (i >> (2 * bitsPerCoord)) & MASK
     * */
    private final int bitsPerCoord = (int) (Math.log(SIZE) / Math.log(2));
    private final int MASK = (1 << bitsPerCoord) - 1;

    public Chunk(int x, int y, int z) {
        pos = new Vector3i(x * SIZE, y * SIZE, z * SIZE);
        visualPos = new Vector3i(x * SIZE / 2, y * SIZE / 2, z * SIZE / 2);
    }

    public Chunk mesh() {
        mesh = ChunkMesher.meshSingleChunk(this);
        return this;
    }

    public short getBlock(int x, int y, int z) {
        return blocks[(y * SIZE_SQUARED) + (z * SIZE) + x];
    }

    public void setBlock(int x, int y, int z, short ID) {
        blocks[(y * SIZE_SQUARED) + (z * SIZE) + x] = ID;
    }
}
