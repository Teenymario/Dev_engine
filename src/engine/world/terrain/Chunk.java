package engine.world.terrain;

import engine.graphics.ChunkMesher;
import engine.graphics.models.ChunkMesh;
import engine.world.generation.OpenSimplex2S;

import java.util.Random;

import static engine.maths.Vector3.Vector3i;

public class Chunk {
    public static final int SIZE = 16;
    public static final int SIZE_SQUARED = SIZE * SIZE;
    public static final int SIZE_CUBED = SIZE * SIZE * SIZE;

    public static final double FREQUENCY = 1D / 24D;
    public static final long SEED = new Random().nextLong();
    public Short[] blocks = new Short[SIZE_CUBED];
    public Vector3i pos;
    public ChunkMesh mesh;

    /* Position utils
     *  x = i & MASK
     *  y = (i >> bitsPerCoord) & MASK
     *  z = (i >> (2 * bitsPerCoord)) & MASK
     * */
    private final int bitsPerCoord = (int) (Math.log(SIZE) / Math.log(2));
    private final int MASK = (1 << bitsPerCoord) - 1;

    public Chunk(int x, int y, int z) {
        pos = new Vector3i(x * SIZE / 2, y * SIZE / 2, z * SIZE / 2);

        generate();

        mesh = ChunkMesher.meshSingleChunk(this);
    }

    public Chunk generate() {
        short block;
        double noise;
        double x1;
        double y1;
        double z1;

        for(int x = 0; x < SIZE; x++) {
            for(int z = 0; z < SIZE; z++) {
                for(int y = 0; y < SIZE; y++) {
                    x1 = (pos.x * 2 + x) * FREQUENCY;
                    y1 = (pos.y * 2 + y) * FREQUENCY;
                    z1 = (pos.z * 2 + z) * FREQUENCY;

                    noise = OpenSimplex2S.noise4_ImproveXYZ(SEED, x1, y1, z1, main.main.TIME_S * FREQUENCY);

                    if(noise > 0.15D) {
                        block = 1;
                    } else {
                        block = 0;
                    }

                    setBlock(x, y, z, block);
                }
            }
        }
        return this;
    }

    public Chunk remesh() {
        mesh = ChunkMesher.meshSingleChunk(this);
        return this;
    }

    public short getBlock(int x, int y, int z) {
        return blocks[(y * SIZE_SQUARED) + (z * SIZE) + x];
    }

    private void setBlock(int x, int y, int z, short ID) {
        blocks[(y * SIZE_SQUARED) + (z * SIZE) + x] = ID;
    }
}
