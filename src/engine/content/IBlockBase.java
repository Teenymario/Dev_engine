package engine.content;

import engine.graphics.models.BlockModel;
import engine.maths.Vector3i;

public interface IBlockBase {
    BlockModel getModel();
    void setModel(BlockModel model);

    short getID();

    static void setID(short ID) {}

    Vector3i getPos();
    void setPos(int x, int y, int z);
    void setPos(Vector3i newPos);
}