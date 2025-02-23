package engine.graphics;

import engine.graphics.models.ChunkMesh;
import engine.world.terrain.Chunk;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class ChunkMesher {
    /* Position utils
     *  x = i & MASK
     *  y = (i >> bitsPerCoord) & MASK
     *  z = (i >> (2 * bitsPerCoord)) & MASK
     * */
    private final int bitsPerCoord = (int) (Math.log(Chunk.SIZE) / Math.log(2));
    private final int MASK = (1 << bitsPerCoord) - 1;

    private static final float[] southFace = {
            0.5f, -0.5f, -0.5f,  // Bottom Right (0)
            -0.5f, -0.5f, -0.5f,  // Bottom Left  (1)
            -0.5f,  0.5f, -0.5f,  // Top Left     (2)
            0.5f,  0.5f, -0.5f   // Top Right    (3)
    };
    private static final float[] northFace = {  //correct
            0.5f, -0.5f, -0.5f,  // Bottom Right (0)
            -0.5f, -0.5f, -0.5f,  // Bottom Left  (1)
            -0.5f,  0.5f, -0.5f,  // Top Left     (2)
            0.5f,  0.5f, -0.5f   // Top Right    (3)
    };
    private static final float[] westFace = {
            -0.5f, -0.5f, -0.5f,  // Bottom Left  (0)
            -0.5f, -0.5f,  0.5f,  // Bottom Right (1)
            -0.5f,  0.5f,  0.5f,  // Top Right    (2)
            -0.5f,  0.5f, -0.5f   // Top Left     (3)
    };
    private static final float[] eastFace = {   //correct
            0.5f, -0.5f,  0.5f,  // Bottom Left  (0)
            0.5f, -0.5f, -0.5f,  // Bottom Right (1)
            0.5f,  0.5f, -0.5f,  // Top Right    (2)
            0.5f,  0.5f,  0.5f   // Top Left     (3)
    };
    private static final float[] topFace = {
            -0.5f,  0.5f, -0.5f,  // Bottom Left  (0)
            0.5f,  0.5f, -0.5f,  // Bottom Right (1)
            0.5f,  0.5f,  0.5f,  // Top Right    (2)
            -0.5f,  0.5f,  0.5f   // Top Left     (3)
    };
    private static final float[] bottomFace = {
            -0.5f, -0.5f,  0.5f,  // Bottom Left  (0)
            0.5f, -0.5f,  0.5f,  // Bottom Right (1)
            0.5f, -0.5f, -0.5f,  // Top Right    (2)
            -0.5f, -0.5f, -0.5f   // Top Left     (3)
    };

    private static final int[] faceInds = { 0, 1, 2, 2, 3, 0 };

    public static ChunkMesh meshSingleChunk(Chunk chunk) {
        ArrayList<Float> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        ArrayList<Float> texCoords = new ArrayList<>();

        for(int y = 0; y < Chunk.SIZE; y++) {
            for(int z = 0; z < Chunk.SIZE; z++) {
                for(int x = 0; x < Chunk.SIZE; x++) {
                    if(chunk.getBlock(x, y, z) == 0) {
                        continue;
                    }
                    int e = 12;

                    //Check for -z | north
                    if(isNotSolid(chunk, x, y, z - 1)) {
                        for(int i = 0; i < 12; i += 3) {
                            vertices.add(northFace[i] + x);
                            vertices.add(northFace[i + 1] + y);
                            vertices.add(northFace[i + 2] + z);
                        }
                        for(int i = 0; i < 6; i++) {
                            indices.add(vertices.size() - e + faceInds[i]);
                        }
                    }

                    //Check for +z | south
                    if(isNotSolid(chunk, x, y, z + 1)) {
                        for(int i = 0; i < 12; i += 3) {
                            vertices.add(southFace[i] + x);
                            vertices.add(southFace[i + 1] + y);
                            vertices.add(southFace[i + 2] + z);
                        }
                        for(int i = 0; i < 6; i++) {
                            indices.add(vertices.size() - e + faceInds[i]);
                        }
                    }

                    //Check for -x | west
                    if(isNotSolid(chunk, x - 1, y, z)) {
                        for(int i = 0; i < 12; i += 3) {
                            vertices.add(westFace[i] + x);
                            vertices.add(westFace[i + 1] + y);
                            vertices.add(westFace[i + 2] + z);
                        }
                        for(int i = 0; i < 6; i++) {
                            indices.add(vertices.size() - e + faceInds[i]);
                        }
                    }

                    //Check for +x | east
                    if(isNotSolid(chunk, x + 1, y, z)) {
                        for(int i = 0; i < 12; i += 3) {
                            vertices.add(eastFace[i] + x);
                            vertices.add(eastFace[i + 1] + y);
                            vertices.add(eastFace[i + 2] + z);
                        }
                        for(int i = 0; i < 6; i++) {
                            indices.add(vertices.size() - e + faceInds[i]);
                        }
                    }

                    //Check for +y | top
                    if(isNotSolid(chunk, x, y + 1, z)) {
                        for(int i = 0; i < 12; i += 3) {
                            vertices.add(topFace[i] + x);
                            vertices.add(topFace[i + 1] + y);
                            vertices.add(topFace[i + 2] + z);
                        }
                        for(int i = 0; i < 6; i++) {
                            indices.add(vertices.size() - e + faceInds[i]);
                        }
                    }

                    //Check for -y | bottom
                    if(isNotSolid(chunk, x, y - 1, z)) {
                        for(int i = 0; i < 12; i += 3) {
                            vertices.add(bottomFace[i] + x);
                            vertices.add(bottomFace[i + 1] + y);
                            vertices.add(bottomFace[i + 2] + z);
                        }
                        for(int i = 0; i < 6; i++) {
                            indices.add(vertices.size() - e + faceInds[i]);
                        }
                    }
                }
            }
        }

        //Java please give us a default .toPrimitive() function for giving back a primitive array like T[] with T being the type the arraylist was initialised as
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.size());
        for(float point: vertices) {
            vertexBuffer.put(point);
        }
        vertexBuffer.flip();

        for(int i = 0; i < vertices.size() - 1; i++) {
            texCoords.add(0f);
        }
        FloatBuffer texBuffer = MemoryUtil.memAllocFloat(texCoords.size());
        for(float texCoord: texCoords) {
            texBuffer.put(texCoord);
        }
        texBuffer.flip();

        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.size());
        indexBuffer.put(indices.stream().mapToInt(i -> i).toArray());
        indexBuffer.flip();

        return new ChunkMesh(vertexBuffer, indexBuffer, texBuffer);
    }

    private static boolean isNotSolid(Chunk chunk, int x, int y, int z) {
        // Check if coordinates are within bounds
        if (x < 0 || x >= Chunk.SIZE || y < 0 || y >= Chunk.SIZE || z < 0 || z >= Chunk.SIZE) {
            return true;
        }
        return true;
        //return chunk.getBlock(x, y, z) == 0;
    }
}
