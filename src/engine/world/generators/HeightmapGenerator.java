package engine.world.generators;

import engine.world.noise.OpenSimplex2S;
import engine.world.terrain.Chunk;

import java.util.ArrayList;

public class HeightmapGenerator extends GeneratorBase {
    private int base = 64;
    private int top = 128;

    //lookup tables
    private int MAX_MIN = (top - base) / 2;     //Divided by 2 since normalised max - min (1 - (-1)) = 2, we divide by 2

    public HeightmapGenerator(long seed, int minY, int maxY) {
        this.seed = seed;
        this.minY = minY;
        this.maxY = maxY;
    }

    public HeightmapGenerator generateChunk(Chunk chunk) {
        internalGen(chunk);

        return this;
    }

    public HeightmapGenerator generateChunks(Chunk[] chunks) {
        for(Chunk chunk: chunks) {
            internalGen(chunk);
        }

        return this;
    }

    public HeightmapGenerator generateChunks(Chunk[] chunks, int offset, int length) {
        for(int i = offset; i < length; i++) {
            internalGen(chunks[i]);
        }

        return this;
    }

    public HeightmapGenerator generateChunks(Chunk[] chunks, ArrayList<Integer> indices) {
        for(int i: indices) {
            internalGen(chunks[i]);
        }

        return this;
    }

    private void internalGen(Chunk chunk) {
        for(int x = 0; x < Chunk.SIZE; x++) {
            for(int z = 0; z < Chunk.SIZE; z++) {
                for(int y = 0; y < Chunk.SIZE; y++) {
                    //Denormalise range -1 <-> 1 to base <-> top
                    int topY = (int) Math.ceil(base + MAX_MIN * (OpenSimplex2S.noise2_ImproveX(seed, (chunk.pos.x + x) * FREQUENCY, (chunk.pos.z + z) * FREQUENCY) + 1));   //+ 1 used since (x - (-1)) = x + 1
                    if(chunk.pos.y + y == topY) {
                        chunk.setBlock(x, y, z, (short) 3);
                    } else if(chunk.pos.y + y > top) {
                        chunk.setBlock(x, y, z, (short) 0);
                    } else if(chunk.pos.y + y < topY) {
                        chunk.setBlock(x, y, z, (short) 2);
                    }
                }
            }
        }

        chunk.mesh();
    }
}
