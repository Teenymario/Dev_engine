package engine.content;

import java.util.ArrayList;

import static main.main.concat;

//Singleton class used to store all the blocks within the game and their registry names.
//Also provides necessary functions to register blocks and to provide block textures for the block texture atlas
public class BlockManager {
    private static final BlockManager singleton = new BlockManager();
    private final ArrayList<BlockBase> blocks = new ArrayList<>();      //List of all blocks that are registered in the game
    private final ArrayList<String> registries = new ArrayList<>();     //List of all registries ordered by index to allow linking to ID

    private BlockManager() {}

    public void register(BlockBase block, String registryClass, String registry/*, BlockModel model*/) {
        registry = concat(registryClass, ":", registry);

        if(!registries.contains(registry)) {
            registries.add(registry);
        } else {
            throw new IllegalStateException(concat("Registry \"", registry, "\" already exists"));
        }

        blocks.add(block);
        block.setID((short) blocks.size());

        System.out.println(concat("Registered ", registryClass, ":", registry));
    }

    public short getID(String registry) {
        return (short) registries.indexOf(registry);
    }

    public String getRegistry(short ID) {
        return registries.get(ID + 1);
    }

    public static BlockManager getInstance() {
        return singleton;
    }
}
