package main;

import engine.IO.Window;
import utils.Stringer;

public class DevEngine implements Runnable {
    //Core values
    public Thread mainThread;
    public static String OS = System.getProperty("os.name").toLowerCase();
    public static int numCores = Runtime.getRuntime().availableProcessors();

    //Rendering values
    public final int WIDTH = 1280, HEIGHT = 760;
    public static float fov = 100.0f;
    public static float near = 0.0025f;
    public static float far = 50000.0f;
    public static float speed = 5.0f, sensitivity = 10.0f;

    //Main thread specific variables
    public static Stringer stringer = new Stringer();
    public static Window window = Window.getInstance();

    public void start() {
        mainThread = new Thread(this, "main");
        mainThread.start();
    }

    public void run() {
        //CoreSetup
        //Initial setup of all the core functionality like the render systems and basic loading screen
        long startTime = System.currentTimeMillis();
        System.out.println("Starting game loading with CoreSetup");

        //Setting up game window
        new Window(WIDTH, HEIGHT, "DevEngine");


        //PreInit
        //Loading up all resources, like textures, shaders and registries and language translations
        System.out.println("Next phase to: PreInit");



        //Init
        //Loading up all game content like blocks and items and fluids and gasses, etc...
        System.out.println("Next phase to: Init");



        //PostInit
        //Loading up all mechanics, world gen, keybinds, etc...
        System.out.println("Next phase to: PostInit");



        //Finalise
        //Doing everything else and then ending on the title screen/in a world
        System.out.println("Next phase to: Finalise");



        //And we start to run the game here
        System.out.println(stringer.concat("Game loaded in ", (System.currentTimeMillis() - startTime) / 1000, " seconds"));
    }

    //The pretty sad entry point, main method with a single line
    public static void main(String[] args) {
        new DevEngine().start();
    }
}
