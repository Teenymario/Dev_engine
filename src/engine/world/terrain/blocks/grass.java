package engine.world.terrain.blocks;

import engine.world.terrain.BlockBase;

public class grass extends BlockBase {
    public static int meshID;
    public static String registryName;

    public grass(String registryName, int meshID) {
        this.meshID = meshID;
        this.registryName = registryName;
    }

    @Override
    public void setMeshID(int meshID) {
        grass.meshID = meshID;
    }

    @Override
    public int getMeshID() {
        return meshID;
    }
}
