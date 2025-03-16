package engine.world.terrain;

import engine.graphics.ChunkMesher;
import engine.graphics.models.ChunkMesh;

import static engine.maths.Vector3.Vector3i;

public class Chunk {
    public static final int SIZE = 16;
    public static final int SIZE_SQUARED = SIZE * SIZE;
    public static final int SIZE_CUBED = SIZE_SQUARED * SIZE;

    public short[] blocks = new short[SIZE_CUBED];
    public Vector3i pos;
    public Vector3i visualPos;  //This is here because I dont want to constantly multiply and divide by 2 since opengl likes to render chunks apart if the positioning is 16 blocks
    public ChunkMesh mesh;

    //Binary shifting instead of multiplication
    public static final int SHIFT = Integer.numberOfTrailingZeros(SIZE);
    public static final int SHIFT_DOUBLE = SHIFT * 2;

    public Chunk(int x, int y, int z) {
        pos = new Vector3i(x * SIZE, y * SIZE, z * SIZE);
        visualPos = new Vector3i(x * SIZE / 2, y * SIZE / 2, z * SIZE / 2);
    }

    public void changePos(int x, int y, int z) {
        pos.redefine(x * SIZE, y * SIZE, z * SIZE);
        visualPos.redefine(x * SIZE / 2, y * SIZE / 2, z * SIZE / 2);
    }

    public short getBlock(int x, int y, int z) {
        try {
            return blocks[(y << SHIFT_DOUBLE) + (z << SHIFT) + x];
        } catch(ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public void setBlock(int x, int y, int z, short ID) {
        blocks[(y << SHIFT_DOUBLE) + (z << SHIFT) + x] = ID;
    }
}
