package main.java.org.bearengine.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Keyboard extends GLFWKeyCallback{
	
	private static byte[] KeyStates = new byte[65536];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		KeyStates[key] = (byte)action;
	}

	public static boolean isKeyPressed(int key){
		byte state = KeyStates[key];
		return state == GLFW_PRESS;
	}
	
	public static boolean isKeyHeld(int key){
		byte state = KeyStates[key];
		return state == GLFW_REPEAT;
	}
	
	public static boolean isKeyDown(int key){
		byte state = KeyStates[key];
		return state == GLFW_PRESS || state == GLFW_REPEAT;
	}

}
