package org.bearengine.core;

import java.util.ArrayList;
import java.util.List;

import org.bearengine.graphics.Display;
import org.bearengine.input.Keyboard;
import org.bearengine.input.Mouse;
import org.bearengine.screens.BearEngineSplashScreen;
import org.bearengine.tests.GameTest;
import org.bearengine.utils.Time;

import org.lwjgl.glfw.GLFW;

public class Engine implements Runnable{

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
			System.err.println("BearEngine has crashed: ");
			e.printStackTrace();
		}finally {
			Exit();
		}
	}
	
	private void init(){
		GLFW.glfwInit();
		
		setupScreensOrder();
		
		Display.mainDisplay.createDisplay();
		Display.mainDisplay.setVSYNC(0);
		Display.mainDisplay.centreOnScreen();
		Display.mainDisplay.setCurrent();
	}
	
	private void setupScreensOrder(){
		screenOrder.add(new BearEngineSplashScreen());
		
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
				Display.mainDisplay.setTitle("BearEngine - FPS: " + Time.FPS + " UPS: " + Time.UPS);
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
			System.out.println("Next Screen!");
		}else if(!screen.isInitialised){
			screen.isInitialised = true;
			screen.init();
			System.out.println("Screen Initialised!");
		}else{
			screen.update(deltaTime);
		}
	}
	
	private void render(){
		Game screen = screenOrder.get(screenIndex);
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
		Engine engine = new Engine(30, 120, game);
		engine.start();
	}

}
