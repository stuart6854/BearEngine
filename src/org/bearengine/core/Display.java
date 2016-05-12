package org.bearengine.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;

public class Display {
	
	public static Map<Long, Display> displays = new HashMap<>();
	
	public static final int Windowed = 0, Fullscreen = 1, Borderless = 2;
	
	private long windowID;
	
	private String title = "BearEngine";
	private int width = 640, height = 480;
	
	private int windowMode = Display.Windowed;
	
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
		setHints();
		
		windowID = glfwCreateWindow(width, height, title, NULL, NULL);
		
		if(windowID == NULL){
			throw new RuntimeException("Failed to Create the GLFW Window!");
		}
		
		setCallbacks();
		
		setCurrent();
		centreOnScreen();
		setVisible(true);
		
		Display.displays.put(windowID, this);
		
		System.out.println(width + "x" + height + "(Mode:" + windowMode + ") Display Created!");
	}
	
	public void update(){
		glfwPollEvents();
		glfwSwapBuffers(windowID);
	}
	
	public void destroy(){
		destroyed = true;
		Display.displays.remove(this.windowID);
		glfwDestroyWindow(windowID);
	}
	
	private void setHints(){
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
	}
	
	private void setCallbacks(){
		glfwSetWindowSizeCallback(this.windowID, this.windowSizeCallback);
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
	
	public void setWindowMode(int windowMode){
		this.windowMode = windowMode;
		
		long tmpWindowID = -1;
		
		if(windowMode == Display.Windowed){
			tmpWindowID = glfwCreateWindow(width, height, title, NULL, windowID);
		}else if(windowMode == Display.Fullscreen){
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			tmpWindowID = glfwCreateWindow(vidMode.width(), vidMode.height(), title, glfwGetPrimaryMonitor(), windowID);
		}else if (windowMode == Display.Borderless) {
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			tmpWindowID = glfwCreateWindow(vidMode.width(), vidMode.height(), title, glfwGetPrimaryMonitor(), windowID);
		}
		this.destroy();
		this.windowID = tmpWindowID;
		this.setCurrent();
	}
	
	private void setCurrent(){
		glfwMakeContextCurrent(this.windowID);
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

	public int getWindowMode() {
		return windowMode;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isDestroyed(){
		return destroyed;
	}
		
}
