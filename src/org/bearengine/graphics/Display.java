package org.bearengine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;

public class Display {
	
	public static Map<Long, Display> displays = new HashMap<>();
	
	public static final Display mainDisplay = new Display();
	
	public enum WindowMode{ 
		Windowed(0), 
		Fullscreen(1), 
		Borderless(2);
		
		private int mode;
		
		WindowMode(int mode){
			this.mode = mode;
		}
	}
	
	private long windowID;
	
	private String title = "BearEngine";
	private int width = 640, height = 480;
	
	private WindowMode windowMode = WindowMode.Windowed;
	
	private boolean destroyed = false;
	private boolean visible = false;
	
	private GLFWWindowSizeCallback windowSizeCallback = new GLFWWindowSizeCallback() {
		
		@Override
		public void invoke(long window, int width, int height) {
			Display display = Display.displays.get(window);
			
			display.setWidthHeight(width, height);
		}
	};
	
	private GLFWFramebufferSizeCallback framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
		
		@Override
		public void invoke(long window, int width, int height) {
			GL11.glViewport(0, 0, width, height);
		}
	};
	
	public void createDisplay(){
		if(windowID != NULL) {
			System.err.println("This Display is Already Created!");
			return;
		}
		
		setHints();
		
		windowID = glfwCreateWindow(width, height, title, NULL, NULL);
		
		if(windowID == NULL){
			throw new RuntimeException("Failed to Create the GLFW Window!");
		}
		
		this.destroyed = false;
		
		setCallbacks();
		
		setCurrent();
		centreOnScreen();
		setVisible(true);
		
		Display.displays.put(windowID, this);
		
		System.out.println(width + "x" + height + "(Mode: " + windowMode.name() + ") Display Created!");
	}
	
	public void update(){
		if(windowID == NULL) return;
		glfwPollEvents();
		glfwSwapBuffers(windowID);
	}
	
	public void destroy(){
		destroyed = true;
		Display.displays.remove(this.windowID);
		glfwDestroyWindow(windowID);
		this.windowID = NULL;
	}
	
	private void setHints(){
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
	}
	
	private void setCallbacks(){
		glfwSetWindowSizeCallback(this.windowID, this.windowSizeCallback);
		glfwSetFramebufferSizeCallback(this.windowID, this.framebufferSizeCallback);
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
		if(this.visible)glfwShowWindow(this.windowID);
		else glfwHideWindow(this.windowID);
	}
	
	public void setWidth(int width){
		setWidthHeight(width, this.height);
	}
	
	public void setHeight(int height){
		setWidthHeight(this.width, height);
	}
	
	public void setWidthHeight(int width, int height){
		this.width = width;
		this.height = height;
		updateWindowSize();
	}
	
	private void updateWindowSize(){
		glfwSetWindowSize(windowID, width, height);
	}
	
	public void setTitle(String title){
		this.title = title;
		glfwSetWindowTitle(this.windowID, title);
	}
	
	public void setWindowMode(WindowMode windowMode){
		this.windowMode = windowMode;
		
		long tmpWindowID = -1;
		
		if(windowMode == WindowMode.Windowed){
			tmpWindowID = glfwCreateWindow(width, height, title, NULL, windowID);
		}else if(windowMode == WindowMode.Fullscreen){
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			tmpWindowID = glfwCreateWindow(vidMode.width(), vidMode.height(), title, glfwGetPrimaryMonitor(), windowID);
		}else if (windowMode == WindowMode.Borderless) {
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			tmpWindowID = glfwCreateWindow(vidMode.width(), vidMode.height(), title, glfwGetPrimaryMonitor(), windowID);
		}
		this.destroy();
		this.windowID = tmpWindowID;
		this.setCurrent();
	}
	
	public void setCurrent(){
		glfwMakeContextCurrent(this.windowID);
	}
	
	public void setVSYNC(int vsync){
		glfwSwapInterval(vsync);
	}
	
	public void centreOnScreen(){
		setVisible(false);
		
		// Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
        		windowID,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

		setVisible(true);
	}
	
	public boolean shouldClose(){
		return glfwWindowShouldClose(this.windowID) == 0 ? false : true;
	}
	
	///Getters///
	
	public long getWindowID() {
		return windowID;
	}

	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public WindowMode getWindowMode() {
		return windowMode;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isDestroyed(){
		return destroyed;
	}
		
}
