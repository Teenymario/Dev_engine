package main;

import engine.IO.Input;
import engine.IO.Window;
import engine.content.BlockManager;
import engine.content.BlockBase;
import engine.content.blocks.*;
import engine.graphics.MasterRenderer;
import engine.graphics.Mesh;
import engine.graphics.ObjectMesh;
import engine.graphics.Shader;
import engine.graphics.renderers.GUIRenderer;
import engine.graphics.renderers.GameObjectRenderer;
import engine.graphics.renderers.TerrainRenderer;
import engine.maths.Vector3f;
import engine.objects.Camera;
import engine.objects.GUITexture;
import engine.objects.GameObject;
import engine.objects.Light;
import engine.world.dimension.Dimension;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class main implements Runnable {
    //Core values
    public Thread game;
    public Window window;
    public Shader objShader;
    public Shader terrainShader;
    public Shader guiShader;
    public GameObjectRenderer objRenderer;
    public TerrainRenderer terrainRenderer;
    public GUIRenderer guiRenderer;
    public MasterRenderer<GameObjectRenderer, TerrainRenderer, GUIRenderer> masterRenderer;
    public static List<GameObject> objectMasterList = new ArrayList<>();
    public static List<Mesh> meshes = new ArrayList<>();
    public static List<GUITexture> guis = new ArrayList<>();

    //Rendering values
    public final int WIDTH = 1280, HEIGHT = 760;
    public static float fov = 100.0f;
    public static float near = 0.0025f;
    public static float far = 50000.0f;
    public static float speed = 5.0f, sensitivity = 10.0f;

    //Game values
    public boolean thirdPerson;
    public static boolean sprinting;
    public static float speedModifier;
    public static boolean lightLocked;

    //Game ticking
    public static long lastTime;

    //GameObjects
    public GameObject object;
    public static Camera camera;
    public Light light;
    public GameObject lightCube;

    //Game world
    public static final String registryID = "devEngine";  //Allow for easier modding
    public static BlockManager blockManager;
    public static ArrayList<BlockBase> contentBlocks = new ArrayList<>();
    //public static ArrayList<ItemBase> contentItems = new ArrayList<>();
    public static Dimension world;

    //Simulation
    public static int tickRate = 20;     //Per second
    public static int frameRate = 60;    //Per second
    public static double deltaTime = 0;

    //Util
    public static final StringBuilder stringer = new StringBuilder();    //Please use the concat function. If you use this directly make sure to run .setLength(0); on the thing after you finish

    public static String concat(Object... args) {
        for (Object arg : args) {
            stringer.append(arg);
        }

        String val = stringer.toString();
        stringer.setLength(0);
        return val;
    }
    //Util

    public void start() {
        game = new Thread(this, "game");
        game.start();
    }

    public void run() {
        System.out.println("Started loading sequence");

        //Instantiate core functionality
        instantiate();

        //Load and setup content
        preInit();

        //Process graphics
        init();

        //Finalise
        postInit();
        System.gc();

        //Start the game
        long lastTime = System.nanoTime();
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        int ticks = 0;

        while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            long now = System.nanoTime();
            deltaTime = (now - lastTime) / 1000000000.0;
            lastTime = now;
            delta += deltaTime * tickRate;
            update();
            handleInput();

            while (delta >= 1) {
                tick();
                ticks++;
                delta--;
            }

            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                GLFW.glfwSetWindowTitle(window.getWindow(), window.getTitle() + " | fps: " + frames + " | tps: " + ticks);
                timer += 1000;
                frames = 0;
                ticks = 0;
            }

            GLFW.glfwPollEvents();
            window.swapBuffers();
        }
        close();
    }

    //Setup core game engine content
    private void instantiate() {
        System.out.println("- Instantiate");
        window = new Window(WIDTH, HEIGHT, "game");
        window.setBackgroundColor(0.2f, 0.2f, 0.2f);
        window.create();

        objShader = new Shader("/shaders/objVert.glsl", "/shaders/objFrag.glsl");
        objShader.create();
        objRenderer = new GameObjectRenderer(objShader);

        terrainShader = new Shader("/shaders/terrainVert.glsl", "/shaders/terrainFrag.glsl");
        terrainShader.create();
        terrainRenderer = new TerrainRenderer(terrainShader);

        guiShader = new Shader("/shaders/guiVert.glsl", "/shaders/guiFrag.glsl");
        guiShader.create();
        guiRenderer = new GUIRenderer(guiShader);

        masterRenderer = new MasterRenderer<>(objRenderer, terrainRenderer, guiRenderer);

        //Meshes
        ObjectMesh.construct("resources/models/Grass_Block.obj").constructMesh();
        ObjectMesh.construct("resources/models/Dirt_Block.obj").constructMesh();
        ObjectMesh.construct("resources/models/Stone_Block.obj").constructMesh();
        ObjectMesh.construct("resources/models/light.obj").constructMesh();
        //Meshes

        //GameObjects
        object = new GameObject(0, new Vector3f(0, 0.5f, -5), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
        light = new Light(new Vector3f(0, 1, 1), new Vector3f(1, 1, 1));
        lightCube = new GameObject(3, light.pos, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

        //terrains.add(new Terrain(0, 0, "/textures/grass_top.png"));
        //terrains.add(new Terrain(1, 0, "/textures/grass_top.png"));
        //GameObjects
    }

    //Loading game content
    private void preInit() {
        System.out.println("- Pre Init");
        blockManager = BlockManager.getInstance();

        blockManager.register(new dirt(), registryID, "dirt");
        blockManager.register(new stone(), registryID, "stone");
        blockManager.register(new grass(), registryID, "grass");
        blockManager.register(new sand(), registryID, "sand");
        blockManager.register(new glass(), registryID, "glass");
        blockManager.register(new stairs(), registryID, "stairs");



        //Create world
        world = new Dimension("earth");
        //Create world

        //Guis
        //guis.add(new GUITexture(0, new Vector2f(0f, 0f), new Vector2f(0.02f, 0.02f), "/textures/gui/cursor.png"));
        //Guis

        //objectMasterList.add(object);
        objectMasterList.add(lightCube);

        for(GameObject object : objectMasterList) {
            masterRenderer.processObject(object);
        }
    }

    //Loading graphics related content
    private void init() {
        System.out.println("- Init");
    }

    //Finalise
    private void postInit() {
        System.out.println("- Post Init");

        camera = new Camera(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
        thirdPerson = false;
        speedModifier = 1;
        sprinting = false;
        lightLocked = false;
        lastTime = System.currentTimeMillis();
    }

    private void handleInput() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
        if (Input.isKeyDown(GLFW.GLFW_KEY_L)) window.mouseState(true);
        if (Input.isKeyDown(GLFW.GLFW_KEY_U)) window.mouseState(false);
        if (Input.isKeyDown(GLFW.GLFW_KEY_C)) {
            camera.setRot(new Vector3f(0, 0, 0));
            camera.setPos(new Vector3f(0, 1, 0));
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_F5)) thirdPerson = !thirdPerson;
        if (Input.isKeyDown(GLFW.GLFW_KEY_F6)) {
            lightLocked = !lightLocked;
            if (lightLocked) {
                objectMasterList.get(0).setScale(0, 0, 0);
            } else {
                objectMasterList.get(0).setScale(1, 1, 1);
                objectMasterList.get(0).setPos(light.pos.x, light.pos.y, light.pos.z);
            }
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_Q)) speedModifier = speedModifier * 1.02f;
        if (Input.isKeyDown(GLFW.GLFW_KEY_E)) speedModifier = speedModifier / 1.02f;
        sprinting = Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL);

        if (Input.isKeyDown(GLFW.GLFW_KEY_UP)) {
            light.pos.z -= 0.025f * speedModifier;
            objectMasterList.get(0).setPos(light.pos.x, light.pos.y, light.pos.z);
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            light.pos.z += 0.025f * speedModifier;
            objectMasterList.get(0).setPos(light.pos.x, light.pos.y, light.pos.z);
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            light.pos.x -= 0.025f * speedModifier;
            objectMasterList.get(0).setPos(light.pos.x, light.pos.y, light.pos.z);
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            light.pos.x += 0.025f * speedModifier;
            objectMasterList.get(0).setPos(light.pos.x, light.pos.y, light.pos.z);
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
            light.pos.y += 0.025f * speedModifier;
            objectMasterList.get(0).setPos(light.pos.x, light.pos.y, light.pos.z);
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL)) {
            light.pos.y -= 0.025f * speedModifier;
            objectMasterList.get(0).setPos(light.pos.x, light.pos.y, light.pos.z);
        }
        if (lightLocked) {
            light.pos.x = camera.pos.x;
            light.pos.y = camera.pos.y;
            light.pos.z = camera.pos.z;
        }
    }

    private void tick() {

    }

    private void update() {
        window.update();
        if(thirdPerson) {
            camera.updateVisual(object);
        } else {
            camera.updateVisual();
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
        terrainShader.destroy();
        guiShader.destroy();
        masterRenderer.destroy();
        System.out.println("Goodbye :)");
    }

    public static void main(String[] args) {
        new main().start();
    }
}