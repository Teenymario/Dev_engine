package engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;

import static main.main.concat;

public class ResourceManager {
    private static final ResourceManager singleton = new ResourceManager();
    public final ArrayList<String> registries = new ArrayList<>();
    public final ArrayList<Texture> textures = new ArrayList<>();
    public TextureAtlas atlas;
    public int atlasID;

    private ResourceManager() {}

    public void register(String path, String registryClass, String registry) {
        registry = concat(registryClass + ":" + registry);  //Registryclass will have to eventually be derived to a mod specific indicator like forge

        if(!registries.contains(registry)) {
            textures.add(new Texture(path));
            registries.add(registry);

            System.out.println(concat("Registered texture ", registry));
        } else {
            throw new IllegalStateException(concat("Registry \"", registry, "\" already exists | ResourceManager"));
        }
    }

    public void registerAtlas() {
        atlas = new TextureAtlas(textures);

        //Upload atlas texture
        atlasID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, atlasID);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, atlas.size, atlas.size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, atlas.imgData);

        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        //textures.clear();     Uncomment once shaders are updated to handle an atlas
    }

    public Texture getTexture(String registry) {
        return textures.get(registries.indexOf(registry));
    }

    public int getRegistryIndex(String registry) {
        return registries.indexOf(registry);
    }

    public static ResourceManager getInstance() {
        return singleton;
    }
}
