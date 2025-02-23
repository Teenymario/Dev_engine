package engine.graphics;

import engine.graphics.renderers.IChunkRenderer;
import engine.graphics.renderers.IGUIRendererBase;
import engine.graphics.renderers.ITerrainRenderer;
import engine.objects.Light;
import main.main;

public class MasterRenderer<obj extends IChunkRenderer, terrain extends ITerrainRenderer, gui extends IGUIRendererBase> {

    private obj chunkRenderer;
    private terrain terrainRenderer;
    private gui guiRenderer;

    public MasterRenderer(obj objRenderer, terrain terrainRenderer, gui guiRenderer) {
        this.chunkRenderer = objRenderer;
        this.terrainRenderer = terrainRenderer;
        this.guiRenderer = guiRenderer;
    }

    public void render(Light light) {
        chunkRenderer.render(main.chunks);
        terrainRenderer.render(main.world.chunks, light);
        guiRenderer.render(main.guis);
    }

    public void destroy() {
        chunkRenderer.getShader().destroy();
        terrainRenderer.getShader().destroy();
        guiRenderer.getShader().destroy();
    }

}
