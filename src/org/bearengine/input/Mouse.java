package org.bearengine.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Mouse{

	private static byte[] MouseState = new byte[8];
	
	private static double x, y;
	
	private static boolean cursorOverWindow;
	
	private GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
		
		@Override
		public void invoke(long window, int button, int action, int mods) {
			MouseState[button] = (byte)action;
		}
		
	};
	
	private GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
		
		@Override
		public void invoke(long window, double xpos, double ypos) {
			x = xpos;
			y = ypos;
		}
		
	};
	
	private GLFWCursorEnterCallback cursorEnterCallback = new GLFWCursorEnterCallback() {
		
		@Override
		public void invoke(long window, int entered) {
			cursorOverWindow = (entered == 1);
		}
		
	};
	
	
	public static boolean isButtonPressed(int button){
		return MouseState[button] == GLFW_PRESS;
	}
	
	public static double getMouseX(){
		return x;
	}
	
	public static double getMouseY(){
		return y;
	}
	
	public static boolean isCursorOverWindow(){
		return cursorOverWindow;
	}
	
	
	public void setCallbacks(long windowID){
		glfwSetMouseButtonCallback(windowID, mouseButtonCallback);
		glfwSetCursorPosCallback(windowID, cursorPosCallback);
		glfwSetCursorEnterCallback(windowID, cursorEnterCallback);
	}
	
}
