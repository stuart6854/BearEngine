package main.java.org.bearengine.tests;

import main.java.org.bearengine.core.Engine;
import main.java.org.bearengine.core.Game;
import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.font.Font;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.importers.OBJImporter;
import main.java.org.bearengine.graphics.rendering.Renderer;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Color;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.objects.Camera;
import main.java.org.bearengine.objects.GameObject;
import main.java.org.bearengine.ui.*;
import main.java.org.bearengine.utils.File;
import main.java.org.bearengine.utils.FileUtils;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.joml.Matrix4f;

public class GameTest extends Game {

    Texture texture;
    Mesh mesh;
    GameObject object;
    GameObject textObj;

    Canvas canvas;
    Button button;
    Label label;
    InputField inputField;

	public GameTest() {
		super();
	}

	@Override
	public void init() {
        Debug.log("GameTest -> Init.");
        Display.mainDisplay.SetClearColor(new Color(1, 0, 1));

        Camera.Main_Camera.SetProjection(new Matrix4f().perspective((float)Math.toRadians(60.0f), Display.mainDisplay.Aspect, 0.1f, 1000.0f));
        Camera.Main_Camera.SetPosition(0, 0, 1);

        texture = new Texture().UploadTexture(ResourceLoader.Load("/main/java/resources/textures/placeholder_orange_256.png", Image.class));

        OBJImporter importer = new OBJImporter();
        mesh = importer.LoadMesh("/main/java/resources/models/textured-cube.obj");
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

        Image fontImage = ResourceLoader.Load("/main/java/resources/fonts/OpenSans_DF.png", Image.class);
        Texture texture = new Texture().UploadTexture(fontImage);
        Font font = new Font(8, texture, ResourceLoader.Load("/main/java/resources/fonts/OpenSans_DF.fnt", File.class));

        canvas = new Canvas(Canvas.RenderMode.ScreenSpace);
        Panel panel = new Panel(512, 256);
        canvas.AddChild(panel);
        panel.SetPosition(10, 10, 0);
        button = new Button("Click Me!", font, 256, 64);
        button.SetPosition(10, 10, 0);
        panel.AddChild(button);

        label = new Label("Hello World!\nNew Line Test", font);
        label.SetPosition(32, 300, 1);
        panel.AddChild(label);

        Label label2 = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit.\nMauris nisl diam, feugiat at mollis eget, tincidunt et est. Integer ex.", font);
        label2.SetPosition(32, 500, 1);
        panel.AddChild(label2);

        inputField = new InputField(font, 256, 64);
        inputField.SetPosition(10, 128, 0);
        panel.AddChild(inputField);

        Debug.log("GameTest -> Init End.");
	}

	@Override
	public void update(float deltaTime) {
//        object.Rotate(0, 1f * deltaTime, 0);

        if(button.IsClicked()){
            Debug.log("Clicked:" + System.currentTimeMillis());
        }

        canvas.Update();
	}

	@Override
	public void render() {
        Renderer.RenderObjects();
        Renderer.RenderUI();

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
