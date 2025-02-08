package engine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static main.main.stringer;
import static main.main.concat;

public class FileUtils {

    public static String loadAsString(String path) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(path)))) {
            String line = "";
            while((line = reader.readLine()) != null) {
                stringer.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to load file at: " + path + " | Error: ");
            e.printStackTrace();
        }

        String val = stringer.toString();
        stringer.setLength(0);
        return val;
    }

    /**
     * Recursively loops over all of the files of a given directory and files of directories within said directory.
     * Calls a provided callback function for each file that is looped over (Override the call method in {@link engine.utils.FileCallback#call(File)})
     * @param callback Callback function that will be called for every file with said file being provided as an argument
     * @param path The directory to start looping from
     */
    public static void recursiveLoop(FileCallback callback, String path) {
        File[] resources = new File(path).listFiles();
        assert resources != null;
        for (File file : resources) {
            if (file.isDirectory()) {
                if (!path.endsWith("/")) path = concat(path, "/");
                recursiveLoop(callback, concat(path, file.getName(), "/"));
            } else {
                callback.call(file);
            }
        }
    }
}