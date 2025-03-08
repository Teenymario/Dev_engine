package engine.world.generators;

import engine.world.noise.OpenSimplex2S;
import engine.world.terrain.Chunk;

public class NoiseGenerator extends GeneratorBase {

    public NoiseGenerator(long seed) {
        this.seed = seed;
        minY = Integer.MIN_VALUE;
        maxY = Integer.MAX_VALUE;
    }

    public NoiseGenerator generateChunk(Chunk chunk) {
        internalGen(chunk);

        return this;
    }

    public NoiseGenerator generateChunks(Chunk[] chunks) {
        for(Chunk chunk: chunks) {
            internalGen(chunk);
        }

        return this;
    }

    public NoiseGenerator generateChunks(Chunk[] chunks, int offset, int length) {
        for(int i = offset; i < length; i++) {
            internalGen(chunks[i]);
        }

        return this;
    }

    public NoiseGenerator generateChunks(Chunk[] chunks, int[] indices, int xOff, int yOff, int zOff) {
        for(int i: indices) {
            internalGen(chunks[i]);
        }

        return this;
    }

    private void internalGen(Chunk chunk) {
        short block;
        float noise;

        for(int x = 0; x < Chunk.SIZE; x++) {
            for(int z = 0; z < Chunk.SIZE; z++) {
                for(int y = 0; y < Chunk.SIZE; y++) {
                    noise = OpenSimplex2S.noise3_ImproveXZ(seed, (x + chunk.pos.x) * FREQUENCY, (y + chunk.pos.y) * FREQUENCY, (z + chunk.pos.z) * FREQUENCY);

                    if(noise > 0.5f) {
                        block = 1;
                    } else {
                        block = 0;
                    }

                    chunk.setBlock(x, y, z, block);
                }
            }
        }

        chunk.mesh();
    }
}
