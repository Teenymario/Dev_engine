package main;

import engine.IO.Input;
import engine.IO.Window;
import engine.Terrain.Terrain;
import engine.graphics.*;
import engine.graphics.renderers.GameObjectRenderer;
import engine.graphics.renderers.TerrainRenderer;
import engine.maths.Vector3f;
import engine.objects.Camera;
import engine.objects.GameObject;
import engine.objects.Light;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class main implements Runnable {
    public Thread game;
    public Window window;
    public Shader objShader;
    public Shader terrainShader;
    public GameObjectRenderer objRenderer;
    public TerrainRenderer terrainRenderer;
    public MasterRenderer<GameObjectRenderer, TerrainRenderer> masterRenderer;
    public static List<GameObject> objectMasterList = new ArrayList<>();
    public static List<Terrain> terrains = new ArrayList<>();
    public static List<Mesh> meshes;

    //Rendering values
    public final int WIDTH = 1280, HEIGHT = 760;
    public static float fov = 100.0f;
    public static float near = 0.005f;
    public static float far = 100000.0f;
    public static float speed = 0.025f, sensitivity = 0.5f;

    //Game values
    public boolean thirdPerson;
    public static boolean sprinting;
    public static float speedModifier;
    public static boolean lightLocked;

    //Frame counting
    public static long lastTime;
    public static long counter;

    //GameObject
    public GameObject object;
    public static Camera camera;
    public Light light;
    public GameObject lightCube;



    public void start() {
        game = new Thread(this, "game");
        game.start();
    }

    public void init() {
        System.out.println("Initializing game");
        window = new Window(WIDTH, HEIGHT, "game");
        window.setBackgroundColor(0, 0, 0);
        window.create();

        objShader = new Shader("/shaders/objVert.glsl", "/shaders/objFrag.glsl");
        objShader.create();
        objRenderer = new GameObjectRenderer(window, objShader);

        terrainShader = new Shader("/shaders/terrainVert.glsl", "/shaders/terrainFrag.glsl");
        terrainShader.create();
        terrainRenderer = new TerrainRenderer(window, terrainShader);

        masterRenderer = new MasterRenderer<>(objRenderer, terrainRenderer);

        meshes = new ArrayList<>();

        //Meshes
        ObjectMesh.construct("resources/models/TieInterceptor.obj").constructMesh();
        ObjectMesh.construct("resources/models/light.obj").constructMesh();
        //Meshes

        //GameObjects
        object = new GameObject(0, new Vector3f(0, 0.5f, -1), new Vector3f(0, 0, 0), new Vector3f(2, 2, 2));
        camera = new Camera(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
        light = new Light(new Vector3f(0, 1, 1), new Vector3f(1, 1, 1));
        lightCube = new GameObject(1, light.pos, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

        terrains.add(new Terrain(0, 0, "/textures/grass_top.png"));
        //terrains.add(new Terrain(1, 0, "/textures/grass_top.png"));
        //GameObjects

        objectMasterList.add(object);
        objectMasterList.add(lightCube);
        genObjects(0);

        for(GameObject object : objectMasterList) {
            masterRenderer.processObject(object);
        }

        thirdPerson = false;
        speedModifier = 1;
        sprinting = false;
        lightLocked = false;
        lastTime = System.currentTimeMillis();

        System.gc();
    }

    public void run() {
        init();
        while(!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            if(Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
            if(Input.isKeyDown(GLFW.GLFW_KEY_L)) window.mouseState(true);
            if(Input.isKeyDown(GLFW.GLFW_KEY_U)) window.mouseState(false);
            if(Input.isKeyDown(GLFW.GLFW_KEY_C)){
                camera.setRot(new Vector3f(0, 0, 0));
                camera.setPos(new Vector3f(0, 1, 0));
            }
            if(Input.isKeyDown(GLFW.GLFW_KEY_F5)) thirdPerson = !thirdPerson;
            if(Input.isKeyDown(GLFW.GLFW_KEY_F6)) lightLocked = !lightLocked;
            if(Input.isKeyDown(GLFW.GLFW_KEY_Q)) speedModifier = speedModifier * 1.02f;
            if(Input.isKeyDown(GLFW.GLFW_KEY_E)) speedModifier =  speedModifier / 1.02f;
            sprinting = Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL);

            if(Input.isKeyDown(GLFW.GLFW_KEY_UP)) {
                light.pos.z -= 0.025f * speedModifier;
                objectMasterList.get(1).setPos(light.pos.x, light.pos.y, light.pos.z);
            }
            if(Input.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
                light.pos.z += 0.025f * speedModifier;
                objectMasterList.get(1).setPos(light.pos.x, light.pos.y, light.pos.z);
            }
            if(Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
                light.pos.x -= 0.025f * speedModifier;
                objectMasterList.get(1).setPos(light.pos.x, light.pos.y, light.pos.z);
            }
            if(Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
                light.pos.x += 0.025f * speedModifier;
                objectMasterList.get(1).setPos(light.pos.x, light.pos.y, light.pos.z);
            }
            if(Input.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                light.pos.y += 0.025f * speedModifier;
                objectMasterList.get(1).setPos(light.pos.x, light.pos.y, light.pos.z);
            }
            if(Input.isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL)) {
                light.pos.y -= 0.025f * speedModifier;
                objectMasterList.get(1).setPos(light.pos.x, light.pos.y, light.pos.z);
            }
            if(lightLocked) {
                light.pos.x = camera.pos.x;
                light.pos.y = camera.pos.y + 0.5f;
                light.pos.z = camera.pos.z;
                objectMasterList.get(1).setPos(light.pos.x, light.pos.y, light.pos.z);
            }
            update();
            render();

            counter++;
            if (System.currentTimeMillis() - lastTime > 1000) {
                GLFW.glfwSetWindowTitle(window.getWindow(), window.getTitle() + " | fps: " + counter);
                lastTime = System.currentTimeMillis();
                counter = 0;
                System.gc();
            }

            GLFW.glfwPollEvents();
            window.swapBuffers();
        }
        System.out.println(counter);
        close();
    }

    private void update() {
        window.update();
        if(thirdPerson) {
            camera.update(object);
        } else {
            camera.update();
        }
    }

    private void render() {
        //3D scene
        masterRenderer.render(light);

        //2D HUD


    }

    private void close() {
        window.destroy();
        for(Mesh mesh : meshes) {
            mesh.destroy();
        }
        objShader.destroy();
        masterRenderer.destroy();
        System.out.println("Goodbye :)");
    }

    public void genObjects(int amt) {
        for(int i = 2; i < amt; i++) {
            int min = 5, max = 15;
            objectMasterList.add(new GameObject(0, new Vector3f((float) (min + (Math.random() * max)), (float) (min + (Math.random() * max)), (float) (min + (Math.random() * max))), new Vector3f((float) (Math.random() * 360) + 1, (float) (Math.random() * 360) + 1, (float) (Math.random() * 360) + 1), new Vector3f(1, 1, 1)));
        }
    }

    public static void main(String[] args) {
        new main().start();
    }
}
