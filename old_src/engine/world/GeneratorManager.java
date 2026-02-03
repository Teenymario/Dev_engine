package engine.world;

import engine.utils.LogLevel;
import engine.world.generators.GeneratorBase;
import main.DevEngine;

import java.util.ArrayList;

import static main.DevEngine.concat;

//This class exists basically just to let you retrieve generators with a registry key
public class GeneratorManager {
    private static final GeneratorManager singleton = new GeneratorManager();
    private final ArrayList<GeneratorBase> generators = new ArrayList<>();      //List of all generator that are registered in the game
    private final ArrayList<String> registries = new ArrayList<>();     //List of all registries ordered by index to allow linking to ID

    private GeneratorManager() {}

    public void register(GeneratorBase generator, String registryClass, String registry) {
        registry = concat(registryClass, ":", registry);

        if(!registries.contains(registry)) {
            registries.add(registry);
            generators.add(generator);

            if(DevEngine.logLevel == LogLevel.DEBUG) {
                System.out.println(concat("Registered generator ", registry));
            }
        } else {
            throw new IllegalStateException(concat("Registry \"", registry, "\" already exists | BlockManager"));
        }
    }

    public GeneratorBase getGenerator(String registry) {
        return generators.get(registries.indexOf(registry));
    }

    public static GeneratorManager getInstance() {
        return singleton;
    }
}
