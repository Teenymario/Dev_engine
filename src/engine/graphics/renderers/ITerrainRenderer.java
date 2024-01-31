package engine.graphics.renderers;

import engine.Terrain.Terrain;
import engine.objects.Light;

import java.util.List;

public interface ITerrainRenderer extends IRendererBase {

    void prepareInstance(Terrain terrain, Light light);

    void render(List<Terrain> terrains, Light light);

}
