package main.java.org.bearengine.graphics.importers;

import main.java.org.bearengine.graphics.types.Mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 27/05/2016.
 */
public abstract class ModelImporter {

    protected List<Integer> Indices = new ArrayList<>();
    protected List<Float> Vertices = new ArrayList<>();
    protected List<Float> UVs = new ArrayList<>();
    protected List<Float> Normals = new ArrayList<>();

    public abstract Mesh LoadMesh(String path);

    protected void NewMesh(){
        Indices.clear();
        Vertices.clear();
        UVs.clear();
        Normals.clear();
    }

}
