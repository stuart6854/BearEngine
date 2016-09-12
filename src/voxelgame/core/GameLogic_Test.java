package voxelgame.core;

import main.java.org.bearengine.core.Game;
import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.importers.OBJImporter;
import main.java.org.bearengine.graphics.shaders.Shader;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Color;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.input.Keyboard;
import main.java.org.bearengine.input.Mouse;
import main.java.org.bearengine.objects.Camera;
import main.java.org.bearengine.graphics.*;
import main.java.org.bearengine.objects.GameObject;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.joml.Matrix4f;
import main.java.org.joml.Vector3f;

import org.lwjgl.opengl.GL11;
import voxelgame.data.LoadChunks;
import voxelgame.data.World;

public class GameLogic_Test extends Game {

//	private Skybox skybox;
//	private String[] skyboxTextureFiles = {
//			"/resources/skyboxes/TropicalSunnyDayRight2048.png",
//			"/resources/skyboxes/TropicalSunnyDayLeft2048.png",
//			"/resources/skyboxes/TropicalSunnyDayUp2048.png",
//			"/resources/skyboxes/TropicalSunnyDayDown2048.png",
//			"/resources/skyboxes/TropicalSunnyDayBack2048.png",
//			"/resources/skyboxes/TropicalSunnyDayFront2048.png"
//	};

    private final float CAMERA_POS_STEP = 10.0f;
	private static final float MOUSE_SENSITIVITY = 6.5f;

	private World world;

	private LoadChunks loadChunks;

    OBJImporter objImporter;
    
	GameObject lightSource, lightReceiver;

//	private GUICanvas guiCanvas;

//	Ray ray;
//	Plane plane;
//	Rect3D rect;
//	Line line;

	//private Billboard billboard;

	//Statistics Gui Objects
//	private GUILabel fpsLabel, upsLabel;
//	private GUILabel osLabel, gpuInfoLabel, memoryLabel;
//	private GUILabel openglShaderVersionLabel;

	//Debug GUI Elements
//	private GUILabel camPosLabel;
//	private GUILabel camRotLabel;

	@Override
	public void init(){
		Debug.log(Float.parseFloat("0.0"));

//        Display.mainDisplay.SetClearColor(Color.SKY_BLUE);
        Display.mainDisplay.SetClearColor(Color.BLACK);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_CULL_FACE);

        Camera.Main_Camera.SetProjection(new Matrix4f().perspective((float)Math.toRadians(60.0f), Display.mainDisplay.Aspect, 0.1f, 1000.0f));
		Camera.Main_Camera.SetPosition(-1.5, 152, 6.5);
		Camera.Main_Camera.SetRotation(15, 20, 0);

//		skybox = new Skybox(skyboxTextureFiles);
        
        Image textureSheetImage = ResourceLoader.Load("/voxelgame/resources/textures/texture.png", Image.class);
        Texture textureSheet = new Texture().UploadTexture(textureSheetImage);
		world = new World(textureSheet);

		loadChunks = new LoadChunks(world);
        
        objImporter = new OBJImporter();

		Mesh lightSourceMesh = objImporter.LoadMesh("/main/java/resources/models/cube.obj");
		lightSourceMesh.material.shaderProgram = ShaderProgram.DEFAULT_LIGHTING_SRC;
		lightSource = new GameObject();
        lightSource.SetMesh(lightSourceMesh);
        lightSource.SetPosition(-2f, 152, 0f);
		lightSource.SetRotation(0, .9f, 0);
        lightSource.SetScale(.5f);

		Mesh lightRecieverMesh = objImporter.LoadMesh("/main/java/resources/models/cube_normals.obj");
		lightRecieverMesh.material.shaderProgram = ShaderProgram.DEFAULT_LIGHTING;

        lightReceiver = new GameObject();
        lightReceiver.SetMesh(lightRecieverMesh);
        lightReceiver.SetPosition(4f, 150, -1f);
        lightReceiver.SetScale(1f);

        loadChunks.SetPosition(0, 0, 0);

        //GUI System Testing//
//        List<GUIElement> guiElements = new ArrayList<GUIElement>();
//        FontTexture fontTexture = new FontTexture(new Font("Arial", Font.PLAIN, 12));
//
//        //Statistic Gui Elements
//        fpsLabel = new GUILabel(5, 5, 0, "FPS: 0", fontTexture);
//        upsLabel = new GUILabel(5, 20, 0, "UPS: 0", fontTexture);
//
//        osLabel = new GUILabel(5, 5, 0, "OS: " + SystemInfo.osName + " (" + SystemInfo.osVersion + ") " + SystemInfo.osArchitecture, fontTexture);
//        osLabel.setAnchorPoint(AnchorPoint.TR);
//
//        gpuInfoLabel = new GUILabel(5, 20, 0, SystemInfo.gpuInfo, fontTexture);
//        gpuInfoLabel.setAnchorPoint(AnchorPoint.TR);
//
//        memoryLabel = new GUILabel(5, 45, 0, "Used Memory: 0", fontTexture);
//        memoryLabel.setAnchorPoint(AnchorPoint.TR);
//
//        openglShaderVersionLabel = new GUILabel(5, 75, 0, "OpenGL Shader Version: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION), fontTexture);
//        openglShaderVersionLabel.setAnchorPoint(AnchorPoint.TR);
//
//        camPosLabel = new GUILabel(5, 35, 0, "Pos: x, x, x", fontTexture);
//        camRotLabel = new GUILabel(5, 50, 0, "Rot: x, x", fontTexture);
//
//        guiElements.add(fpsLabel);
//        guiElements.add(upsLabel);
//        guiElements.add(osLabel);
//        guiElements.add(gpuInfoLabel);
//        guiElements.add(memoryLabel);
//        guiElements.add(openglShaderVersionLabel);
//        guiElements.add(camPosLabel);
//        guiElements.add(camRotLabel);
//
//        guiCanvas = new GUICanvas(guiElements);
//
//		ray = new Ray(new Vector3f(3, 149, -1), new Vector3f(-1, 0, 0));
//        plane = new Plane();
//        plane.a = new Vector3f(0, 150, 0);
//        plane.b = new Vector3f(0, 148, 0);
//        plane.c = new Vector3f(0, 148, -2);
//        plane.d = new Vector3f(0, 150, -2);
//
//        rect = new Rect3D();
//        rect.setTL(0, 150, 0);
//        rect.setBL(0, 148, 0);
//        rect.setBR(2, 148, 0);
//        rect.setTR(2, 150, 0);
//
//        line = new Line(new Vector3f(1, 149, 2), new Vector3f(1, 149, -2));

//		float[] vertices = new float[]{
//			-10, 0.1f, 0,
//			-10, -0.1f, 0,
//			0, -0.1f, 0,
//			0, 0.1f, 0
//		};
//
//		int[] indices = new int[]{
//			0, 1, 2, 2, 3, 0
//		};
//
//		billboard = new Billboard();
//		billboard.getMesh().setMaterial(new Material(new Color(1f, 0, 0), 0));
//		billboard.getMesh().setVertices(vertices);
//		billboard.getMesh().setIndices(indices);
//		billboard.getMesh().flushToBuffers();
//		billboard.setPosition(new Vector3f(-2, 154, 0));
//		billboard.getBoundingBox().updateBounds(billboard.getMesh());
	}

	@Override
	public void update(float delta) {
//		lightReceiver.Rotate(0, 10f * delta, 0);
//		mousePicker.update();

		//Update camera based on mouse
		if(Mouse.isButtonHeld(Mouse.BUTTON_RIGHT)){
			Display.mainDisplay.LockMouse(true);
			Camera.Main_Camera.Rotate((float)Mouse.getDY() * MOUSE_SENSITIVITY * delta, (float)Mouse.getDX() * MOUSE_SENSITIVITY * delta, 0f);
		}else{
			Display.mainDisplay.LockMouse(false);
		}
        
        MoveCamera(delta);
		
//		loadChunks.update(delta, Camera.Main_Camera);
		world.update(delta);

		//GUI Updates
//		fpsLabel.setText("FPS: " + Timer.FPS);
//		upsLabel.setText("UPS: " + Timer.UPS);
//		memoryLabel.setText("Used Memory: " + SystemInfo.getUsedMemory() + "/" + (SystemInfo.maxMemory / SystemInfo.MB) + "MB");
//
//		Vector3f pos = camera.GetPosition();
//		camPosLabel.setText("Pos: " + pos.x + ", " + pos.y + ", " + pos.z);
//		Vector3f rot = camera.GetRotation().getEulerAnglesXYZ(new Vector3f(1, 0, 1));
//		camRotLabel.setText("Rot: " + rot.x + ", " + rot.z);
//
//		guiCanvas.update(deltaTime);

//		RayHitInfo hit = plane.getRayHit(ray);
//		if(hit != null){
//			Debug.log("Hit Dist: " + hit.distance);
//		}

		//Debug.log("" + rect.collides(line));//TODO: Fix Collision
	}
    
	private void MoveCamera(float delta){
        float yaw = Camera.Main_Camera.GetEulerRotation().y;
        float Velocity = Keyboard.isPressed(Keyboard.KEY_LEFT_SHIFT) ? CAMERA_POS_STEP * 4f : CAMERA_POS_STEP;
        Velocity *= delta;
        
        Vector3f movement = new Vector3f();
        
        if(Keyboard.isPressed(Keyboard.KEY_W)){
            movement.z -= Velocity;
        }
        if(Keyboard.isPressed(Keyboard.KEY_S)){
            movement.z += Velocity;
        }
        if(Keyboard.isPressed(Keyboard.KEY_A)){
            movement.x -= Velocity;
        }
        if(Keyboard.isPressed(Keyboard.KEY_D)){
            movement.x += Velocity;
        }
        if(Keyboard.isPressed(Keyboard.KEY_SPACE)){
            movement.y += Velocity;
        }
        if(Keyboard.isPressed(Keyboard.KEY_LEFT_ALT)){
            movement.y -= Velocity;
        }
        
        Vector3f newPos = new Vector3f();
        
        if ( movement.z != 0 ) {
            newPos.x += (float)Math.sin(Math.toRadians(yaw)) * -1.0f * movement.z;
            newPos.z += (float)Math.cos(Math.toRadians(yaw)) * movement.z;
        }
        if ( movement.x != 0) {
            newPos.x += (float)Math.sin(Math.toRadians(yaw - 90)) * -1.0f * movement.x;
            newPos.z += (float)Math.cos(Math.toRadians(yaw - 90)) * movement.x;
        }
        newPos.y += movement.y;
        
        Camera.Main_Camera.MovePosition(newPos);
    }
    
	@Override
	public void render() {
//		renderer.render(skybox);

		//world.render(renderer);
		//renderer.render(ObjectManager.getGameObjects());

//		ray.render(renderer, new Color(1f, 0, 0));
//		rect.render(renderer, new Color(0, 0, 1f));
//		plane.render(renderer, new Color(0, 1f, 0));
//		line.render(renderer, new Color(1f, 0, 1f));

//		renderer.RenderGUI(guiCanvas);
	}

	@Override
	public void cleanup() {
		loadChunks.cleanup();
		world.cleanup();
	}

}
