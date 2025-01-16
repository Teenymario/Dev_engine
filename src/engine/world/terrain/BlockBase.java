package engine.world.terrain;

public abstract class BlockBase implements IBlockBase {
    private static String registryName;

    public BlockBase() {

    }

    public String getRegistryName() {
        return registryName;
    }

    public abstract int getMeshID();
    public abstract void setMeshID(int meshID);
}
