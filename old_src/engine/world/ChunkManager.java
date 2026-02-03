package engine.world;

import engine.graphics.ChunkMesher;
import engine.world.generators.GeneratorBase;
import engine.world.terrain.Chunk;
import main.DevEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static engine.maths.Vector3.Vector3i;

import static main.DevEngine.camera;

public class ChunkManager {
    private static final ChunkManager singleton = new ChunkManager();
    public String world;
    public volatile GeneratorBase generator;
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

    public volatile Chunk[] chunks;

    //Multithreaded generation
    public int threadCount = DevEngine.numCores / 2;
    public ExecutorService threads;
    public ArrayList<ArrayList<Integer>> workloads;
    public volatile int runningThreads;
    public ChunkMesher[] meshers;   //We make separate "meshers" just for those 2 variables, because multiple threads like to mess with static variables

    public void setup(String registry, int distance) {
        DISTANCE = distance;
        DISTANCE_SQUARED = distance * distance;
        DISTANCE_CUBED = distance * distance * distance;
        DISTANCE_DIV2 = distance / 2;
        calcRange();

        world = registry;
        generator = DimensionManager.getInstance().getDimension(registry).generator;

        chunks = new Chunk[DISTANCE_CUBED];

        threads = Executors.newFixedThreadPool(threadCount);

        workloads = new ArrayList<>();
        meshers = new ChunkMesher[threadCount];

        for(int i = 0; i < threadCount; i++) {
            workloads.add(new ArrayList<>());
            meshers[i] = new ChunkMesher();
        }

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

    public void loadChunks(int shiftX, int shiftY, int shiftZ) {
        calcRange();
        ArrayList<Integer> indices = new ArrayList<>();
        Vector3i chunkPos = new Vector3i(0, 0, 0);
        boolean shift = false;

        for(int i = 0; i < chunks.length; i++) {
            chunkPos.redefine(chunks[i].pos.x / 16, chunks[i].pos.y / 16, chunks[i].pos.z / 16);

            //Little range check to see if chunks still fall within the render distance
            if(chunkPos.x < DISTANCE_RANGE_START_X) {
                chunkPos.x = DISTANCE_RANGE_END_X + ((shiftX == 0) ? 0 : shiftX + ((shiftX < 0) ? + 1 : -1));

                shift = true;
            } else if(DISTANCE_RANGE_END_X < chunkPos.x) {
                chunkPos.x = DISTANCE_RANGE_START_X + ((shiftX == 0) ? 0 : shiftX + ((shiftX < 0) ? + 1 : -1));
                shift = true;
            }
            if(chunkPos.y < DISTANCE_RANGE_START_Y) {
                chunkPos.y = DISTANCE_RANGE_END_Y + ((shiftY == 0) ? 0 : shiftY + ((shiftY < 0) ? + 1 : -1));
                shift = true;
            } else if(DISTANCE_RANGE_END_Y < chunkPos.y) {
                chunkPos.y = DISTANCE_RANGE_START_Y + ((shiftY == 0) ? 0 : shiftY + ((shiftY < 0) ? + 1 : -1));
                shift = true;
            }
            if(chunkPos.z < DISTANCE_RANGE_START_Z) {
                chunkPos.z = DISTANCE_RANGE_END_Z + ((shiftZ == 0) ? 0 : shiftZ + ((shiftZ < 0) ? + 1 : -1));
                shift = true;
            } else if(DISTANCE_RANGE_END_Z < chunkPos.z) {
                chunkPos.z = DISTANCE_RANGE_START_Z + ((shiftZ == 0) ? 0 : shiftZ + ((shiftZ < 0) ? + 1 : -1));
                shift = true;
            }

            if(shift) {
                indices.add(i);
                chunks[i].changePos(chunkPos.x, chunkPos.y, chunkPos.z);
                shift = false;
            }
        }

        //Distribute chunks to threads
        int counter = 0;
        for(int i: indices) {
            workloads.get(counter).add(i);

            if(++counter == threadCount) {
                counter = 0;
            }
        }

        runningThreads = threadCount;
        for(int i = 0; i < threadCount; i++) {
            int index = i;
            threads.submit(() -> {
                generator.generateChunks(chunks, workloads.get(index));
                for(int j: workloads.get(index)) {
                    chunks[j].mesh = meshers[index].meshSingleChunk(chunks[j]);
                }

                DevEngine.GPUTasks.add(() -> {
                    for(int mesh : workloads.get(index)) {
                        chunks[mesh].mesh.upload();
                    }
                    workloads.get(index).clear();
                });

                runningThreads--;
            });
        }
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
        for(Chunk chunk: chunks) {
            chunk.mesh = meshers[0].meshSingleChunk(chunk);
            chunk.mesh.upload();
        }
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
