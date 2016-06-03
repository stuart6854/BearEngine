package main.java.org.bearengine.graphics.importers;

import main.java.org.bearengine.graphics.types.Mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 27/05/2016.
 */
public abstract class ModelImporter {

    protected List<Integer> indices = new ArrayList<>();
    protected List<Float> vertices = new ArrayList<>();
    protected List<Float> uvs = new ArrayList<>();
    protected List<Float> normals = new ArrayList<>();

    public abstract Mesh LoadMesh(String path);

}
