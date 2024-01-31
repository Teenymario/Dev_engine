package engine.graphics;

import engine.graphics.renderers.IGameObjectRenderer;
import engine.graphics.renderers.ITerrainRenderer;
import engine.objects.GameObject;
import engine.objects.Light;
import main.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer<obj extends IGameObjectRenderer, terrain extends ITerrainRenderer> {

    private obj objRenderer;
    private terrain terrainRenderer;
    private Map<Integer, List<GameObject>> objects = new HashMap<Integer, List<GameObject>>();

    public MasterRenderer(obj objRenderer, terrain terrainRenderer) {
        this.objRenderer = objRenderer;
        this.terrainRenderer = terrainRenderer;
    }

    public void render(Light light) {
        objRenderer.render(objects, light);
        terrainRenderer.render(main.terrains, light);
    }

    public void processObject(GameObject object) {
        int mesh = object.meshID;
        List<GameObject> batch = objects.get(mesh);
        if(batch != null) {
            batch.add(object);
        } else {
            List<GameObject> newBatch = new ArrayList<GameObject>();
            newBatch.add(object);
            objects.put(mesh, newBatch);
        }
    }

    public void destroy() {
        objRenderer.getShader().destroy();
        terrainRenderer.getShader().destroy();
    }

}
