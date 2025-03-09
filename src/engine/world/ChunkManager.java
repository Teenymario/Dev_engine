package engine.world;

import engine.world.generators.GeneratorBase;
import engine.world.terrain.Chunk;

import java.util.ArrayList;

import static engine.maths.Vector3.Vector3i;

import static main.main.camera;

public class ChunkManager {
    private static final ChunkManager singleton = new ChunkManager();
    public String world;
    public GeneratorBase generator;
    public int DISTANCE;
    public int DISTANCE_SQUARED;
    public int DISTANCE_CUBED;
    public int DISTANCE_DIV2;

    //Ranges for checking
    public int DISTANCE_RANGE_START_X;
    public int DISTANCE_RANGE_END_X;
    public int DISTANCE_RANGE_START_Y;
    public int DISTANCE_RANGE_END_Y;
    public int DISTANCE_RANGE_START_Z;
    public int DISTANCE_RANGE_END_Z;

    public Chunk[] chunks;

    public void setup(String registry, int distance) {
        DISTANCE = distance;
        DISTANCE_SQUARED = distance * distance;
        DISTANCE_CUBED = distance * distance * distance;
        DISTANCE_DIV2 = distance / 2;
        calcRange();

        world = registry;
        generator = DimensionManager.getInstance().getDimension(registry).generator;

        chunks = new Chunk[DISTANCE_CUBED];
        firstGen();
    }

    public void setDistance(int distance) {
        DISTANCE = distance;
        DISTANCE_SQUARED = distance * distance;
        DISTANCE_CUBED = distance * distance * distance;
        DISTANCE_DIV2 = distance / 2;
        calcRange();

        chunks = new Chunk[DISTANCE_CUBED];
        firstGen();
    }

    public void changeWorld(String registry) {
        world = registry;
        generator = DimensionManager.getInstance().getDimension(registry).generator;

        chunks = new Chunk[DISTANCE_CUBED];
        firstGen();
    }

    public void loadChunks() {
        calcRange();
        ArrayList<Integer> indices = new ArrayList<>();
        Vector3i chunkPos = new Vector3i(0, 0, 0);
        boolean shift = false;

        for(int i = 0; i < chunks.length; i++) {
            chunkPos.redefine(chunks[i].pos.x / 16, chunks[i].pos.y / 16, chunks[i].pos.z / 16);

            //Little range check to see if chunks still fall within the render distance
            if(chunkPos.x < DISTANCE_RANGE_START_X) {
                chunkPos.x = DISTANCE_RANGE_END_X;
                shift = true;
            } else if(DISTANCE_RANGE_END_X < chunkPos.x) {
                chunkPos.x = DISTANCE_RANGE_START_X;
                shift = true;
            }
            if(chunkPos.y < DISTANCE_RANGE_START_Y) {
                chunkPos.y = DISTANCE_RANGE_END_Y;
                shift = true;
            } else if(DISTANCE_RANGE_END_Y < chunkPos.y) {
                chunkPos.y = DISTANCE_RANGE_START_Y;
                shift = true;
            }
            if(chunkPos.z < DISTANCE_RANGE_START_Z) {
                chunkPos.z = DISTANCE_RANGE_END_Z;
                shift = true;
            } else if(DISTANCE_RANGE_END_Z < chunkPos.z) {
                chunkPos.z = DISTANCE_RANGE_START_Z;
                shift = true;
            }

            if(shift) {
                indices.add(i);
                chunks[i].changePos(chunkPos.x, chunkPos.y, chunkPos.z);
                shift = false;
            }
        }

        generator.generateChunks(chunks, indices);
    }

    private void firstGen() {
        int xOff = (int) Math.ceil(camera.pos.x / Chunk.SIZE) - DISTANCE_DIV2;
        int yOff = (int) Math.ceil(camera.pos.y / Chunk.SIZE) - DISTANCE_DIV2;
        int zOff = (int) Math.ceil(camera.pos.z / Chunk.SIZE) - DISTANCE_DIV2;

        int counter = 0;
        for(int y = 0; y < DISTANCE; y++) {
            for(int z = 0; z < DISTANCE; z++) {
                for(int x = 0; x < DISTANCE; x++) {
                    chunks[counter] = new Chunk(x + xOff, y + yOff, z + zOff);
                    counter++;
                }
            }
        }

        generator.generateChunks(chunks);
    }

    public void calcRange() {
        int xOff = (int) Math.ceil(camera.pos.x / Chunk.SIZE) - DISTANCE_DIV2;
        int yOff = (int) Math.ceil(camera.pos.y / Chunk.SIZE) - DISTANCE_DIV2;
        int zOff = (int) Math.ceil(camera.pos.z / Chunk.SIZE) - DISTANCE_DIV2;

        DISTANCE_RANGE_START_X = xOff;
        DISTANCE_RANGE_END_X = xOff + DISTANCE - 1;
        DISTANCE_RANGE_START_Y = yOff;
        DISTANCE_RANGE_END_Y = yOff + DISTANCE - 1;
        DISTANCE_RANGE_START_Z = zOff;
        DISTANCE_RANGE_END_Z = zOff + DISTANCE - 1;
    }

    private int index(int x, int y, int z) {
        return x + (z << DISTANCE) + (y << DISTANCE_SQUARED);
    }

    public void unloadChunks() {

    }

    private ChunkManager() {}

    public static ChunkManager getInstance() {
        return singleton;
    }
}
