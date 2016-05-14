package org.bearengine.core;

import java.util.ArrayList;
import java.util.List;

import org.bearengine.graphics.Display;
import org.bearengine.utils.Time;
import org.lwjgl.glfw.GLFW;

public class Engine implements Runnable{

	private Thread mainThread;
	
	private static int TARGET_UPS = 25;
	private static int TARGET_FPS = 30;
	
	private List<Game> screenOrder = new ArrayList<>();
	private int screenIndex = 0;
	
	public Engine(){
		this(TARGET_UPS, TARGET_FPS);
	}
	
	public Engine(int target_ups, int target_fps) {
		Engine.TARGET_UPS = target_ups;
		Engine.TARGET_FPS = target_fps;
		this.mainThread = new Thread(this, "BearEngine-Main-Thread");
	}

	public void start(){
		mainThread.start();
	}
	
	@Override
	public void run() {
		try{
			init();
			EngineLoop();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			cleanup();
		}
	}
	
	private void init(){
		GLFW.glfwInit();
		
		Display.mainDisplay.createDisplay();
		Display.mainDisplay.setVSYNC(0);
		Display.mainDisplay.centreOnScreen();
		Display.mainDisplay.setCurrent();
		
		screenOrder.add(new SplashScreen(5));
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
				Display.mainDisplay.setTitle("BearEngine - FPS: " + Time.FPS
												+ " UPS: " + Time.UPS);
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
		Engine engine = new Engine(30, 120);
		engine.start();
	}

}
