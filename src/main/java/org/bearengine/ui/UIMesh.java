package main.java.org.bearengine.ui;

import main.java.org.bearengine.graphics.types.Material;
import main.java.org.bearengine.graphics.types.Mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 11/06/2016.
 */
public class UIMesh {

    public static Mesh Square(float width, float height){
        Mesh mesh = new Mesh();
        mesh.material = new Material();

        List<Integer> indices = new ArrayList<>();
        indices.add(0); indices.add(1); indices.add(2);
        indices.add(0); indices.add(2); indices.add(3);

        List<Float> vertices = new ArrayList<>();
        vertices.add(0f); vertices.add(0f); vertices.add(0f);
        vertices.add(0f); vertices.add(height); vertices.add(0f);
        vertices.add(width); vertices.add(height); vertices.add(0f);
        vertices.add(width); vertices.add(0f); vertices.add(0f);

        List<Float> uvs = new ArrayList<>();
        uvs.add(0f); uvs.add(1f);
        uvs.add(0f); uvs.add(0f);
        uvs.add(1f); uvs.add(0f);
        uvs.add(1f); uvs.add(1f);

        mesh.SetIndices(indices);
        mesh.SetVertices(vertices);
        mesh.SetUVs(uvs);
        mesh.CreateRenderModel();

        return mesh;
    }

}
