package engine.graphics;

import engine.graphics.models.ChunkMesh;
import engine.world.terrain.Chunk;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static main.main.resourceManager;
import static main.main.blockManager;

public class ChunkMesher {
    /* Position utils
     *  x = i & MASK
     *  y = (i >> bitsPerCoord) & MASK
     *  z = (i >> (2 * bitsPerCoord)) & MASK
     * */
    private final int bitsPerCoord = (int) (Math.log(Chunk.SIZE) / Math.log(2));
    private final int MASK = (1 << bitsPerCoord) - 1;

    //8 vertices for a full cube
    private static final float[] cubeVerts = {
            0.5f,  0.5f,  -0.5f,
            0.5f,  -0.5f,  -0.5f,
            0.5f,  0.5f,  0.5f,
            0.5f,  -0.5f,  0.5f,
            -0.5f,  0.5f,  -0.5f,
            -0.5f,  -0.5f,  -0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  -0.5f,  0.5f
    };

    private static final int[] northInds = {
            4, 1, 5,
            4, 0, 1
    };
    private static final int[] southInds = {
            2, 7, 3,
            2, 6, 7
    };
    private static final int[] westInds = {
            6, 5, 7,
            6, 4, 5
    };
    private static final int[] eastInds = {
            0, 3, 1,
            0, 2, 3
    };
    private static final int[] topInds = {
            4, 2, 0,
            4, 6, 2
    };
    private static final int[] bottomInds = {
            1, 7, 5,
            1, 3, 7
    };
    private static int startIndex = 0; //Leave these two here because I don't like constant variable declaration in loops
    private static int vertexCount = 0;

    //It got so terrible I had to ask chatGPT for help, leaving the comments in so I can learn from this
    public static ChunkMesh meshSingleChunk(Chunk chunk) {
        ArrayList<Float> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        ArrayList<Float> texCoords = new ArrayList<>();
        float[] coordData = resourceManager.atlas.coordData;

        int curFace;
        short block;

        for(int y = 0; y < Chunk.SIZE; y++) {
            for(int z = 0; z < Chunk.SIZE; z++) {
                for(int x = 0; x < Chunk.SIZE; x++) {
                    block = chunk.getBlock(x, y, z);
                    if(block == 0) {
                        continue;
                    }

                    //Check for -z | north
                    if(isNotSolid(chunk, x, y, z - 1)) {
                        addFace(vertices, indices, northInds, chunk, x, y, z);

                        curFace = blockManager.getBlockByID(block).getModel().faces[0];

                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                    }

                    //Check for +z | south
                    if(isNotSolid(chunk, x, y, z + 1)) {
                        addFace(vertices, indices, southInds, chunk, x, y, z);

                        curFace = blockManager.getBlockByID(block).getModel().faces[1];

                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                    }

                    //Check for -x | west
                    if(isNotSolid(chunk, x - 1, y, z)) {
                        addFace(vertices, indices, westInds, chunk, x, y, z);

                        curFace = blockManager.getBlockByID(block).getModel().faces[2];

                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                    }

                    //Check for +x | east
                    if(isNotSolid(chunk, x + 1, y, z)) {
                        addFace(vertices, indices, eastInds, chunk, x, y, z);

                        curFace = blockManager.getBlockByID(block).getModel().faces[3];

                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                    }

                    //Check for +y | top
                    if(isNotSolid(chunk, x, y + 1, z)) {
                        addFace(vertices, indices, topInds, chunk, x, y, z);

                        curFace = blockManager.getBlockByID(block).getModel().faces[4];

                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                    }

                    //Check for -y | bottom
                    if(isNotSolid(chunk, x, y - 1, z)) {
                        addFace(vertices, indices, bottomInds, chunk, x, y, z);

                        curFace = blockManager.getBlockByID(block).getModel().faces[5];

                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                        texCoords.add(coordData[curFace * 4 + 2]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 1]);
                        texCoords.add(coordData[curFace * 4]);
                        texCoords.add(coordData[curFace * 4 + 3]);
                    }

                    //End of block check
                }
            }
        }
        //Just so the mesher is not messed up for the next chunk mesh
        startIndex = 0;
        vertexCount = 0;

        //Java please give us a default .toPrimitive() function for giving back a primitive array like T[] with T being the type the arraylist was initialised as
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.size());
        for(float point: vertices) {
            vertexBuffer.put(point);
        }
        vertexBuffer.flip();

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
        return chunk.getBlock(x, y, z) == 0;
    }

    private static void addFace(ArrayList<Float> vertices, ArrayList<Integer> indices, int[] faceInds, Chunk chunk, int x, int y, int z) {
        startIndex = vertexCount; // Save the starting index before adding new vertices

        for (int i = 0; i < 6; i++) { // Always 6 indices per face
            int vertIndex = faceInds[i];

            // Add vertex position
            vertices.add(cubeVerts[vertIndex * 3] + chunk.pos.x + x);
            vertices.add(cubeVerts[vertIndex * 3 + 1] + chunk.pos.y + y);
            vertices.add(cubeVerts[vertIndex * 3 + 2] + chunk.pos.z + z);

            // Track new vertex count
            vertexCount++;
        }

        // Assign indices relative to the new vertices
        for (int i = 0; i < 6; i++) {
            indices.add(startIndex + i);
        }
    }
}
