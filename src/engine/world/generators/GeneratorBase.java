package engine.world.generators;

import engine.world.terrain.Chunk;

import java.util.ArrayList;

public abstract class GeneratorBase {
    public long seed;
    public int minY;
    public int maxY;
    public static float FREQUENCY = 0.5f / 24f;

    public abstract GeneratorBase generateChunk(Chunk chunk);
    public abstract GeneratorBase generateChunks(Chunk[] chunks);
    public abstract GeneratorBase generateChunks(Chunk[] chunks, int offset, int length);
    public abstract GeneratorBase generateChunks(Chunk[] chunks, int[] indices);
    public abstract GeneratorBase generateChunks(Chunk[] chunks, ArrayList<Integer> indices);

}
