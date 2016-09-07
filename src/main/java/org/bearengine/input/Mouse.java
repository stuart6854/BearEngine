package main.java.org.bearengine.input;

import static org.lwjgl.glfw.GLFW.*;

import main.java.org.bearengine.debug.Debug;
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
    
    private static final float ClickTime = 0.25f; //If mouse is down and then up in this time a click is registered else it is held down
    
    private static int[] ButtonStates = new int[8]; // 0 - normal, 1 - up, 2 - down, 3 - clicked, 4 - held
    private static float[] ButtonDownTimes = new float[8];
    
	private static float x, y;
	private static float prevx, prevy;
	private static float dx, dy;

	private static boolean cursorOverWindow;
    
    private static List<IMouseCallbacks> MouseListeners = new ArrayList<>();
	
	private static GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
		
		@Override
		public void invoke(long window, int button, int action, int mods) {//Only called on state change
            if(action == GLFW_PRESS){
                ButtonStates[button] = 2;
                for(IMouseCallbacks callbacks : MouseListeners)
                    callbacks.OnMouseDown();
            }else{
                ButtonStates[button] = 1;
                for(IMouseCallbacks callbacks : MouseListeners)
                    callbacks.OnMouseUp();
            }
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
        
//    public static boolean isButtonClicked(int button){
//        return ButtonStates[button] == 3;
//    }
    
    public static boolean isButtonHeld(int button){
        return ButtonStates[button] == 4;
    }
    
    public static boolean isButtonDown(int button){//Called once when mouse button goes down
        return ButtonStates[button] == 2;
    }
    
    public static boolean isButtonUp(int button){//Called once when mouse button comes back up
        return ButtonStates[button] == 1;
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

	public static void RegisterMouseListner(IMouseCallbacks listener){
	    if(!MouseListeners.contains(listener)){
	        MouseListeners.add(listener);
        }
    }
    
    public static void DeregisterMouseListner(IMouseCallbacks listener){
        if(MouseListeners.contains(listener)){
            MouseListeners.remove(listener);
        }
    }


    public static void BeginFrame(double delta){
        // 0 - Normal
        // 1 - Up
        // 2 - Down
        // 3 - Clicked
        // 4 - Held
        for(int i = 0; i < ButtonDownTimes.length; i++){
            int state = ButtonStates[i];
            
            switch(state){
                case 0:
                    ButtonDownTimes[i] = 0;
                    break;
                case 1:
                    
                    break;
                case 2:
                    ButtonDownTimes[i] += delta;
                    break;
                case 3:
                    ButtonDownTimes[i] = 0;
                    break;
                case 4:
                    ButtonDownTimes[i] += delta;
                    break;
                default:
                    break;
            }
            
            if(ButtonDownTimes[i] <= ClickTime && state == 1){//Click Detected
                ButtonStates[i] = 3;
                ButtonDownTimes[i] = 0;
                for(IMouseCallbacks callbacks : MouseListeners)
                    callbacks.OnMouseClick();
            }else if(ButtonDownTimes[i] > ClickTime && state == 2 || state == 4){//Held Detected
                ButtonStates[i] = 4;
                for(IMouseCallbacks callbacks : MouseListeners)
                    callbacks.OnMouseHeld();
            }
            
            if(state == 1){
                ButtonDownTimes[i] = 0;
                ButtonStates[i] = 0;
            }
        }
        
//        ButtonStates_ThisFrame.clear();
//        ButtonStates_ThisFrame.addAll(ButtonStates);
    }

    public static void EndFrame(){
        
    }

    public static void Cleanup(){

    }

}
