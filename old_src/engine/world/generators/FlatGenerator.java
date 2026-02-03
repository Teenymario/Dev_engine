package engine.world.generators;

import engine.world.terrain.Chunk;

import java.util.ArrayList;

public class FlatGenerator extends GeneratorBase {

    public FlatGenerator() {
        seed = 0L;
        minY = 0;
        maxY = 5;
    }

    public FlatGenerator generateChunk(Chunk chunk) {
        internalGen(chunk);

        return this;
    }

    public FlatGenerator generateChunks(Chunk[] chunks) {
        for(Chunk chunk: chunks) {
            internalGen(chunk);
        }

        return this;
    }

    public FlatGenerator generateChunks(Chunk[] chunks, int offset, int length) {
        for(int i = offset; i < length; i++) {
            internalGen(chunks[i]);
        }

        return this;
    }

    public FlatGenerator generateChunks(Chunk[] chunks, ArrayList<Integer> indices) {
        for(int i: indices) {
            internalGen(chunks[i]);
        }

        return this;
    }

    public FlatGenerator generateChunks(Chunk[] chunks, int[] indices) {
        for(int i: indices) {
            internalGen(chunks[i]);
        }

        return this;
    }

    private void internalGen(Chunk chunk) {
        int y1;

        for(int x = 0; x < Chunk.SIZE; x++) {
            for(int z = 0; z < Chunk.SIZE; z++) {
                for(int y = minY; y < maxY; y++) {
                    y1 = chunk.pos.y + y;

                    if(y1 > maxY) continue;

                    if(y1 > maxY - 2) {
                        chunk.setBlock(x, y, z, (short) 3);
                    } else if(y1 > maxY - 4) {
                        chunk.setBlock(x, y, z, (short) 2);
                    } else if(y1 >= minY) {
                        chunk.setBlock(x, y, z, (short) 1);
                    }
                }
            }
        }
    }
}
