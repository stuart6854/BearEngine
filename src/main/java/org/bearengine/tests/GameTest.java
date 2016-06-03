package main.java.org.bearengine.tests;

import main.java.org.bearengine.core.Game;
import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.importers.OBJImporter;
import main.java.org.bearengine.graphics.rendering.BatchRenderer;
import main.java.org.bearengine.graphics.rendering.MeshBatchRenderer;
import main.java.org.bearengine.graphics.rendering.Renderer;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.*;
import main.java.org.bearengine.objects.Camera;
import main.java.org.bearengine.objects.GameObject;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.joml.Matrix4f;
import main.java.org.bearengine.graphics.shaders.Shader;

import java.util.ArrayList;
import java.util.List;

public class GameTest extends Game {

    BatchRenderer renderer;
    MeshBatchRenderer meshRenderer;
    Texture texture;
    Mesh mesh;

    GameObject object;

	public GameTest() {
		super();
	}

	@Override
	public void init() {
        Debug.log("GameTest -> Init.");

        Shader vertShader = new Shader("shaders/default_vertex.vert", Shader.VERTEX_SHADER);
        vertShader.LoadSourceCode();
        vertShader.CompileShader();
        Shader fragShader = new Shader("shaders/default_fragment.frag", Shader.FRAGMENT_SHADER);
        fragShader.LoadSourceCode();
        fragShader.CompileShader();

        ShaderProgram shaderProgram = new ShaderProgram();
        shaderProgram.Initialise();
        shaderProgram.AttachShader(vertShader);
        shaderProgram.AttachShader(fragShader);
        shaderProgram.Link();
        //shaderProgram.SpecifyVertexAttribute("position", 3);
        //shaderProgram.SpecifyVertexAttribute("texcoord", 2);
        //shaderProgram.SpecifyVertexAttribute("normal", 3);

        float x = 2;
        float y = x / Display.mainDisplay.Aspect;
        //Camera.Main_Camera.SetProjection(new Matrix4f().ortho(-x, x, -y, y, 1, -1));
        Camera.Main_Camera.SetProjection(new Matrix4f().perspective((float)Math.toRadians(60), Display.mainDisplay.Aspect, 0.01f, 1000.0f));
        Camera.Main_Camera.SetPosition(0, 0, 1);

//		renderer = new BatchRenderer(Camera.Main_Camera, shaderProgram);
//        renderer.Init();
//
//        meshRenderer = new MeshBatchRenderer(Camera.Main_Camera, shaderProgram);
//        meshRenderer.Init();

        texture = new Texture().UploadTexture(Image.GetImage("textures/placeholder_orange_256.jpg", ResourceLoader.FileType.Internal));

        OBJImporter importer = new OBJImporter();
        mesh = importer.LoadMesh("models/square.obj");
        mesh.material.shaderProgram = shaderProgram;
        mesh.material.SetTexture(texture);

        object = new GameObject();
        object.setMesh(mesh);
        object.SetPosition(0, 0, -5);

        Display.mainDisplay.SetClearColor(new Color(1, 0, 1));

        Debug.log("GameTest -> Init End.");
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void render() {
        Renderer.Render();
	}

	@Override
	public void cleanup() {
		if(renderer != null) renderer.Cleanup();
	}

}
