package engine.graphics.renderers;

import engine.objects.GameObject;
import engine.objects.Light;

import java.util.List;
import java.util.Map;

public interface IGameObjectRenderer extends IRendererBase{

    void prepareInstance(GameObject object, Light light);

    void render(Map<Integer, List<GameObject>> objects, Light light);

}
