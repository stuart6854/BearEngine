package org.bearengine.graphics.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Mesh {

    public Material material;

    private float[] vertices;
    private float[] uvs;
    private float[] normals;

    public Mesh(){

    }

    public void SetVertices(List<Float> verticesList){
        float[] verts = new float[verticesList.size()];
        for(int i = 0; i < verticesList.size(); i++) verts[i] = verticesList.get(i);
        this.vertices = verts;
    }

    public void SetUVs(List<Float> uvsList){
        float[] uvs = new float[uvsList.size()];
        for(int i = 0; i < uvsList.size(); i++) uvs[i] = uvsList.get(i);
        this.uvs = uvs;
    }

    public void SetNormals(List<Float> normalsList){
        float[] normals = new float[normalsList.size()];
        for(int i = 0; i < normalsList.size(); i++) normals[i] = normalsList.get(i);
        this.normals = normals;
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
