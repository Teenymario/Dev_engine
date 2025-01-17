package engine.content;

import engine.world.terrain.BlockBase;

import java.util.ArrayList;

//Singleton class used to store all the blocks within the game and their registry names.
//Also provides necessary functions to register blocks and to provide block textures for the block texture atlas
public class BlockManager {
    public static BlockManager singleton;
    public ArrayList<BlockBase> blocks = new ArrayList<>();

    public BlockManager() {
        System.out.println("Initialised block manager");
        singleton = this;
    }

    public static BlockManager getInstance() {
        return singleton;
    }
}
