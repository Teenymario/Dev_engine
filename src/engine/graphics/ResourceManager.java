package engine.graphics;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static main.main.concat;

public class ResourceManager {
    private static final ResourceManager singleton = new ResourceManager();
    public final HashMap<String, Texture> textures = new LinkedHashMap<>();
    public TextureAtlas atlas;

    private ResourceManager() {}

    public void register(String path, String registryClass, String registry) {
        registry = concat(registryClass + ":" + registry);

        if(!textures.containsKey(registry)) {
            textures.put(registry, new Texture(path));

            System.out.println(concat("Registered texture ", registry));
        } else {
            throw new IllegalStateException(concat("Registry \"", registry, "\" already exists | ResourceManager"));
        }
    }

    public void registerAtlas() {
        atlas = new TextureAtlas(textures.values().toArray(new Texture[0]));
        //textures.clear();     Uncomment once shaders are updated to handle an atlas
    }

    public Texture getTexture(String registry) {
        return textures.get(registry);
    }

    public static ResourceManager getInstance() {
        return singleton;
    }
}
