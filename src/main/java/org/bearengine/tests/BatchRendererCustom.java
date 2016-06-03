package main.java.org.bearengine.tests;

import main.java.org.bearengine.graphics.rendering.BatchRenderer;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.objects.Camera;

/**
 * Created by Stuart on 20/05/2016.
 */
public class BatchRendererCustom extends BatchRenderer {

    public BatchRendererCustom(Camera camera, ShaderProgram shaderProgram){
        super(camera, shaderProgram);
    }

}
