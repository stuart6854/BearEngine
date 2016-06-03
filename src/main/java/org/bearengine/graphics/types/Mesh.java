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

    }

    public void SetIndices(List<Integer> indicesList){
        int[] indices = new int[indicesList.size()];
        for(int i = 0; i < indicesList.size(); i++)
            indices[i] = indicesList.get(i);

        this.indices = indices;
        this.IndicesCount = indices.length;
    }

    public void SetVertices(List<Float> verticesList){
        float[] verts = new float[verticesList.size()];
        for(int i = 0; i < verticesList.size(); i++)
            verts[i] = verticesList.get(i);

        this.vertices = verts;
    }

    public void SetUVs(List<Float> uvsList){
        float[] uvs = new float[uvsList.size()];
        for(int i = 0; i < uvsList.size(); i++)
            uvs[i] = uvsList.get(i);

        this.uvs = uvs;
    }

    public void SetNormals(List<Float> normalsList){
        float[] normals = new float[normalsList.size()];
        for(int i = 0; i < normalsList.size(); i++)
            normals[i] = normalsList.get(i);

        this.normals = normals;
    }

    public void CreateRenderModel(){
        if(normals == null || normals.length > 0) {
            renderModel = RenderModel.Create3DModel(vertices, uvs, normals, indices);
            Debug.log("Mesh -> Creating 3D RenderModel(has normals).");
        } else{
            renderModel = RenderModel.Create2DModel(vertices, uvs, indices);
            Debug.log("Mesh -> Creating 2D RenderModel(no normals).");
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

}
