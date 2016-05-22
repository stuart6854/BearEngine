package org.bearengine.screens;

import static org.lwjgl.opengl.GL11.*;

import org.bearengine.core.Game;
import org.bearengine.debug.Debug;
import org.bearengine.graphics.types.Texture;
import org.bearengine.utils.Time;

public class SplashScreen extends Game {
	
	private double startTime;
	private double duration;
	private double endTime;

    private Texture SplashTexture;
	
	public SplashScreen(float duration, Texture texture) {
		super();
		this.duration = duration;
        this.SplashTexture = texture;
	}
	
	@Override
	public void init() {
		this.isInitialised = true;
		
		this.startTime = Time.getTime();
		this.endTime = this.startTime + this.duration;
	}

	@Override
	public void update(float deltaTime) {
		if(Time.getTime() >= this.endTime){
			this.isExitRequested = true;
			Debug.log("SplashScreen complete!");
		}
	}

	@Override
	public void render() {
		SplashTexture.Bind();

        glBegin(GL_QUADS);
            glTexCoord2f(0, 1); glVertex2f(-.5f, .5f);
            glTexCoord2f(0, 0); glVertex2f(-.5f, -.5f);
            glTexCoord2f(1, 0); glVertex2f(.5f, -.5f);
            glTexCoord2f(1, 1); glVertex2f(.5f, .5f);
        glEnd();

	}

	@Override
	public void cleanup() {
		
	}

}
