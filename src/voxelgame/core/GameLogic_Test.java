package voxelgame.core;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import main.java.org.bearengine.core.Game;
import main.java.org.bearengine.input.Keyboard;
import main.java.org.bearengine.input.Mouse;
import main.java.org.bearengine.objects.Camera;
import main.java.org.bearengine.graphics.*;
import main.java.org.joml.Quaternionf;
import main.java.org.joml.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

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

	private Vector3f cameraInc;
	private final float CAMERA_POS_STEP = 10.0f;
	private static final float MOUSE_SENSITIVITY = 6.5f;

	private World world;

	private LoadChunks loadChunks;

//	GameObject testObject, testObject2;

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
		Camera.Main_Camera.SetPosition(-5, 153, 10);
//		skybox = new Skybox(skyboxTextureFiles);

		cameraInc = new Vector3f();
		world = new World("res/textures/texture.png");

		loadChunks = new LoadChunks(world);

//		Mesh mesh = OBJLoader.loadMesh("res/models/cube.obj").flushToBuffers();
//
//        Texture texture = new Texture("res/textures/grassblock.png", false);
//        Material material = new Material(texture, 0.0f, false);
//        //Material material = new Material(new Color(1f, 0f, 0f), 0.0f, false);
//        mesh.setMaterial(material);

//		testObject = new GameObject();
//        testObject.setMesh(mesh);
//        testObject.getTransform().setPosition(new Vector3f(-3f, 150, 0));
//        testObject.getTransform().setScale(1f);
//        ObjectManager.addGameObject(testObject);
//
//        testObject2 = new GameObject();
//        testObject2.setMesh(mesh);
//        testObject2.getTransform().setPosition(new Vector3f(-6f, 150, 0));
//        testObject2.getTransform().setScale(1f);
//        ObjectManager.addGameObject(testObject2);

        loadChunks.getTransform().setPosition(new Vector3f(0, 0, 0));

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
//		billboard.getTransform().setPosition(new Vector3f(-2, 154, 0));
//		billboard.getBoundingBox().updateBounds(billboard.getMesh());
	}

//	@Override
//	public void input(Window window) {
//		cameraInc.set(0, 0, 0);
//		float camSpeed = Keyboard.isDown(Keyboard.KEY_LEFT_SHIFT) ? CAMERA_POS_STEP * 4f : CAMERA_POS_STEP;
//
//		if(Keyboard.isDown(Keyboard.KEY_W)){
//			cameraInc.z = -camSpeed;
//		}else if(Keyboard.isDown(Keyboard.KEY_S)){
//			cameraInc.z = camSpeed;
//		}
//		if(Keyboard.isDown(Keyboard.KEY_A)){
//			cameraInc.x = -camSpeed;
//		}else if(Keyboard.isDown(Keyboard.KEY_D)){
//			cameraInc.x = camSpeed;
//		}
//		if(Keyboard.isDown(Keyboard.KEY_SPACE)){
//			cameraInc.y = camSpeed;
//		}else if(Keyboard.isDown(Keyboard.KEY_LEFT_ALT)){
//			cameraInc.y = -camSpeed;
//		}
//	}

	@Override
	public void update(float deltaTime) {
		Camera.Main_Camera.MovePosition(cameraInc.x * deltaTime, cameraInc.y * deltaTime, cameraInc.z * deltaTime);
//		mousePicker.update();

		//Update camera based on mouse
		if(Mouse.isButtonHeld(Mouse.BUTTON_RIGHT)){
			Display.mainDisplay.LockMouse(true);
			Camera.Main_Camera.Rotate((float)Mouse.getDY() * MOUSE_SENSITIVITY * deltaTime, (float)Mouse.getDX() * MOUSE_SENSITIVITY * deltaTime, 0f);
		}else{
			Display.mainDisplay.LockMouse(false);
		}

		//loadChunks.update(deltaTime, camera);
		//world.update(deltaTime);

		//GUI Updates
//		fpsLabel.setText("FPS: " + Timer.FPS);
//		upsLabel.setText("UPS: " + Timer.UPS);
//		memoryLabel.setText("Used Memory: " + SystemInfo.getUsedMemory() + "/" + (SystemInfo.maxMemory / SystemInfo.MB) + "MB");
//
//		Vector3f pos = camera.getTransform().getPosition();
//		camPosLabel.setText("Pos: " + pos.x + ", " + pos.y + ", " + pos.z);
//		Vector3f rot = camera.getTransform().getRotation().getEulerAnglesXYZ(new Vector3f(1, 0, 1));
//		camRotLabel.setText("Rot: " + rot.x + ", " + rot.z);
//
//		guiCanvas.update(deltaTime);

//		RayHitInfo hit = plane.getRayHit(ray);
//		if(hit != null){
//			Logger.debug("Hit Dist: " + hit.distance);
//		}

		//Logger.debug("" + rect.collides(line));//TODO: Fix Collision
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
