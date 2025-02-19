package engine.graphics;

import engine.content.BlockBase;
import engine.graphics.renderers.IBlockRenderer;
import engine.graphics.renderers.IGUIRendererBase;
import engine.graphics.renderers.ITerrainRenderer;
import engine.objects.Light;
import main.main;

import java.util.ArrayList;

public class MasterRenderer<obj extends IBlockRenderer, terrain extends ITerrainRenderer, gui extends IGUIRendererBase> {

    private obj blockRenderer;
    private terrain terrainRenderer;
    private gui guiRenderer;
    public final ArrayList<BlockBase> blocks = new ArrayList<>();

    public MasterRenderer(obj objRenderer, terrain terrainRenderer, gui guiRenderer) {
        this.blockRenderer = objRenderer;
        this.terrainRenderer = terrainRenderer;
        this.guiRenderer = guiRenderer;
    }

    public void render(Light light) {
        blockRenderer.render(blocks);
        terrainRenderer.render(main.world.chunks, light);
        guiRenderer.render(main.guis);
    }

    public void destroy() {
        blockRenderer.getShader().destroy();
        terrainRenderer.getShader().destroy();
        guiRenderer.getShader().destroy();
    }

}
