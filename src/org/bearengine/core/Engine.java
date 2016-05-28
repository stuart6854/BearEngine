package org.bearengine.core;

import org.bearengine.debug.Debug;
import org.bearengine.graphics.Display;
import org.bearengine.graphics.types.Image;
import org.bearengine.graphics.types.Texture;
import org.bearengine.input.Keyboard;
import org.bearengine.input.Mouse;
import org.bearengine.objects.Camera;
import org.bearengine.screens.SplashScreen;
import org.bearengine.tests.GameTest;
import org.bearengine.utils.Time;
import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Engine implements Runnable{

    //TODO: FPS isn't being capped!

	private final Thread ENGINE_THREAD;
	
	private static int TARGET_UPS = 25;
	private static int TARGET_FPS = 30;
	
	private final Game gameScreen;
	private List<Game> screenOrder = new ArrayList<>();
	private int screenIndex = 0;
	
	public Engine(Game gameScreen){
		this(TARGET_UPS, TARGET_FPS, gameScreen);
	}
	
	public Engine(int target_ups, int target_fps, Game gameScreen) {
		Engine.TARGET_UPS = target_ups;
		Engine.TARGET_FPS = target_fps;
		this.gameScreen = gameScreen;
		this.ENGINE_THREAD = new Thread(this, "BearEngine-Main-Thread");
	}

	public void start(){
		ENGINE_THREAD.start();
	}

	@Override
	public void run() {
		try{
			init();
			EngineLoop();
		}catch(Exception e){
			Debug.error("BearEngine has crashed: ");
			e.printStackTrace();
		}finally {
			Exit();
		}
	}
	
	private void init(){
        //Configuration.DEBUG.set(true);

		GLFW.glfwInit();

		Display.mainDisplay.createDisplay();
        Display.mainDisplay.setWidthHeight(1280, 720);
		Display.mainDisplay.setVSYNC(0);
		Display.mainDisplay.centreOnScreen();
		Display.mainDisplay.setCurrent();

		printVersions();

		setupScreensOrder();

        Camera.Main_Camera = new Camera(new Matrix4f().ortho(-1, 1, -1, 1, 1, -1));
	}
	
	private void printVersions(){
		Debug.log("LWJGL Version: " + Version.getVersion());
		Debug.log("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		Debug.log("GLFW Version: " + GLFW.glfwGetVersionString());
		Debug.log("");
	}
	
	private void setupScreensOrder(){
        Texture texture = new Texture().UploadTexture(Image.loadImage("resources/textures/bearengine_logo.png"));
		screenOrder.add(new SplashScreen(.1f, texture));
		
		screenOrder.add(gameScreen);
	}
	
	private void EngineLoop(){
		float lag = 0.0f;
		float interval = 1.0f / TARGET_UPS;
		
		int fpsCounter = 0, upsCounter = 0;
	    long lastSec = System.currentTimeMillis();
	    
		while(!Display.mainDisplay.shouldClose()){
			double elapsedTime = Time.getElapsedTime();
			lag += elapsedTime;
			
			while(lag >= interval){
				update(interval);
				
				upsCounter++;
				lag -= interval;
			}
			render();

            Display.mainDisplay.update();

			sync();
			
			fpsCounter++;
			if(System.currentTimeMillis() - lastSec > 1000) {
				lastSec += 1000;
				Time.FPS = fpsCounter;
				Time.UPS = upsCounter;
				fpsCounter = 0;
				upsCounter = 0;
				Display.mainDisplay.setTitle("BearEngine -  UPS: " + Time.UPS + " || FPS: " + Time.FPS);
		   }
			
		}
		cleanup();
	}
	
	private void update(float deltaTime){		
		Game screen = screenOrder.get(screenIndex);
		if(screen.isExitRequested){
			screen.cleanup();
			screenIndex++;
			if(screenIndex >= screenOrder.size()){
				Exit();
			}
			Debug.log("Next Screen!");
		}else if(!screen.isInitialised){
			screen.init();
            screen.isInitialised = true;
			Debug.log("Screen Initialised!");
		}else{
			screen.update(deltaTime);
		}
	}
	
	private void render(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		Game screen = screenOrder.get(screenIndex);
        if(!screen.isInitialised) return;

		screen.render();
	}
	
	private void sync(){
		float loopSlot = 1f / TARGET_FPS;
		float endTime = (float) (Time.getLastLoopTime() + loopSlot);
		while(Time.getTime() < endTime){
			try{
				Thread.sleep(1);
			}catch (InterruptedException e){}
		}
	}
	
	private void cleanup(){
        Mouse.Cleanup();

        for(Game screen : screenOrder) {//Clean all Screens just in-case
            screen.cleanup();
        }

		for (Display display : Display.displays.values()) {
			display.destroy();
		}
	}
	
	private void Exit(){
		cleanup();
		System.exit(0);
	}
	
	public static void main(String... args){
		Game game = new GameTest();
		Engine engine = new Engine(30, 60, game);
		engine.start();
	}

}
