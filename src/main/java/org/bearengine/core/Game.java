package main.java.org.bearengine.core;

public abstract class Game {
	
	public boolean isInitialised = false;
	public boolean isExitRequested = false;
	
	public abstract void init();
	
	public abstract void update(float deltaTime);
	
	public abstract void render();
	
	public abstract void cleanup();
	
}
