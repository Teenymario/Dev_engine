package engine.world;

import engine.utils.LogLevel;
import engine.world.dimension.Dimension;
import main.DevEngine;

import java.util.HashMap;

import static main.DevEngine.concat;

public class DimensionManager {
    public HashMap<String, Dimension> dimensions = new HashMap<>();
    private static final DimensionManager singleton = new DimensionManager();

    public void registerDimension(Dimension dimension, String registryClass) {
        String registry = concat(registryClass, ":", dimension.getRegistryName());

        if(!dimensions.containsKey(registry)) {
            dimensions.put(registry, dimension);

            if(DevEngine.logLevel == LogLevel.DEBUG) {
                System.out.println(concat("Registered dimension ", registry));
            }
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
