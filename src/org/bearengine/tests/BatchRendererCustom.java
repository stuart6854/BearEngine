package org.bearengine.tests;

import org.bearengine.graphics.rendering.BatchRenderer;
import org.bearengine.graphics.shaders.ShaderProgram;
import org.bearengine.objects.Camera;
import org.joml.Matrix4f;

/**
 * Created by Stuart on 20/05/2016.
 */
public class BatchRendererCustom extends BatchRenderer {

    public BatchRendererCustom(Camera camera, ShaderProgram shaderProgram){
        super(camera, shaderProgram);
    }

}
