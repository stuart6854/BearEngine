package main.java.org.bearengine.debug;

import main.java.org.bearengine.graphics.rendering.RenderSpace;
import main.java.org.bearengine.graphics.shaders.Shader;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Material;
import main.java.org.bearengine.graphics.types.RenderModel;
import main.java.org.bearengine.graphics.types.Transform;
import main.java.org.bearengine.objects.Object;

import java.util.List;

/**
 * Created by Stuart on 07/07/2016.
 */
public class DebugMesh {

    private RenderSpace Render_Space;

    public String Mesh_Name = "";

    public Material material;

    public int IndicesCount;
    private int[] indices;
    private float[] vertices;

    public RenderModel renderModel;

    public Transform transform;

    public DebugMesh(){
        this.material = new Material();
        this.material.shaderProgram = ShaderProgram.DEBUG_MESH_SHADER_PROGRAM;
    }

    public void SetIndices(List<Integer> indicesList){
        if(indicesList.size() == 0) return;
        int[] indices = new int[indicesList.size()];
        for(int i = 0; i < indicesList.size(); i++)
            indices[i] = indicesList.get(i);

        SetIndices(indices);
    }

    public void SetVertices(List<Float> verticesList){
        if(verticesList.size() == 0) return;
        float[] verts = new float[verticesList.size()];
        for(int i = 0; i < verticesList.size(); i++)
            verts[i] = verticesList.get(i);

        SetVertices(verts);
    }

    public void SetIndices(int[] indices){
        this.indices = indices;
        this.IndicesCount = indices.length;
    }

    public void SetVertices(float[] vertices){
        this.vertices = vertices;
    }

    public void SetRenderSpace(RenderSpace render_Space){
        this.Render_Space = render_Space;
    }

    public void CreateRenderModel(Transform transform){
        Cleanup();
        renderModel = RenderModel.CreateDebugRenderModel(vertices, indices);
        this.transform = transform;
    }

    public int[] GetIndices() {
        return indices;
    }

    public float[] GetVertices(){
        return vertices;
    }

    public RenderSpace GetRenderSpace(){
        return this.Render_Space;
    }

    public void Cleanup(){
        if(renderModel != null)
            renderModel.ReleaseModel();
    }

}
