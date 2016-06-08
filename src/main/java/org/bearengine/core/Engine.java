package main.java.org.bearengine.core;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.input.Keyboard;
import main.java.org.bearengine.input.Mouse;
import main.java.org.bearengine.objects.Camera;
import main.java.org.bearengine.objects.DevCamera;
import main.java.org.bearengine.tests.GameTest;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.screens.SplashScreen;
import main.java.org.bearengine.utils.Time;
import main.java.org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.Configuration;

import java.io.IOException;
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
        Debug.log("Engine -> Engine Starting.");
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
        Configuration.DEBUG.set(true);

        Debug.log("Engine -> Initialising Engine.");

		GLFW.glfwInit();

		Display.mainDisplay.createDisplay();
        Display.mainDisplay.setWidthHeight(1280, 720);
		Display.mainDisplay.setVSYNC(1);
		Display.mainDisplay.centreOnScreen();
		Display.mainDisplay.setCurrent();

		printVersions();

		setupScreensOrder();

        Camera.Main_Camera = new Camera(new Matrix4f().ortho(-1, 1, -1, 1, 1, -1));
        Debug.log("Engine -> Engine Initialised.");
    }
	
	private void printVersions(){
		Debug.log("LWJGL Version: " + Version.getVersion());
		Debug.log("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		Debug.log("GLFW Version: " + GLFW.glfwGetVersionString());
		Debug.log("");
	}
	
	private void setupScreensOrder(){
        Texture texture = new Texture().UploadTexture(Image.GetImage("textures/bearengine_logo.png", ResourceLoader.FileType.Internal));
		screenOrder.add(new SplashScreen(0.1f, texture));
		
		screenOrder.add(gameScreen);
	}

    private void EngineLoop(){
        Debug.log("Engine -> Starting Game-Loop.");

        long lastSec = System.currentTimeMillis();
        int millis, frames = 0;

        long lastTime = System.nanoTime();

        Time.init();

        while(!Display.mainDisplay.shouldClose()){
            double delta = Time.NanToSec(System.nanoTime() - lastTime);
            lastTime = System.nanoTime();
            Keyboard.BeginFrame();

            update((float)delta);
            render();
            frames++;

            Keyboard.EndFrame();

            if(System.currentTimeMillis() - lastSec > 1000) {
                millis = (int)(delta * 1000);
                Display.mainDisplay.setTitle("BearEngine -  MS: " + millis + " || FPS: " + frames);
                frames = 0;
                lastSec += 1000;
            }
        }

        Debug.log("Engine -> Exited Loop.");
        Exit();
    }

	private void update(float deltaTime){
		Game screen = screenOrder.get(screenIndex);
		if(screen.isExitRequested){
			screen.cleanup();
			screenIndex++;
			if(screenIndex >= screenOrder.size()){
				Exit();
			}
			Debug.log("Engine -> Next Screen!");
		}else if(!screen.isInitialised){
			screen.init();
            screen.isInitialised = true;
			Debug.log("Engine -> Screen Initialised!");
		}else{
            //Debug.log("Engine -> Screen Update!");
			screen.update(deltaTime);
            //Debug.log("Engine -> Screen Update END!");
		}

        DevCamera.ProcessInput(deltaTime);
	}
	
	private void render(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		Game screen = screenOrder.get(screenIndex);
        if(!screen.isInitialised) return;

		screen.render();

        Display.mainDisplay.update();
	}

	private void cleanup(){
        Debug.log("Engine -> Cleaning Up.");

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
        Debug.log("Engine -> Exiting.");
		System.exit(0);
	}
	
	public static void main(String... args)throws IOException{
        Game game = new GameTest();
		Engine engine = new Engine(120, 120, game);
		engine.start();

//        ClassLoaderDummy.searchResource("/main/java/resources/textures/bearengine_logo.png");
//        Image image = Image.GetImage("textures/bearengine_logo.png", ResourceLoader.FileType.Internal);
//        InputStream stream = Image.class.getResourceAsStream("/main/java/resources/textures/bearengine_logo.png");
//        Debug.log("Jar Texture: " + (image == null ? "NULL" : "FOUND"));
//        File file = new File("G:\\UserStorage\\Pictures\\photo.jpg");
//        BufferedImage image = ImageIO.read(file);
//        Debug.log("External Texture: " + (image == null ? "NULL" : "FOUND"));
	}

}
