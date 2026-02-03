package main;

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

    public void start() {
        mainThread = new Thread(this, "main");
        mainThread.start();
    }

    public void run() {

    }

    //The pretty sad entry point, main method with a single line
    public static void main(String[] args) {
        new DevEngine().start();
    }
}
