package engine.world.generators;

import engine.world.terrain.Chunk;

import java.util.ArrayList;

public class oneblockGenerator extends GeneratorBase {

    public oneblockGenerator() {
        this.seed = 0;
        minY = Integer.MIN_VALUE;
        maxY = Integer.MAX_VALUE;
    }

    public oneblockGenerator generateChunk(Chunk chunk) {
        internalGen(chunk);

        return this;
    }

    public oneblockGenerator generateChunks(Chunk[] chunks) {
        for(Chunk chunk: chunks) {
            internalGen(chunk);
        }

        return this;
    }

    public oneblockGenerator generateChunks(Chunk[] chunks, int offset, int length) {
        for(int i = offset; i < length; i++) {
            internalGen(chunks[i]);
        }

        return this;
    }

    public oneblockGenerator generateChunks(Chunk[] chunks, ArrayList<Integer> indices) {
        for(int i: indices) {
            internalGen(chunks[i]);
        }

        return this;
    }

    public oneblockGenerator generateChunks(Chunk[] chunks, int[] indices) {
        for(int i: indices) {
            internalGen(chunks[i]);
        }

        return this;
    }

    private void internalGen(Chunk chunk) {
        if(chunk.pos.x == 0 && chunk.pos.y == 0 && chunk.pos.z == 0) {
            chunk.blocks[0] = 3;
        }
    }
}
