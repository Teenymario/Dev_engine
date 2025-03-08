package engine.world.dimension;

import engine.world.generators.GeneratorBase;
import engine.world.terrain.Chunk;

public class Dimension implements IDimensionBase {
    private String registryName;
    public Chunk[] chunks;
    public GeneratorBase generator;

    public Dimension(String registryName, GeneratorBase chunkGenerator) {
        this.registryName = registryName;
        generator = chunkGenerator;
    }

    public String getRegistryName() {
        return registryName;
    }
}
