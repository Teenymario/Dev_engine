package engine.content;

import engine.graphics.models.BlockModel;

import static engine.maths.Vector3.Vector3i;

public abstract class BlockBase implements IBlockBase {
    private Vector3i pos = new Vector3i(0, 0, 0);

    public BlockBase() {}

    public abstract BlockModel getModel();

    public abstract void setModel(BlockModel model);

    public abstract short getID();

    public abstract void setID(short ID);

    @Override
    public Vector3i getPos() {
        return pos;
    }

    @Override
    public void setPos(int x, int y, int z) {
        pos.redefine(x, y, z);
    }

    //Not suggested for use, can cause unnecessary overhead with the garbage collector
    @Override
    @Deprecated
    public void setPos(Vector3i newPos) {
        pos = newPos;
    }
}