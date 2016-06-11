package main.java.org.bearengine.tests;

import main.java.org.bearengine.core.Engine;
import main.java.org.bearengine.core.Game;
import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.font.Font;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.importers.OBJImporter;
import main.java.org.bearengine.graphics.rendering.Renderer;
import main.java.org.bearengine.graphics.shaders.Shader;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Color;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.objects.Camera;
import main.java.org.bearengine.objects.GameObject;
import main.java.org.bearengine.ui.Canvas;
import main.java.org.bearengine.ui.Panel;
import main.java.org.bearengine.ui.UIObject;
import main.java.org.bearengine.utils.FileUtils;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.joml.Matrix4f;

public class GameTest extends Game {

    Texture texture;
    Mesh mesh;
    GameObject object;
    GameObject textObj;

	public GameTest() {
		super();
	}

	@Override
	public void init() {
        Debug.log("GameTest -> Init.");
        Display.mainDisplay.SetClearColor(new Color(1, 0, 1));

        Camera.Main_Camera.SetProjection(new Matrix4f().perspective((float)Math.toRadians(60.0f), Display.mainDisplay.Aspect, 0.1f, 1000.0f));
        Camera.Main_Camera.SetPosition(0, 0, 1);

        texture = new Texture().UploadTexture(Image.GetImage("textures/placeholder_orange_256.jpg", ResourceLoader.FileType.Internal));

        OBJImporter importer = new OBJImporter();
        mesh = importer.LoadMesh("models/textured-cube.obj");
        mesh.material.SetTexture(texture);
        mesh.material.shaderProgram = ShaderProgram.DEFAULT;
        mesh.material.RenderCamera = Camera.Main_Camera;

        object = new GameObject();
        object.setMesh(mesh);
        object.SetPosition(0, -1, -5);


//        FontTest.CreateFont();
//        Camera guiCamera = new Camera(new Matrix4f().ortho2D(0, Display.mainDisplay.getWidth()/2, Display.mainDisplay.getHeight()/2, 0));
//        textObj = new GameObject();
//        textObj.Name = "TextObj";
//        textObj.setMesh(FontTest.CreateMesh("Hello World!\nHow are you today?\n1234567890", 0, 0, Color.BLACK));
//        textObj.GetMesh().material.shaderProgram = guiShaderProgram;
//        textObj.GetMesh().material.RenderCamera = guiCamera;
//        textObj.SetPosition(0, 0, 0);
//        textObj.SetScale(1f, 1f, 0f);

        Image fontImage = Image.GetImage("fonts/OpenSans_DF.png", ResourceLoader.FileType.Internal);
        Texture texture = new Texture().UploadTexture(fontImage);
        Font font = new Font(texture, FileUtils.GetResourceStream("fonts/OpenSans_DF.fnt"));

        Canvas canvas = new Canvas(Canvas.RenderMode.ScreenSpace);
        Panel panel = new Panel(100, 100);
        canvas.AddChild(panel);
        panel.SetPosition(10, 10, 0);

        Debug.log("GameTest -> Init End.");
	}

	@Override
	public void update(float deltaTime) {
//        object.Rotate(0, 1f * deltaTime, 0);
	}

	@Override
	public void render() {
        Renderer.RenderUI();
        Renderer.RenderObjects();

        //FontTest.DrawString("Test", 0, 0, Color.BLACK);
	}

	@Override
	public void cleanup() {

	}

    public static void main(String... args){
        Game game = new GameTest();
        Engine engine = new Engine(120, 120, game);
        engine.start();
    }

}
