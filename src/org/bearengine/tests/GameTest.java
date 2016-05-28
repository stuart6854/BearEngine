package org.bearengine.tests;

import org.bearengine.core.Game;
import org.bearengine.debug.Debug;
import org.bearengine.graphics.Display;
import org.bearengine.graphics.importers.OBJImporter;
import org.bearengine.graphics.rendering.BatchRenderer;
import org.bearengine.graphics.rendering.MeshBatchRenderer;
import org.bearengine.graphics.shaders.Shader;
import org.bearengine.graphics.shaders.ShaderProgram;
import org.bearengine.graphics.types.*;
import org.bearengine.objects.Camera;
import org.joml.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengles.QCOMAlphaTest;

public class GameTest extends Game {

    BatchRenderer renderer;
    MeshBatchRenderer meshRenderer;
    Quaterniond rot = new Quaterniond().normalize();
    Texture texture;
    Mesh mesh;

	public GameTest() {
		super();
	}

	@Override
	public void init() {
        Shader vertShader = new Shader("resources/shaders/default_vertex.vert", Shader.VERTEX_SHADER);
        vertShader.LoadSourceCode();
        vertShader.CompileShader();
        Shader fragShader = new Shader("resources/shaders/default_fragment.frag", Shader.FRAGMENT_SHADER);
        fragShader.LoadSourceCode();
        fragShader.CompileShader();

        ShaderProgram shaderProgram = new ShaderProgram();
        shaderProgram.Initialise();
        shaderProgram.AttachShader(vertShader);
        shaderProgram.AttachShader(fragShader);
        shaderProgram.Link();
        shaderProgram.SpecifyVertexAttribute("position", 3);
        shaderProgram.SpecifyVertexAttribute("color", 4);
        shaderProgram.SpecifyVertexAttribute("texcoord", 2);
        shaderProgram.SpecifyVertexAttribute("normal", 3);

        float x = 1;
        float y = x / Display.mainDisplay.Aspect;
        Matrix4f m_ortho = new Matrix4f().ortho(-x, x, -y, y, 1, -1);
        Camera.Main_Camera.SetProjection(m_ortho);

		renderer = new BatchRenderer(Camera.Main_Camera, shaderProgram);
        renderer.Init();

        meshRenderer = new MeshBatchRenderer(Camera.Main_Camera, shaderProgram);
        meshRenderer.Init();

        texture = new Texture().UploadTexture(Image.loadImage("resources/textures/placeholder_orange_256.jpg"));

        OBJImporter importer = new OBJImporter();
        mesh = importer.LoadMesh("resources/models/textured-cube.obj");

        Debug.log("GameTest -> Init");
	}

	@Override
	public void update(float deltaTime) {
        rot.rotateY(1f * deltaTime);
        //rot.rotateX(0.5f * deltaTime);
	}

	@Override
	public void render() {
        meshRenderer.Begin();

        meshRenderer.AddMesh(mesh, new Matrix4d().translate(0, 0f, 0).rotate(rot).scale(.25f));

        meshRenderer.End();

//		renderer.Begin();
//
//        texture.Bind();
//
//        renderer.AddMesh(null, new Matrix4d().translate(-0.4f, 0.2f, 0).rotate(rot).scale(.5f));
//
//        renderer.AddVertex(new Vector3d(.2f, -.2f, 0), new Color(1, 1, 1), new Vector2f(1, 0));
//        renderer.AddVertex(new Vector3d(-.2f, .2f, 0), new Color(1, 1, 1), new Vector2f(0, 1));
//        renderer.AddVertex(new Vector3d(.2f, .2f, 0), new Color(1, 1, 1), new Vector2f(1, 1));
//
//        renderer.End();
	}

	@Override
	public void cleanup() {
		if(renderer != null) renderer.Cleanup();
	}

}
