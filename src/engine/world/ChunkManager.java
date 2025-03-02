package engine.world;

import engine.world.terrain.Chunk;

public class ChunkManager {
    private static final ChunkManager singleton = new ChunkManager();
    public String world;
    public int renderDistance;
    public Chunk[] chunks;

    public void setDistance(int distance) {
        renderDistance = distance;
        int[] chunks = new int[distance * distance * distance];
        loadChunks();
    }

    public void changeWorld(String registry) {
        world = registry;
        int[] chunks = new int[renderDistance * renderDistance * renderDistance];
        loadChunks();
    }

    public void loadChunks() {

    }

    public void unloadChunks() {

    }

    private ChunkManager() {};

    public static ChunkManager getInstance() {
        return singleton;
    }
}
