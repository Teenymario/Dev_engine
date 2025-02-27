package engine.world;

import engine.world.dimension.Dimension;

import java.util.HashMap;

import static main.main.concat;

public class DimensionManager {
    public HashMap<String, Dimension> dimensions = new HashMap<>();
    private static final DimensionManager singleton = new DimensionManager();

    public void registerDimension(Dimension dimension, String registryClass) {
        String registry = concat(registryClass, ":", dimension.getRegistryName());

        if(!dimensions.containsKey(registry)) {
            dimensions.put(registry, dimension);
        } else {
            throw new IllegalStateException(concat("Registry \"", registry, "\" already exists | DimensionManager"));
        }
    }

    public Dimension getDimension(String registry) {
        return dimensions.get(registry);
    }

    private DimensionManager() {}

    public static DimensionManager getInstance() {
        return singleton;
    }
}
