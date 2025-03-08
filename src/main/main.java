package main;

import engine.IO.Input;
import engine.IO.Window;
import engine.content.BlockManager;
import engine.content.blocks.*;
import engine.graphics.*;
import engine.graphics.renderers.ChunkRenderer;
import engine.graphics.renderers.GUIRenderer;
import engine.graphics.renderers.TerrainRenderer;
import engine.objects.Camera;
import engine.objects.GUITexture;
import engine.objects.GameObject;
import engine.objects.Light;
import engine.utils.FileCallback;
import engine.utils.FileUtils;
import engine.world.ChunkManager;
import engine.world.DimensionManager;
import engine.world.GeneratorManager;
import engine.world.dimension.Dimension;
import engine.world.generators.FlatGenerator;
import engine.world.generators.NoiseGenerator;
import engine.world.terrain.Chunk;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static engine.maths.Vector3.Vector3f;

public class main implements Runnable {
    //Core values
    public Thread game;
    public Window window;
    public Shader blockShader;
    public Shader terrainShader;
    public Shader guiShader;
    public ChunkRenderer chunkRenderer;
    public TerrainRenderer terrainRenderer;
    public GUIRenderer guiRenderer;
    public MasterRenderer<ChunkRenderer, TerrainRenderer, GUIRenderer> masterRenderer;
    public static List<GameObject> objectMasterList = new ArrayList<>();
    public static List<Mesh> meshes = new ArrayList<>();
    public static List<GUITexture> guis = new ArrayList<>();
    public static String OS = System.getProperty("os.name").toLowerCase();

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
    public static final String registryID = "devEngine";        //Allow for easier modding
    public static ResourceManager resourceManager;
    public static BlockManager blockManager;
    //public static ArrayList<ItemBase> contentItems = new ArrayList<>();
    public static DimensionManager dimensionManager;
    public static ChunkManager chunkManager;
    public static GeneratorManager generatorManager;
    public static String curDimension;

    //Simulation
    public static int tickRate = 20;        //Per second
    public static int frameRate = 60;       //Per second
    public static double deltaTime = 0;
    public static int renderDistance = 8;   //Default to 8
    public static Vector3f lastChunkLoad;

    //Util
    public static final StringBuilder stringer = new StringBuilder();    //Please use the concat function. If you use this directly make sure to run .setLength(0); on the thing after you finish
    public static double TIME_S = 0D;

    public static String concat(Object... args) {
        for(Object arg : args) {
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
        /*try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("Started loading sequence");

        //Setup engine
        instantiate();
        System.gc();

        //Load resources
        preInit();
        System.gc();

        //Load content
        init();
        System.gc();

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

            //Triggers each second
            if (System.currentTimeMillis() - timer > 1000) {
                GLFW.glfwSetWindowTitle(window.getWindow(), window.getTitle() + " | fps: " + frames + " | tps: " + ticks);
                timer += 1000;
                frames = 0;
                ticks = 0;
            }

            GLFW.glfwPollEvents();
            window.swapBuffers();
        }
        close(false);
    }

    //Setup core game engine
    private void instantiate() {
        System.out.println("- Instantiate");
        window = new Window(WIDTH, HEIGHT, "Dev engine");
        window.setBackgroundColor(0.474509804f, 0.650980392f, 1f);  //Imitate minecraft's sky color
        window.create();
        window.setIcon("resources/icon.png");

        blockShader = new Shader("/shaders/chunkVert.glsl", "/shaders/chunkFrag.glsl");
        blockShader.create();
        chunkRenderer = new ChunkRenderer(blockShader);

        terrainShader = new Shader("/shaders/terrainVert.glsl", "/shaders/terrainFrag.glsl");
        terrainShader.create();
        terrainRenderer = new TerrainRenderer(terrainShader);

        guiShader = new Shader("/shaders/guiVert.glsl", "/shaders/guiFrag.glsl");
        guiShader.create();
        guiRenderer = new GUIRenderer(guiShader);

        masterRenderer = new MasterRenderer<>(chunkRenderer, terrainRenderer, guiRenderer);
    }

    //Loading game resources and graphics
    private void preInit() {
        System.out.println("- Pre Init");
        resourceManager = ResourceManager.getInstance();

        //Read all files textures directory and register all textures
        String ogPath;
        if(OS.contains("win")) {
            ogPath = "resources\\textures\\";
        } else {
            ogPath = "resources/textures/";
        }

        FileUtils.recursiveLoopAlphabetic(new FileCallback() {
            @Override
            public void call(File file) {
                String registry = file.getPath().replace(ogPath, "").replace(".png", "");
                resourceManager.register(file.getPath(), registryID, registry);
            }
        }, "resources/textures/");
        resourceManager.registerAtlas();
    }

    //Loading game content
    private void init() {
        System.out.println("- Init");
        blockManager = BlockManager.getInstance();

        blockManager.register(new stone(), registryID, "stone");
        blockManager.register(new dirt(), registryID, "dirt");
        blockManager.register(new grass(), registryID, "grass");
        blockManager.register(new sand(), registryID, "sand");
        blockManager.register(new glass(), registryID, "glass");
        blockManager.register(new stairs(), registryID, "stairs");
        blockManager.register(new oakLog(), registryID, "oak_log");

        blockManager.registerBlockModels();

        //Create world generators
        generatorManager = GeneratorManager.getInstance();
        generatorManager.register(new FlatGenerator(), registryID, "flat");
        generatorManager.register(new NoiseGenerator(0), registryID, "noise");
        //Create world generators

        //Create world
        dimensionManager = DimensionManager.getInstance();
        dimensionManager.registerDimension(new Dimension("earth", generatorManager.getGenerator("devEngine:noise")), registryID);

        //Create world

        //Load into world
        curDimension = concat(registryID, ":earth");
        //Load into world

        //Guis
        //guis.add(new GUITexture(0, new Vector2f(0f, 0f), new Vector2f(0.02f, 0.02f), "/textures/gui/cursor.png"));
        //Guis
    }

    //Finalise
    private void postInit() {
        System.out.println("- Post Init");

        lastChunkLoad = new Vector3f(0, 17, 0);
        camera = new Camera(new Vector3f(0, 17, 0), new Vector3f(0, 1, 0));

        chunkManager = ChunkManager.getInstance();
        chunkManager.setup(curDimension, renderDistance);

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
        if (Input.isKeyDown(GLFW.GLFW_KEY_Q)) speedModifier += (float) (2.5f * deltaTime);
        if (Input.isKeyDown(GLFW.GLFW_KEY_E)) speedModifier -= (float) (2.5f * deltaTime);
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

        /*if(Input.isKeyDown(GLFW.GLFW_KEY_R)) {
            for(Chunk chunk : dimensionManager.getDimension(curDimension).chunks) {
                TIME_S += 0.1D * deltaTime;
                chunk.generate().remesh();
            }
        }*/

        if (lightLocked) {
            light.pos.x = camera.pos.x;
            light.pos.y = camera.pos.y;
            light.pos.z = camera.pos.z;
        }
    }

    private void tick() {
        int shiftX = (int) (camera.pos.x - lastChunkLoad.x) / 16;
        int shiftY = (int) (camera.pos.y - lastChunkLoad.y) / 16;
        int shiftZ = (int) (camera.pos.z - lastChunkLoad.z) / 16;

        if(Math.abs(shiftX) >= 1 || Math.abs(shiftY) >= 1 || Math.abs(shiftZ) >= 1) {
            chunkManager.loadChunks(shiftX, shiftY, shiftZ);
            lastChunkLoad.redefine(camera.pos);
        }
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

    private void close(boolean error) {
        window.destroy();
        for(Mesh mesh : meshes) {
            mesh.destroy();
        }
        blockShader.destroy();
        terrainShader.destroy();
        guiShader.destroy();
        masterRenderer.destroy();

        if(!error) {
            System.out.println("Goodbye :)");
            System.exit(0);
        } else {
            System.out.println("Oh no there was an error :(");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new main().start();
    }
}