package org.bearengine.graphics.rendering;

import org.bearengine.graphics.shaders.ShaderProgram;
import org.bearengine.graphics.types.Mesh;
import org.bearengine.math.types.Matrix4;

/**
 * Created by Stuart on 22/05/2016.
 */
public class MeshBatchRenderer extends BatchRenderer{

    //TODO: Need some sort of list/array, which i can sort, to store meshes

    public MeshBatchRenderer(Matrix4 projection, ShaderProgram shaderProgram){
        super(projection, shaderProgram);
    }

    public void AddMesh(Mesh mesh){

    }

    private void SortMeshesByTexture(){

    }

}
