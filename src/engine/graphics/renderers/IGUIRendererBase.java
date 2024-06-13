package engine.graphics.renderers;

import engine.graphics.Shader;
import engine.objects.GUITexture;

import java.util.List;

public interface IGUIRendererBase {

    void render(List<GUITexture> GUIs);

    Shader getShader();

}
