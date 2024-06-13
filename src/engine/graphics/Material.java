package engine.graphics;

import engine.maths.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Material {
    private String path;
    private int width, height;
    private int textureID;
    private float transparency, specExp, opticalDensity;
    private int textID;
    private Vector3f ambient, diffuse, specular, emissive;


    public Material(String path) {
        this.path = path;
        this.transparency = 1;
        this.specExp = 255;
        this.opticalDensity = 1;
        this.textID = -1;
        this.ambient = new Vector3f(1, 1, 1);
        this.diffuse = new Vector3f(1, 1, 1);
        this.specular = new Vector3f(1, 1, 1);
        this.emissive = new Vector3f(0, 0, 0);
    }
    public Material() {
        this.path = "";
        this.transparency = 1;
        this.specExp = 255;
        this.opticalDensity = 1;
        this.textID = -1;
        this.ambient = new Vector3f(1, 1, 1);
        this.diffuse = new Vector3f(1, 1, 1);
        this.specular = new Vector3f(1, 1, 1);
        this.emissive = new Vector3f(0, 0, 0);
    }

    public Material(String path, float transparency, float specExp, float opticalDensity, int textID, Vector3f ambient, Vector3f diffuse, Vector3f specular, Vector3f emissive) {
        this.path = path;
        this.transparency = transparency;
        this.specExp = specExp;
        this.opticalDensity = opticalDensity;
        this.textID = textID;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.emissive = emissive;
    }

    public void createTexture() {
        try {
            FileInputStream in = new FileInputStream("resources" + path);
            PNGDecoder decoder = new PNGDecoder(in);
            ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.RGBA);
            buf.flip();

            textureID = GL11.glGenTextures();
            width = decoder.getWidth();
            height = decoder.getHeight();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            in.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        GL13.glDeleteTextures(textureID);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int textureID() {
        return textureID;
    }

    public void textureID(int textrureID) {
        this.textureID = textrureID;
    }

    public String path() {
        return path;
    }

    public void path(String path) {
        this.path = path;
    }

    public float specExp() {
        return specExp;
    }

    public void specExp(float specExp) {
        this.specExp = specExp;
    }

    public float transparency() {
        return transparency;
    }

    public void transparency(float transparency) {
        this.transparency = transparency;
    }

    public Vector3f ambient() {
        return ambient;
    }

    public void ambient(float x, float y, float z) {
        this.ambient.x = x;
        this.ambient.y = y;
        this.ambient.z = z;
    }

    public Vector3f diffuse() {
        return diffuse;
    }

    public void diffuse(float x, float y, float z) {
        this.diffuse.x = x;
        this.diffuse.y = y;
        this.diffuse.z = z;
    }

    public Vector3f specular() {
        return specular;
    }

    public void specular(float x, float y, float z) {
        this.specular.x = x;
        this.specular.y = y;
        this.specular.z = z;
    }

    public float opticalDensity() {
        return opticalDensity;
    }

    public void opticalDensity(float opticalDensity) {
        this.opticalDensity = opticalDensity;
    }

    public Vector3f emissive() {
        return emissive;
    }

    public void emissive(float x, float y, float z) {
        this.emissive.x = x;
        this.emissive.y = y;
        this.emissive.z = z;
    }

    public int textID() {
        return textID;
    }

    public void textID(int textID) {
        this.textID = textID;
    }
}