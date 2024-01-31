package engine.graphics;

import engine.maths.*;
import engine.utils.FileUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shader {
    private String vertFile, fragFile;
    private int vertID, fragID, programID;

    public Shader(String vertPath, String fragPath) {
        vertFile = FileUtils.loadAsString(vertPath);
        fragFile = FileUtils.loadAsString(fragPath);
    }

    public void create() {
        programID = GL20.glCreateProgram();

        //Vertex shader
        vertID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertID, vertFile);
        GL20.glCompileShader(vertID);
        //Error handling
        if(GL20.glGetShaderi(vertID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex shader error:\n" + GL20.glGetShaderInfoLog(vertID));
            return;
        }
        //Vertex shader

        //Fragment shader
        fragID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragID, fragFile);
        GL20.glCompileShader(fragID);
        //Error handling
        if(GL20.glGetShaderi(fragID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment shader error:\n" + GL20.glGetShaderInfoLog(fragID));
            return;
        }
        //Fragment shader

        //Program
        GL20.glAttachShader(programID, vertID);
        GL20.glAttachShader(programID, fragID);
        GL20.glLinkProgram(programID);
        //Error handling
        if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program linking error: " + GL20.glGetProgramInfoLog(programID));
            return;
        }

        GL20.glValidateProgram(programID);
        //Error handling
        if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program validate error: " + GL20.glGetProgramInfoLog(programID));
            return;
        }
        //Program
    }

    public int getUniformLoc(String name) {
        return GL20.glGetUniformLocation(programID, name);
    }



    //Setting uniform values
    public void setUniform(String name, float val) {
        GL20.glUniform1f(getUniformLoc(name), val);
    }

    public void setUniform(String name, int val) {
        GL20.glUniform1i(getUniformLoc(name), val);
    }

    public void setUniform(String name, boolean val) {
        GL20.glUniform1i(getUniformLoc(name), val ? 1 : 0);
    }

    public void setUniform(String name, Vector2f val) {
        GL20.glUniform2f(getUniformLoc(name), val.x, val.y);
    }

    public void setUniform(String name, Vector3f val) {
        GL20.glUniform3f(getUniformLoc(name), val.x, val.y, val.z);
    }

    public void setUniform(String name, Vector3i val) {
        GL20.glUniform3i(getUniformLoc(name), val.x, val.y, val.z);
    }

    public void setUniform(String name, Vector4f val) {
        GL20.glUniform4f(getUniformLoc(name), val.x, val.y, val.z, val.w);
    }

    /*
    Definitions of material in float[] form
    0 = texture ID
    1 = transparency
    2 = specExp
    3 = opticalDensity
    4, 5, 6 = ambient
    7, 8, 9 = diffuse
    10, 11, 12 = specular
    13, 14, 15 = emmisive
    */
    public void setUniform(String name, float[] val) {
        GL20.glUniform1fv(getUniformLoc(name), val);
    }

    public void setUniform(String name, Matrix4f val) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(16).put(val.getAll());
        buffer.flip();
        GL20.glUniformMatrix4fv(getUniformLoc(name), true, buffer);
    }
    //Setting uniform values

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void destroy() {
        GL20.glDetachShader(programID, vertID);
        GL20.glDetachShader(programID, fragID);
        GL20.glDeleteShader(vertID);
        GL20.glDeleteShader(fragID);
        GL20.glDeleteProgram(programID);
    }
}