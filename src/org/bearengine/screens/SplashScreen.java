package org.bearengine.screens;

import org.bearengine.core.Game;
import org.bearengine.utils.Time;

public class SplashScreen extends Game {
	
	private double startTime;
	private double duration;
	private double endTime;
	
	public SplashScreen(float duration) {
		super();
		this.duration = duration;
	}
	
	@Override
	public void init() {
		this.isInitialised = true;
		
		this.startTime = Time.getTime();
		this.endTime = this.startTime + this.duration;
		System.out.println("Start: " + startTime);
		System.out.println("End: " + endTime);
	}

	@Override
	public void update(float deltaTime) {
		if(Time.getTime() >= this.endTime){
			this.isExitRequested = true;
			System.out.println("SplashScreen complete!");
		}
	}

	@Override
	public void render() {
		//Display BearEngine Logo
	}

	@Override
	public void cleanup() {
		
	}
	
	public boolean isExitRequested(){
		return this.isExitRequested;
	}

}
