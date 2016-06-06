package main.java.org.bearengine.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Mouse {

	private static byte[] MouseState = new byte[8];
	
	private static float x, y;
	private static float prevx, prevy;
	private static float dx, dy;

	private static boolean cursorOverWindow;
	
	private static GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
		
		@Override
		public void invoke(long window, int button, int action, int mods) {
			MouseState[button] = (byte)action;
		}
		
	};
	
	private static GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
		
		@Override
		public void invoke(long window, double x, double y) {
            prevx = Mouse.x;
            prevy = Mouse.y;

			Mouse.x = (float)x;
			Mouse.y = (float)y;

            dx = Mouse.x - Mouse.prevx;
            dy = Mouse.y - Mouse.prevy;
		}
		
	};

	private static GLFWCursorEnterCallback cursorEnterCallback = new GLFWCursorEnterCallback() {

		@Override
		public void invoke(long window, boolean entered) {
			cursorOverWindow = entered;
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

    public static double getDX(){
        float dx = Mouse.dx;
        Mouse.dx = 0;
        return dx;
    }

    public static double getDY(){
        float dy = Mouse.dy;
        Mouse.dy = 0;
        return dy;
    }

	public static boolean isCursorOverWindow() {
        return cursorOverWindow;
    }
	
	public void setCallbacks(long windowID){
		glfwSetMouseButtonCallback(windowID, mouseButtonCallback);
		glfwSetCursorPosCallback(windowID, cursorPosCallback);
		glfwSetCursorEnterCallback(windowID, cursorEnterCallback);
	}

    public static void Cleanup(){

    }

}
