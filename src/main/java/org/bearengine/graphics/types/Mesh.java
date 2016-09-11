package main.java.org.bearengine.graphics.types;

import main.java.org.bearengine.utils.Utils;

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
    private float[] colors;

    public RenderModel renderModel;

    public Mesh(){
        this.material = new Material();
    }

    public void SetIndices(List<Integer> indicesList){
        SetIndices(Utils.ToArrayInt(indicesList));
    }

    public void SetVertices(List<Float> verticesList){
        SetVertices(Utils.ToArrayFloat(verticesList));
    }

    public void SetIndices(int[] indices){
        this.indices = indices;
        this.IndicesCount = indices.length;
    }

    public void SetVertices(float[] vertices){
        this.vertices = vertices;
    }

    public void SetUVs(List<Float> uvsList){
        SetUVs(Utils.ToArrayFloat(uvsList));
    }

    public void SetUVs(float[] uvs){
        this.uvs = uvs;
    }

    public void SetNormals(List<Float> normalsList){
        SetNormals(Utils.ToArrayFloat(normalsList));
    }

    public void SetNormals(float[] normals){
        this.normals = normals;
    }

    public void SetColors(List<Float> colorsList){
        SetColors(Utils.ToArrayFloat(colorsList));
    }

    public void SetColors(float[] colors){
        this.colors = colors;
    }

    public void CreateRenderModel(){
//	    if(colors == null || colors.length == 0)
//	    	SetDefaultColors();

        if(uvs != null && uvs.length == 0)
            uvs = null;

        if(normals != null && normals.length == 0)
            normals = null;

        renderModel = RenderModel.CreateModel(indices, vertices, uvs, normals, colors);
//        if(normals != null && normals.length > 0) {
//            renderModel = RenderModel.Create3DModel(indices, vertices, uvs, normals);
////            Debug.log("Mesh -> Creating 3D RenderModel(has Normals).");
//        } else{
//            renderModel = RenderModel.Create2DModel(indices, vertices, uvs);
////            Debug.log("Mesh -> Creating 2D RenderModel(no Normals).");
//        }
    }

    public int[] GetIndices(){
        return indices;
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

    public float[] GetColors(){
        return colors;
    }

    public void Cleanup(){
        if(renderModel != null)
            renderModel.ReleaseModel();
    }

    private void SetDefaultColors(){
	    Color defaultColor = Color.WHITE;

	    int verts = vertices.length / 3;
		float[] colors = new float[verts * 4];
	    for(int i = 0; i < colors.length; i += 4){
		    colors[i] = defaultColor.r;
		    colors[i + 1] = defaultColor.g;
		    colors[i + 2] = defaultColor.b;
		    colors[i + 3] = defaultColor.a;
	    }
	    SetColors(colors);
    }

}
