package engine.world;

import engine.world.generators.GeneratorBase;
import engine.world.terrain.Chunk;

import java.util.ArrayList;

import static main.main.camera;

public class ChunkManager {
    private static final ChunkManager singleton = new ChunkManager();
    public String world;
    public GeneratorBase generator;
    public int DISTANCE;
    public int DISTANCE_SQUARED;
    public int DISTANCE_CUBED;
    public int DISTANCE_DIV2;
    public Chunk[] chunks;

    public void setup(String registry, int distance) {
        DISTANCE = distance;
        DISTANCE_SQUARED = distance * distance;
        DISTANCE_CUBED = distance * distance * distance;
        DISTANCE_DIV2 = distance / 2;
        world = registry;
        generator = DimensionManager.getInstance().getDimension(registry).generator;

        chunks = new Chunk[DISTANCE * DISTANCE * DISTANCE];
        loadChunks(0, 0, 0);
    }

    public void setDistance(int distance) {
        DISTANCE = distance;
        DISTANCE_SQUARED = distance * distance;
        DISTANCE_CUBED = distance * distance * distance;
        DISTANCE_DIV2 = distance / 2;

        chunks = new Chunk[DISTANCE_CUBED];
        loadChunks(0, 0, 0);
    }

    public void changeWorld(String registry) {
        world = registry;
        generator = DimensionManager.getInstance().getDimension(registry).generator;

        chunks = new Chunk[DISTANCE * DISTANCE * DISTANCE];
        loadChunks(0, 0, 0);
    }

    public void loadChunks(int shiftX, int shiftY, int shiftZ) {
        int xOff = Math.round(camera.pos.x / Chunk.SIZE) - DISTANCE_DIV2;
        int yOff = Math.round(camera.pos.y / Chunk.SIZE) - DISTANCE_DIV2;
        int zOff = Math.round(camera.pos.z / Chunk.SIZE) - DISTANCE_DIV2;

        int counter = 0;
        for(int x = 0; x < DISTANCE; x++) {
            for(int y = 0; y < DISTANCE; y++) {
                for(int z = 0; z < DISTANCE; z++) {
                    chunks[counter] = new Chunk(x + xOff, y + yOff, z + zOff);
                    counter++;
                }
            }
        }

        generator.generateChunks(chunks);
    }

    public void unloadChunks() {

    }

    private ChunkManager() {}

    public static ChunkManager getInstance() {
        return singleton;
    }
}
