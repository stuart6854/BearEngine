package org.bearengine.graphics.rendering;

import org.bearengine.debug.Debug;
import org.bearengine.graphics.shaders.ShaderProgram;
import org.bearengine.graphics.types.Mesh;
import org.bearengine.objects.Camera;
import org.joml.Matrix4d;
import org.joml.Vector2f;
import org.joml.Vector3d;

/**
 * Created by Stuart on 22/05/2016.
 */
public class MeshBatchRenderer extends BatchRenderer{

    //TODO: Need some sort of list/array, which i can sort, to store meshes

    public MeshBatchRenderer(Camera camera, ShaderProgram shaderProgram){
        super(camera, shaderProgram);
    }

    public void AddMesh(Mesh mesh, Matrix4d transform){
        float[] verts = mesh.GetVertices();
        float[] uvs = mesh.GetUvs();
        float[] norms = mesh.GetNormals();
        for(int i = 0; i < verts.length / 3; i++){
            Vector3d pos = new Vector3d(verts[i * 3], verts[i * 3 + 1], verts[i * 3 + 2]);
            Vector2f uv = new Vector2f(uvs[i * 2], uvs[i * 2 + 1]);
            Vector3d normal = new Vector3d(norms[i * 3], norms[i * 3 + 1], norms[i * 3 + 2]);

            AddVertex(transform.transformPosition(pos), uv, normal);
        }
    }

    private void SortMeshesByTexture(){

    }

}
