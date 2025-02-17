package engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static main.main.concat;

public class ResourceManager {
    private static final ResourceManager singleton = new ResourceManager();
    public final HashMap<String, Texture> textures = new LinkedHashMap<>();
    public TextureAtlas atlas;
    public int atlasID;
    public int coordSSBO;

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

        //Upload atlas texture
        atlasID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, atlasID);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, atlas.size, atlas.size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, atlas.imgData);

        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL30.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        //Upload texture coords
        coordSSBO = GL30.glGenBuffers();
        GL30.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, coordSSBO);  // Bind SSBO
        GL30.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, atlas.coordData, GL15.GL_STATIC_DRAW);
        GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 0, coordSSBO);
        GL30.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);// Unbind

        //textures.clear();     Uncomment once shaders are updated to handle an atlas
    }

    public Texture getTexture(String registry) {
        return textures.get(registry);
    }

    public static ResourceManager getInstance() {
        return singleton;
    }
}
