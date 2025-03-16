package engine.graphics;

import engine.graphics.models.ChunkMesh;
import engine.world.terrain.Chunk;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static main.DevEngine.resourceManager;
import static main.DevEngine.blockManager;

public class ChunkMesher {
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
    private static ArrayList<Short> nonSolid = new ArrayList<>();
    static {
        nonSolid.add((short) 0);
        nonSolid.add((short) 5);
    }

    private int startIndex = 0; //Leave these two here because I don't like constant variable declaration in loops
    private int vertexCount = 0;

    //It got so terrible I had to ask chatGPT for help, leaving the comments in so I can learn from this
    public ChunkMesh meshSingleChunk(Chunk chunk) {
        FloatBuffer vertices = MemoryUtil.memAllocFloat(Chunk.SIZE_CUBED * 24);  //6 faces, 4 vertices per face
        IntBuffer indices = MemoryUtil.memAllocInt(Chunk.SIZE_CUBED * 36);       //6 faces, 6 indices per face
        FloatBuffer texCoords = MemoryUtil.memAllocFloat(Chunk.SIZE_CUBED * 48); //two floats per vertex

        short block;

        for(int y = 0; y < Chunk.SIZE; y++) {
            for(int z = 0; z < Chunk.SIZE; z++) {
                for(int x = 0; x < Chunk.SIZE; x++) {
                    block = chunk.getBlock(x, y, z);
                    if(block == 0) {
                        continue;
                    }

                    //Check for -z | north
                    if(shouldAdd(chunk, x, y, z - 1, block)) {
                        addFace(vertices, indices, northInds, chunk, x, y, z);
                        addTextureCoords(texCoords, resourceManager.atlas.coordData, blockManager.getBlockByID(block).getModel().faces[0]);
                    }

                    //Check for +z | south
                    if(shouldAdd(chunk, x, y, z + 1, block)) {
                        addFace(vertices, indices, southInds, chunk, x, y, z);
                        addTextureCoords(texCoords, resourceManager.atlas.coordData, blockManager.getBlockByID(block).getModel().faces[1]);
                    }

                    //Check for -x | west
                    if(shouldAdd(chunk, x - 1, y, z, block)) {
                        addFace(vertices, indices, westInds, chunk, x, y, z);
                        addTextureCoords(texCoords, resourceManager.atlas.coordData, blockManager.getBlockByID(block).getModel().faces[2]);
                    }

                    //Check for +x | east
                    if(shouldAdd(chunk, x + 1, y, z, block)) {
                        addFace(vertices, indices, eastInds, chunk, x, y, z);
                        addTextureCoords(texCoords, resourceManager.atlas.coordData, blockManager.getBlockByID(block).getModel().faces[3]);
                    }

                    //Check for +y | top
                    if(shouldAdd(chunk, x, y + 1, z, block)) {
                        addFace(vertices, indices, topInds, chunk, x, y, z);
                        addTextureCoords(texCoords, resourceManager.atlas.coordData, blockManager.getBlockByID(block).getModel().faces[4]);
                    }

                    //Check for -y | bottom
                    if(shouldAdd(chunk, x, y - 1, z, block)) {
                        addFace(vertices, indices, bottomInds, chunk, x, y, z);
                        addTextureCoords(texCoords, resourceManager.atlas.coordData, blockManager.getBlockByID(block).getModel().faces[5]);
                    }

                    //End of block check
                }
            }
        }
        //Just so the mesher is not messed up for the next chunk mesh
        startIndex = 0;
        vertexCount = 0;

        vertices.flip();
        texCoords.flip();
        indices.flip();

        return new ChunkMesh(vertices, indices, texCoords);
    }

    private static boolean shouldAdd(Chunk chunk, int x, int y, int z, short curBlock) {
        if (x < 0 || x >= Chunk.SIZE || y < 0 || y >= Chunk.SIZE || z < 0 || z >= Chunk.SIZE) {
            return true;
        }

        short neighbor = chunk.getBlock(x, y, z);
        if(nonSolid.contains(neighbor) && nonSolid.contains(curBlock)) {
            return false;
        }
        return nonSolid.contains(neighbor);
    }

    private void addFace(FloatBuffer vertices, IntBuffer indices, int[] faceInds, Chunk chunk, int x, int y, int z) {
        startIndex = vertexCount; // Save the starting index before adding new vertices

        for (int i = 0; i < 6; i++) { // Always 6 indices per face
            int vertIndex = faceInds[i];

            //Add vertex position
            vertices.put(cubeVerts[vertIndex * 3] + chunk.visualPos.x + x);
            vertices.put(cubeVerts[vertIndex * 3 + 1] + chunk.visualPos.y + y);
            vertices.put(cubeVerts[vertIndex * 3 + 2] + chunk.visualPos.z + z);

            // Track new vertex count
            vertexCount++;
        }

        //Assign indices relative to the new vertices
        for (int i = 0; i < 6; i++) {
            indices.put(startIndex + i);
        }
    }

    private static void addTextureCoords(FloatBuffer texCoords, float[] coordData, int curFace) {
        texCoords.put(coordData[curFace * 4 + 2]);
        texCoords.put(coordData[curFace * 4 + 1]);
        texCoords.put(coordData[curFace * 4]);
        texCoords.put(coordData[curFace * 4 + 3]);
        texCoords.put(coordData[curFace * 4 + 2]);
        texCoords.put(coordData[curFace * 4 + 3]);
        texCoords.put(coordData[curFace * 4 + 2]);
        texCoords.put(coordData[curFace * 4 + 1]);
        texCoords.put(coordData[curFace * 4]);
        texCoords.put(coordData[curFace * 4 + 1]);
        texCoords.put(coordData[curFace * 4]);
        texCoords.put(coordData[curFace * 4 + 3]);
    }
}