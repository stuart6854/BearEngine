package main.java.org.bearengine.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import java.util.ArrayList;
import java.util.List;

public class Mouse {

    public static final int BUTTON_1 = 0;
    public static final int BUTTON_2 = 1;
    public static final int BUTTON_3 = 2;
    public static final int BUTTON_4 = 3;
    public static final int BUTTON_5 = 4;
    public static final int BUTTON_6 = 5;
    public static final int BUTTON_7 = 6;
    public static final int BUTTON_8 = 7;

    public static final int BUTTON_LEFT = BUTTON_1;
    public static final int BUTTON_RIGHT = BUTTON_2;
    public static final int BUTTON_MIDDLE = BUTTON_3;

    private static List<Integer> ButtonStates = new ArrayList<>();
    private static List<Integer> ButtonStates_ThisFrame = new ArrayList<>();
    private static List<Integer> ButtonStates_LastFrame = new ArrayList<>();
	
	private static float x, y;
	private static float prevx, prevy;
	private static float dx, dy;

	private static boolean cursorOverWindow;
	
	private static GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
		
		@Override
		public void invoke(long window, int button, int action, int mods) {
			SetButton(button, action != GLFW_RELEASE);
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

    public static boolean isButtonClicked(int button){
        return ButtonStates_ThisFrame.contains(button) && !ButtonStates_LastFrame.contains(button);
    }

	public static boolean isButtonPressed(int button){
		return ButtonStates_ThisFrame.contains(button);
	}

    public static boolean isButtonReleased(int button){
        return !isButtonPressed(button);
    }

	public static int getMouseX(){
		return (int)x;
	}
	
	public static int getMouseY(){
		return (int)y;
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

    private static void SetButton(int btn, boolean pressed){
        if(pressed && !ButtonStates.contains(btn))
            ButtonStates.add(btn);

        if(!pressed && ButtonStates.contains(btn))
            ButtonStates.remove((Integer)btn);
    }

    public static void BeginFrame(){
        ButtonStates_ThisFrame.clear();
        ButtonStates_ThisFrame.addAll(ButtonStates);
    }

    public static void EndFrame(){
        ButtonStates_LastFrame.clear();
        ButtonStates_LastFrame.addAll(ButtonStates_ThisFrame);
    }

    public static void Cleanup(){

    }

}
