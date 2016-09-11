package main.java.org.bearengine.tests;

import main.java.org.bearengine.core.Engine;
import main.java.org.bearengine.core.Game;
import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.font.Font;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.importers.OBJImporter;
import main.java.org.bearengine.graphics.rendering.RenderSpace;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Color;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.objects.Camera;
import main.java.org.bearengine.objects.GameObject;
import main.java.org.bearengine.ui.*;
import main.java.org.bearengine.utils.File;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.joml.Matrix4f;

public class GameTest extends Game {

    Texture texture;
    Mesh mesh;
    GameObject object;

    Canvas canvas;
    Button button;
    Label label;
    InputField inputField;
    ScrollPane scrollPane;
    ScrollBar scrollBar;
    Label scrollLabel;

	public GameTest() {
		super();
	}

	@Override
	public void init() {
        Debug.log("GameTest -> Init.");
        Display.mainDisplay.SetClearColor(Color.SKY_BLUE);

        Camera.Main_Camera.SetProjection(new Matrix4f().perspective((float)Math.toRadians(60.0f), Display.mainDisplay.Aspect, 0.1f, 1000.0f));
        Camera.Main_Camera.SetPosition(0, 2, 1);

        texture = new Texture().UploadTexture(ResourceLoader.Load("/main/java/resources/textures/placeholder_orange_256.png", Image.class));

        OBJImporter importer = new OBJImporter();
        Mesh mesh = importer.LoadMesh("/main/java/resources/models/textured-cube.obj");
        mesh.material.SetTexture(texture);
        mesh.material.shaderProgram = ShaderProgram.DEFAULT;
        mesh.material.RenderCamera = Camera.Main_Camera;

        object = new GameObject();
        object.SetMesh(mesh);
        object.SetPosition(0, 0, -5);

        Image fontImage = ResourceLoader.Load("/main/java/resources/fonts/OpenSans_61_DF.png", Image.class, false);
        Texture texture = new Texture().UploadTexture(fontImage);
        Font font = new Font(0.2f, texture, ResourceLoader.Load("/main/java/resources/fonts/OpenSans_61_DF.fnt", File.class));

        canvas = new Canvas(RenderSpace.SCREEN_SPACE);
//        canvas = new Canvas(RenderSpace.WORLD_SPACE);
//        canvas.SetPixelOffset(0, 0, -5);//Next 3 lines used for World-Space Canvas
//        canvas.SetWidth(4);
//        canvas.SetHeight(4);
        canvas.SetShowDebugMesh(true);

        Panel panel = new Panel();
        panel.SetNormalisedPosition(0, 0);
        panel.SetPixelOffset(10, 10, 0);
        panel.SetWidth(512);
        panel.SetHeight(400);
        panel.SetShowDebugMesh(true);
        panel.SetParent(canvas);

        button = new Button("Click Me!", font, panel);
        button.SetPixelOffset(10, 10, 0);
        button.SetWidth(256);
        button.SetHeight(64);
        button.SetShowDebugMesh(true);

        label = new Label("Hello World!\nNew Line Test", font);
        label.SetPixelOffset(10, 375, 0);
        label.SetShowDebugMesh(true);
        label.SetParent(panel);

//        label = new Label("ABCDEFG", font);
//        label.SetPixelOffset(10, 10, 0);
//        label.SetShowDebugMesh(true);
//        label.SetParent(canvas);
//
        Label label2 = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit.\nMauris nisl diam, feugiat at mollis eget, tincidunt et est. Integer ex.", font);
        label2.SetPixelOffset(32, 500, 0);
        label2.SetShowDebugMesh(true);
        label2.SetParent(canvas);

        inputField = new InputField(font, panel);
        inputField.SetPixelOffset(10, 128, 0);
        inputField.SetWidth(200);
        inputField.SetHeight(32);
        inputField.SetShowDebugMesh(true);

        scrollBar = new ScrollBar(panel);
        scrollBar.SetNormalisedPosition(1, 1);
        scrollBar.SetPixelOffset(-16, -256, 0);
        scrollBar.SetWidth(16);
        scrollBar.SetHeight(256);
        scrollBar.SetShowDebugMesh(true);

        scrollPane = new ScrollPane(panel);
        scrollPane.SetNormalisedPosition(1, 1);
        scrollPane.SetPixelOffset(-256 - 16, -256, 0);
        scrollPane.SetWidth(256);
        scrollPane.SetHeight(256);
        scrollPane.SetShowDebugMesh(true);
        scrollPane.SetScrollBarVertical(scrollBar);

        scrollLabel = new Label("Scroll Label.", font);
        scrollLabel.SetPixelOffset(10, 10, 0);
        scrollLabel.SetShowDebugMesh(true);
        scrollLabel.SetParent(scrollPane);

        Debug.log("GameTest -> Init End.");
	}

	@Override
	public void update(float deltaTime) {
        object.Rotate(0, 1f * deltaTime, 0);

        if(button.IsClicked()){
            Debug.log("Clicked:" + System.currentTimeMillis());
        }

        canvas.Update();
	}

	@Override
	public void render() {

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
