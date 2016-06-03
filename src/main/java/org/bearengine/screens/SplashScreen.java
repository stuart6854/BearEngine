package main.java.org.bearengine.screens;

import static org.lwjgl.opengl.GL11.*;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.utils.Time;
import main.java.org.bearengine.core.Game;

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
        Debug.log("SplashScreen -> Init.");

		this.isInitialised = true;
		
		this.startTime = Time.getTime();
		this.endTime = this.startTime + this.duration;

        Debug.log("SplashScreen -> Init End.");
	}

	@Override
	public void update(float deltaTime) {
		if(Time.getTime() >= this.endTime){
			this.isExitRequested = true;
			Debug.log("SplashScreen -> Complete!");
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
