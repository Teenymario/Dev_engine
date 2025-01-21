package engine.content.blocks;

import engine.content.BlockBase;
import engine.graphics.models.BlockModel;

public class dirt extends BlockBase {
    public static Short ID;
    public static BlockModel model;

    @Override
    public BlockModel getModel() {
        return model;
    }

    @Override
    public void setModel(BlockModel model) {
        this.model = model;
    }

    @Override
    public short getID() {
        return ID;
    }

    @Override
    public void setID(short ID) {
        this.ID = ID;
    }
}
