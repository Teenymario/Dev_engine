package engine.graphics;

import engine.graphics.renderers.IChunkRenderer;
import engine.graphics.renderers.IGUIRendererBase;
import engine.world.ChunkManager;
import main.DevEngine;

public class MasterRenderer<obj extends IChunkRenderer, gui extends IGUIRendererBase> {

    private obj chunkRenderer;
    private gui guiRenderer;

    public MasterRenderer(obj objRenderer, gui guiRenderer) {
        this.chunkRenderer = objRenderer;
        this.guiRenderer = guiRenderer;
    }

    public void render() {
        chunkRenderer.render(ChunkManager.getInstance().chunks);
        guiRenderer.render(DevEngine.guis);
    }

    public void destroy() {
        chunkRenderer.getShader().destroy();
        guiRenderer.getShader().destroy();
    }

}
