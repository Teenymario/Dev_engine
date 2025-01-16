package engine.graphics.renderers;

import engine.world.terrain.BlockBase;
import engine.world.terrain.Chunk;
import engine.objects.Light;

public interface ITerrainRenderer extends IRendererBase {

    void prepareInstance(BlockBase block, Light light);

    void render(Chunk[] chunks, Light light);

}
