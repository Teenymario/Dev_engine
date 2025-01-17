package engine.content.blocks;

import engine.world.terrain.BlockBase;

public class stone extends BlockBase {
    public static int meshID;
    public static String registryName;

    public stone(String registryName, int meshID) {
        this.meshID = meshID;
        this.registryName = registryName;
    }

    @Override
    public void setMeshID(int meshID) {
        stone.meshID = meshID;
    }

    @Override
    public int getMeshID() {
        return meshID;
    }
}
