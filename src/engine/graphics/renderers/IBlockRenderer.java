package engine.graphics.renderers;

import engine.content.BlockBase;

import java.util.ArrayList;

public interface IBlockRenderer extends IRendererBase {

    void render(ArrayList<BlockBase> block);

}
