package engine.graphics;

import engine.world.terrain.Chunk;

public class ChunkMesher {
    /* Position utils
     *  x = i & MASK
     *  y = (i >> bitsPerCoord) & MASK
     *  z = (i >> (2 * bitsPerCoord)) & MASK
     * */
    private int bitsPerCoord = (int) (Math.log(Chunk.SIZE) / Math.log(2));
    private int MASK = (1 << bitsPerCoord) - 1;

    public static int meshSingleChunk(Chunk chunk) {
        for(int y = 0; y < Chunk.SIZE; y++) {
            for(int z = 0; z < Chunk.SIZE; z++) {
                for(int x = 0; x < Chunk.SIZE; x++) {
                    if(chunk.getBlock(x, y, z) == 0) {
                        continue;
                    }

                    //Check for -z
                    if(isSolid(chunk, x, y, z - 1)) {

                    }

                    //Check for +z
                    if(isSolid(chunk, x, y, z + 1)) {

                    }

                    //Check for -x
                    if(isSolid(chunk, x - 1, y, z)) {

                    }

                    //Check for +x
                    if(isSolid(chunk, x + 1, y, z)) {

                    }

                    //Check for -y
                    if(isSolid(chunk, x, y - 1, z)) {

                    }

                    //Check for +y
                    if(isSolid(chunk, x, y + 1, z)) {

                    }

                }
            }
        }

        return new ChunkMesh(vertices, indices, textCoords, ).constructMesh();
    }

    private static boolean isSolid(Chunk chunk, int x, int y, int z) {
        // Check if coordinates are within bounds
        if (x < 0 || x >= Chunk.SIZE || y < 0 || y >= Chunk.SIZE || z < 0 || z >= Chunk.SIZE) {
            return false;
        }
        return chunk.getBlock(x, y, z) != 0;
    }
}
