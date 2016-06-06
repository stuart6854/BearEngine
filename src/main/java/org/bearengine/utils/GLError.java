package main.java.org.bearengine.utils;

import main.java.org.bearengine.debug.Debug;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;

/**
 * Created by Stuart on 21/05/2016.
 */
public class GLError {

    public static GLFWErrorCallback glfwErrorCallback = GLFWErrorCallback.createPrint().set();

    public static boolean Check(String referrer){
        int error = glGetError();
        if(error != GL_NO_ERROR) {
            Debug.error(referrer + " -> OpenGL Error(" + error + "): " + DecodeErrorCode(error));
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

    public static String DecodeErrorCode(int code){
        String error = "";

        switch(code){
            case GL_INVALID_ENUM:
                error = "ILLEGAL ENUM PARAMETER!";
                break;
            case GL_INVALID_VALUE:
                error = "ILLEGAL VALUE PARAMETER!";
                break;
            case GL_INVALID_OPERATION:
                error = "INVALID OPERATION!";
                break;
            case GL_STACK_OVERFLOW:
                error = "STACK OVERFLOW!";
                break;
            case GL_STACK_UNDERFLOW:
                error = "STACK UNDERFLOW!";
                break;
            case GL_OUT_OF_MEMORY:
                error = "OUT OF MEMORY!";
                break;
            default:
                error = "UNKNOWN ERROR CODE!";
                break;
        }

        return error;
    }

}
