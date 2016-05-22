package org.bearengine.tests;

import org.bearengine.core.Game;
import org.bearengine.debug.Debug;
import org.bearengine.graphics.Display;
import org.bearengine.graphics.rendering.BatchRenderer;
import org.bearengine.graphics.shaders.Shader;
import org.bearengine.graphics.shaders.ShaderProgram;
import org.bearengine.graphics.types.Color;
import org.bearengine.graphics.types.Image;
import org.bearengine.graphics.types.Texture;
import org.bearengine.math.types.Matrix4;
import org.bearengine.math.types.Vector2;
import org.bearengine.math.types.Vector3;

public class GameTest extends Game {

    BatchRenderer renderer;

    Texture texture;

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

        float x = 1;
        float y = x / Display.mainDisplay.Aspect;
		renderer = new BatchRenderer(Matrix4.orthographic(-x, x, -y, y, 1, -1), shaderProgram);
        renderer.Init();

        texture = new Texture().UploadTexture(Image.loadImage("resources/textures/placeholder_orange_64.jpg"));

        Debug.log("GameTest -> Init");
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void render() {
		renderer.Begin();

        texture.Bind();

        renderer.AddVertex(new Vector3(-.2f, .2f, 0), new Color(1, 1, 1), new Vector2(0, 1));
        renderer.AddVertex(new Vector3(-.2f, -.2f, 0), new Color(1, 1, 1), new Vector2(0, 0));
        renderer.AddVertex(new Vector3(.2f, -.2f, 0), new Color(1, 1, 1), new Vector2(1, 0));

        renderer.AddVertex(new Vector3(.2f, -.2f, 0), new Color(1, 1, 1), new Vector2(1, 0));
        renderer.AddVertex(new Vector3(-.2f, .2f, 0), new Color(1, 1, 1), new Vector2(0, 1));
        renderer.AddVertex(new Vector3(.2f, .2f, 0), new Color(1, 1, 1), new Vector2(1, 1));

        renderer.End();
	}

	@Override
	public void cleanup() {
		if(renderer != null) renderer.Cleanup();
	}

}
