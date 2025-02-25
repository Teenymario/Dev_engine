package engine.content;
import engine.graphics.models.BlockModel;
import engine.graphics.models.ModelType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static main.main.concat;
import static main.main.resourceManager;

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
            blocks.add(block);
            block.setID((short) blocks.size());

            System.out.println(concat("Registered block ", registry));
        } else {
            throw new IllegalStateException(concat("Registry \"", registry, "\" already exists | BlockManager"));
        }
    }

    public short getID(String registry) {
        return (short) registries.indexOf(registry);
    }

    public String getRegistry(short ID) {
        return registries.get(ID - 1);
    }

    public BlockBase getBlockByReg(String registry) {
        return blocks.get(registries.indexOf(registry));
    }

    public BlockBase getBlockByID(short ID) {
        return blocks.get(ID - 1);
    }

    //Run after all blocks are registered
    public void registerBlockModels() {
        int index = 0;
        String line;
        String[] tokens;

        for(String registry : registries) {
            BlockModel model = new BlockModel();
            int[] faces = new int[6];

            try(BufferedReader reader = new BufferedReader(new FileReader(concat("resources/models/blocks/", registry.split(":")[1], ".model")))) {
                while((line = reader.readLine()) != null) {
                    tokens = line.split(" ");

                    switch(tokens[0]) {
                        case "type":
                            switch(tokens[1]) {
                                case "gas":
                                    model.setType(ModelType.GAS);
                                    return;
                                case "full":
                                    model.setType(ModelType.FULL);
                                    break;
                                case "slab":
                                    model.setType(ModelType.SLAB);
                                    break;
                                case "stair":
                                    model.setType(ModelType.STAIR);
                                    break;
                                case "custom":
                                    model.setType(ModelType.CUSTOM);
                                    break;
                            }
                        case "north":
                            faces[0] = resourceManager.getRegistryIndex(tokens[1]) ;
                            break;
                        case "south":
                            faces[1] = resourceManager.getRegistryIndex(tokens[1]);
                            break;
                        case "west":
                            faces[2] = resourceManager.getRegistryIndex(tokens[1]);
                            break;
                        case "east":
                            faces[3] = resourceManager.getRegistryIndex(tokens[1]);
                            break;
                        case "top":
                            faces[4] = resourceManager.getRegistryIndex(tokens[1]);
                            break;
                        case "bottom":
                            faces[5] = resourceManager.getRegistryIndex(tokens[1]);
                            break;
                        case "all":
                            Arrays.fill(faces, resourceManager.getRegistryIndex(tokens[1]));
                            break;
                        case "transparent":
                            model.setTransparent();
                            break;
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }

            model.setTextureCoords(faces);
            blocks.get(index).setModel(model);
            index++;
        }
        System.gc();
    }

    public static BlockManager getInstance() {
        return singleton;
    }
}