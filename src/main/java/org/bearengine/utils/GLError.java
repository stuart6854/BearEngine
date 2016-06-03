package main.java.org.bearengine.utils;

import main.java.org.bearengine.debug.Debug;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;

/**
 * Created by Stuart on 21/05/2016.
 */
public class GLError {

    public static GLFWErrorCallback glfwErrorCallback = GLFWErrorCallback.createPrint().set();

    public static boolean Check(String referrer){
        int error = glGetError();
        if(error != GL_NO_ERROR) {
            Debug.error(referrer + " -> OpenGL Error: " + error);
            return  true;
        }
        return false;
    }

    public static void PrintShaderInfoLog(int shaderID){
        IntBuffer intvalue = BufferUtils.createIntBuffer(1);

        GL20.glGetShaderiv(shaderID, GL_INFO_LOG_LENGTH, intvalue);

        int length = intvalue.get();
        if(length <= 1) return; //No Log

        ByteBuffer log = BufferUtils.createByteBuffer(length);

        intvalue.flip();

        GL20.glGetShaderInfoLog(shaderID, intvalue, log);

        int actualLength = intvalue.get();

        byte[] logBytes = new byte[actualLength];
        log.get(logBytes);
        Debug.error(new String(logBytes));
    }

}
