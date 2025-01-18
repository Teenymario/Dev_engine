package engine.content;

import engine.graphics.models.blocks.BlockModel;
import engine.maths.Vector3i;

public interface IBlockBase {
    short ID = 0;
    BlockModel model = null;

    BlockModel getModel();
    void setModel(BlockModel model);

    short getID();

    static void setID(short ID) {}

    Vector3i getPos();
    void setPos(int x, int y, int z);
    void setPos(Vector3i newPos);
}
