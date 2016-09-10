package main.java.org.bearengine.graphics.types;

import main.java.org.bearengine.debug.Debug;

import java.util.List;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Mesh {

    public String Mesh_Name = "";

    public Material material;

    public int IndicesCount;
    private int[] indices;
    private float[] vertices;
    private float[] uvs;
    private float[] normals;

    public RenderModel renderModel;

    public Mesh(){
        this.material = new Material();
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

    public void SetUVs(List<Float> uvsList){
        if(uvsList.size() == 0) return;
        float[] uvs = new float[uvsList.size()];
        for(int i = 0; i < uvsList.size(); i++)
            uvs[i] = uvsList.get(i);

        this.uvs = uvs;
    }

    public void SetNormals(List<Float> normalsList){
        if(normalsList.size() == 0) return;
        float[] normals = new float[normalsList.size()];
        for(int i = 0; i < normalsList.size(); i++)
            normals[i] = normalsList.get(i);

        this.normals = normals;
    }

    public void CreateRenderModel(){
        if(normals != null && normals.length > 0) {
            renderModel = RenderModel.Create3DModel(vertices, uvs, normals, indices);
//            Debug.log("Mesh -> Creating 3D RenderModel(has Normals).");
        } else{
            renderModel = RenderModel.Create2DModel(vertices, uvs, indices);
//            Debug.log("Mesh -> Creating 2D RenderModel(no Normals).");
        }
    }

    public float[] GetVertices(){
        return vertices;
    }

    public float[] GetUvs() {
        return uvs;
    }

    public float[] GetNormals() {
        return normals;
    }

    public void Cleanup(){
        if(renderModel != null)
            renderModel.ReleaseModel();
    }

}
