package engine.world.terrain.blocks;

import engine.world.terrain.BlockBase;

public class dirt extends BlockBase {
    public static int meshID;
    public static String registryName;

    public dirt(String registryName, int meshID) {
        this.meshID = meshID;
        this.registryName = registryName;
    }

    @Override
    public void setMeshID(int meshID) {
        dirt.meshID = meshID;
    }

    @Override
    public int getMeshID() {
        return meshID;
    }
}
